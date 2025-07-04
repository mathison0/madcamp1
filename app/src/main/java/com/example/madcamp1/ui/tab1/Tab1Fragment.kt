package com.example.madcamp1.ui.tab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp1.databinding.FragmentTab1Binding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp1.data.ProblemListItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import com.example.madcamp1.data.JsonProblem


class Tab1Fragment : Fragment() {

    private var _binding: FragmentTab1Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // JSON 로드 함수
    private fun loadProblemsFromJson(context: Context): List<ProblemListItem> {
        val jsonString = context.assets.open("problems.json").bufferedReader().use { it.readText() }

        val gson = Gson()
        val listType = object : TypeToken<List<JsonProblem>>() {}.type
        val rawList: List<JsonProblem> = gson.fromJson(jsonString, listType)

        return rawList.map {
            when (it.type) {
                "header" -> ProblemListItem.Header(it.title ?: "")
                "item" -> ProblemListItem.Item(it.name ?: "")
                else -> throw IllegalArgumentException("Unknown type: ${it.type}")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tab1ViewModel =
            ViewModelProvider(this).get(Tab1ViewModel::class.java)

        _binding = FragmentTab1Binding.inflate(inflater, container, false)
        val root: View = binding.root

        // JSON 파싱 후 RecyclerView 세팅
        val problemList = loadProblemsFromJson(requireContext())
        val adapter = ProblemListAdapter(problemList)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}