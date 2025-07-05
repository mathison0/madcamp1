package com.example.madcamp1.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.madcamp1.R
import com.example.madcamp1.databinding.FragmentDetailBinding
import androidx.navigation.fragment.findNavController


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        Log.d("DetailFragment", "binding.textViewTitle: ${binding.textViewTitle}")

        val problemName = DetailFragmentArgs.fromBundle(requireArguments()).problemName
        Log.d("DetailFragment", "현재 전달받은 문제 이름: $problemName")

        val problemId = DetailFragmentArgs.fromBundle(requireArguments()).problemId

        binding.textViewTitle.text = problemName ?: "문제 정보 없음"

        binding.buttonSolution.setOnClickListener {
            Toast.makeText(context, "풀이로 이동합니다", Toast.LENGTH_SHORT).show()

        }

        binding.checkBoxSolved.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "풀었음", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "체크 해제", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonSolution.setOnClickListener {

            val imageResIds = mutableListOf<Int>()
            var index = 1

            while (true) {
                val imageName = "img_${problemId}_${index}"
                val resId = resources.getIdentifier(imageName, "drawable", requireContext().packageName)

                if (resId != 0) {
                    imageResIds.add(resId)
                    index++
                } else {
                    break // 더 이상 이미지가 없으면 종료
                }
            }

// 이동
            val action = DetailFragmentDirections.actionDetailToProblemDetail(imageResIds.toIntArray())
            findNavController().navigate(action)
        }

        // 이미지 리소스 ID 리스트 생성
        val imageResIds = mutableListOf<Int>()
        var index = 1

        while (true) {
            val imageName = "p_${problemId}_${index}"  // 예: p_32111_1
            val resId = resources.getIdentifier(imageName, "drawable", requireContext().packageName)
            if (resId != 0) {
                imageResIds.add(resId)
                index++
            } else {
                break
            }
        }

        // 이미지 뷰를 imageContainer에 추가
        for (resId in imageResIds) {
            val imageView = ImageView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 600
                ).apply {
                    setMargins(0, 16, 0, 16)
                }
                scaleType = ImageView.ScaleType.FIT_CENTER
                setImageResource(resId)
            }
            binding.imageContainer.addView(imageView)
        }



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
