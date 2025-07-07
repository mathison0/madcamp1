package com.example.madcamp1.ui.tab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp1.data.ContestAlbum
import com.example.madcamp1.data.AlbumData
import com.example.madcamp1.databinding.FragmentTab2Binding
import org.json.JSONArray

class Tab2Fragment : Fragment() {

    private var _binding: FragmentTab2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTab2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contestList = loadContestList()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = ContestAlbumAdapter(contestList) { contest ->
            val action = Tab2FragmentDirections
                .actionTab2ToContestDetail(contest.title)
            findNavController().navigate(action)
        }
    }

    private fun loadContestList(): List<ContestAlbum> {
        val inputStream = requireContext().assets.open("problems.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonString)

        val contests = mutableListOf<ContestAlbum>()

        var currentContestTitle = ""
        var waitingForFirstItem = false

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            when (obj.getString("type")) {
                "header" -> {
                    currentContestTitle = obj.getString("title")
                    waitingForFirstItem = true
                }
                "item" -> {
                    if (waitingForFirstItem) {
                        val id = obj.getInt("id")
                        val album = AlbumData.createAlbum(requireContext(), id)
                        val firstImage = album.images.firstOrNull() ?: continue
                        contests.add(ContestAlbum(currentContestTitle, firstImage))
                        waitingForFirstItem = false
                    }
                }
            }
        }

        return contests
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}