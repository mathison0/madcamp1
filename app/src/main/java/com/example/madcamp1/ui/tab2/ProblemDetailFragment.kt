package com.example.madcamp1.ui.tab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.example.madcamp1.databinding.FragmentProblemDetailBinding

class ProblemDetailFragment : Fragment() {
    private var _binding: FragmentProblemDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProblemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val images = arguments?.getIntArray("images") ?: return
        for (resId in images) {
            val imageView = ImageView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 600
                ).apply { setMargins(0, 16, 0, 16) }
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
            imageView.load(resId)
            binding.imageContainer.addView(imageView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}