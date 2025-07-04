package com.example.madcamp1.ui.tab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.madcamp1.databinding.FragmentTab2Binding
import com.example.madcamp1.data.AlbumData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp1.ui.tab2.AlbumAdapter

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
        val albumList = AlbumData.getAlbumList(requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())  // 이게 빠졌어!
        binding.recyclerView.adapter = AlbumAdapter(albumList) { album ->
            val action = Tab2FragmentDirections
                .actionTab2ToProblemDetail(album.images.toIntArray())
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}