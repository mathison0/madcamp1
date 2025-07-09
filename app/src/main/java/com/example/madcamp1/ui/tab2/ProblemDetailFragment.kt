package com.example.madcamp1.ui.tab2

import android.util.Log
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.example.madcamp1.R
import com.example.madcamp1.databinding.FragmentProblemDetailBinding
import com.github.chrisbanes.photoview.PhotoView

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
        val toolbar = (activity as AppCompatActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "풀이"
        Log.d("ToolbarDebug", "hihi");
        for (resId in images) {
            val photoView = PhotoView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )

                scaleType = ImageView.ScaleType.FIT_CENTER
                load(resId)
            }

            val cardView = androidx.cardview.widget.CardView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 600
                ).apply {
                    setMargins(0, 16, 0, 16)
                }

                radius = 8f               // 테두리 둥글기
                cardElevation = 4f         // 그림자 깊이
                setContentPadding(8, 8, 8, 8)
                useCompatPadding = true    // 그림자 패딩 보정
                addView(photoView)
            }

            binding.imageContainer.addView(cardView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}