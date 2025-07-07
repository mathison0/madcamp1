package com.example.madcamp1.ui.tab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp1.data.Album
import com.example.madcamp1.data.AlbumData
import com.example.madcamp1.databinding.FragmentContestDetailBinding
import org.json.JSONArray

class ContestDetailFragment : Fragment() {

    private var _binding: FragmentContestDetailBinding? = null
    private val binding get() = _binding!!

    private val contestTitle: String by lazy {
        arguments?.getString("contestTitle") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContestDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val albums = parseContestFromJson(contestTitle)

        if (albums.isEmpty()) {
            Toast.makeText(requireContext(), "대회를 찾을 수 없습니다: $contestTitle", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = AlbumAdapter(albums) { album ->
            val action = ContestDetailFragmentDirections
                .actionContestDetailToProblemDetail(album.images.toIntArray())
            findNavController().navigate(action)
        }

        requireActivity().title = contestTitle
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseContestFromJson(targetContest: String): List<Album> {
        val albums = mutableListOf<Album>()
        val inputStream = requireContext().assets.open("problems.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonString)

        var currentContestName = ""
        var collecting = false

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            when (obj.getString("type")) {
                "header" -> {
                    currentContestName = obj.getString("title")
                    collecting = currentContestName == targetContest
                }
                "item" -> {
                    if (collecting) {
                        val id = obj.getInt("id")
                        val album = AlbumData.createAlbum(requireContext(), id)
                        albums.add(album)
                    }
                }
            }
        }
        return albums
    }
}