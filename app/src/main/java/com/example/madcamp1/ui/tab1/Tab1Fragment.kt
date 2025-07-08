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
import androidx.core.view.ViewCompat
import com.example.madcamp1.R


class Tab1Fragment : Fragment() {

    private var _binding: FragmentTab1Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var fullList: MutableList<ProblemListItem>
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

        // 리스트 로드 후 isExpanded 설정
        fullList = loadProblemsFromJson(requireContext()).toMutableList()
        fullList.forEach {
            if (it is ProblemListItem.Header) {
                it.isExpanded = true
            }
        }

        adapter = ProblemListAdapter(
            items = emptyList(), // 초기엔 빈 리스트
            onProblemClickListener = { item ->
                val action = Tab1FragmentDirections.actionNavigationTab1ToNavigationDetail(item.name, item.id)
                findNavController().navigate(action)
            },
            onHeaderClick = { header ->
                toggleHeader(header)  // 여기서 토글 함수 호출
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        updateVisibleList() // 초기 표시 리스트 구성

        // 스피너 설정
        val items = listOf("문제", "대회")
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, items)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spinnerFilter.adapter = spinnerAdapter

        // 검색 기능
        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                val selectedOption = binding.spinnerFilter.selectedItem.toString()

                var result = mutableListOf<ProblemListItem>()
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
                            result = result.map { item ->
                                when (item) {
                                    is ProblemListItem.Item -> item.copy(
                                        name = item.name.substringAfter(" - ", item.name)
                                    )
                                    is ProblemListItem.Header -> item  // Header는 그대로
                                }
                            }.toMutableList()

                        }
                        "대회" -> {
                            result.addAll(
                                fullList.filter {
                                    it is ProblemListItem.Header && it.title.contains(query, ignoreCase = true)
                                }.onEach {
                                    (it as ProblemListItem.Header).isExpanded = false
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
        }
        )

        adapter.updateList(fullList)
        Log.d("Tab1Fragment", "초기 리스트 크기: ${fullList.size}")

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val topInset = insets.systemGestureInsets.top
            view.setPadding(0, topInset, 0, 0)
            insets
        }


        return root
    }

    private fun toggleHeader(header: ProblemListItem.Header) {
        fullList.forEach {
            if (it is ProblemListItem.Header) {
                it.isExpanded = it == header && !it.isExpanded
            }
        }
        updateVisibleList()
    }

    private fun updateVisibleList() {
        val newList = mutableListOf<ProblemListItem>()
        var currentHeader: ProblemListItem.Header? = null

        for (item in fullList) {
            when (item) {
                is ProblemListItem.Header -> {
                    newList.add(item)
                    currentHeader = item
                }
                is ProblemListItem.Item -> {
                    if (currentHeader?.isExpanded == true) {
                        newList.add(item)
                    }
                }
            }
        }

        adapter.updateList(newList)
        Log.d("VisibleList", newList.joinToString("\n") { it.toString() })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}