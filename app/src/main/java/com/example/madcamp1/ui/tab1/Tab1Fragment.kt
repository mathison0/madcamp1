package com.example.madcamp1.ui.tab1

import androidx.navigation.fragment.findNavController
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.madcamp1.databinding.FragmentTab1Binding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp1.data.ProblemListItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import com.example.madcamp1.data.JsonProblem
import android.text.TextWatcher
import android.text.Editable
import android.util.Log
import android.widget.ArrayAdapter
import com.example.madcamp1.R


class Tab1Fragment : Fragment() {

    private var _binding: FragmentTab1Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var fullList: List<ProblemListItem>
    private lateinit var adapter: ProblemListAdapter

    // JSON 로드 함수
    private fun loadProblemsFromJson(context: Context): List<ProblemListItem> {
        val jsonString = context.assets.open("problems.json").bufferedReader().use { it.readText() }

        val gson = Gson()
        val listType = object : TypeToken<List<JsonProblem>>() {}.type
        val rawList: List<JsonProblem> = gson.fromJson(jsonString, listType)

        return rawList.map {
            when (it.type) {
                "header" -> ProblemListItem.Header(it.title ?: "")
                "item" -> ProblemListItem.Item(it.name ?: "", id = it.id ?: 0)
                else -> throw IllegalArgumentException("Unknown type: ${it.type}")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTab1Binding.inflate(inflater, container, false)
        val root: View = binding.root

        // 전체 리스트 초기화
        fullList = loadProblemsFromJson(requireContext())

        // 1. 어댑터 설정 시 클릭 리스너 전달
        adapter = ProblemListAdapter(
            fullList,
            onProblemClickListener = { item ->
                val action = Tab1FragmentDirections.actionNavigationTab1ToNavigationDetail(item.name, item.id)
                findNavController().navigate(action)
            },
            onHeaderClick = { header ->
                showProblemsUnderHeader(header)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // 스피너 설정
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.search_filter_options,
            android.R.layout.simple_spinner_item
        ).also { spinnerAdapter ->
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerFilter.adapter = spinnerAdapter
        }

        // 검색 기능
        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                val selectedOption = binding.spinnerFilter.selectedItem.toString()

                val result = mutableListOf<ProblemListItem>()
                if (query.isBlank()) {
                    result.addAll(fullList)
                } else {
                    when (selectedOption) {
                        "문제" -> {
                            result.addAll(
                                fullList.filter {
                                    it is ProblemListItem.Item && it.name.contains(query, ignoreCase = true)
                                }
                            )
                        }
                        "대회" -> {
                            result.addAll(
                                fullList.filter {
                                    it is ProblemListItem.Header && it.title.contains(query, ignoreCase = true)
                                }
                            )
                        }
                    }
                }
                adapter.updateList(result)
                Log.d("Tab1Fragment", "업데이트 리스트 크기: ${result.size}")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        adapter.updateList(fullList)
        Log.d("Tab1Fragment", "초기 리스트 크기: ${fullList.size}")

        return root
    }

    private fun showProblemsUnderHeader(selectedHeader: ProblemListItem.Header) {
        val expandedList = mutableListOf<ProblemListItem>()
        var inSection = false
        for (item in fullList) {
            when (item) {
                is ProblemListItem.Header -> {
                    inSection = item.title == selectedHeader.title
                    if (inSection) expandedList.add(item)
                }
                is ProblemListItem.Item -> {
                    if (inSection) expandedList.add(item)
                }
            }
        }
        adapter.updateList(expandedList)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}