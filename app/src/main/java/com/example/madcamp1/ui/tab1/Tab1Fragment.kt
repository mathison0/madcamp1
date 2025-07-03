package com.example.madcamp1.ui.tab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp1.databinding.FragmentTab1Binding

class Tab1Fragment : Fragment() {

    private var _binding: FragmentTab1Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tab1ViewModel =
            ViewModelProvider(this).get(Tab1ViewModel::class.java)

        _binding = FragmentTab1Binding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        tab1ViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}