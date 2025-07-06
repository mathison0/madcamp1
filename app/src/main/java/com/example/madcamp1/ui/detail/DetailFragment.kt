package com.example.madcamp1.ui.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.chrisbanes.photoview.PhotoView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.madcamp1.databinding.FragmentDetailBinding
import androidx.navigation.fragment.findNavController
import android.widget.ImageView
import java.time.LocalDate
import org.json.JSONArray



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


        // 1. SharedPreferences에서 저장된 체크박스 상태 불러오기
        val prefs = requireContext().getSharedPreferences("checkbox_prefs", Context.MODE_PRIVATE)
        val key = "isChecked_$problemId"

        val editor = prefs.edit()

////       모든 isChecked_로 시작하는 키 제거
//        for ((key, _) in prefs.all) {
//            if (key.startsWith("isChecked_")) {
//                editor.remove(key)
//            }
//        }
//
////      날짜 리스트도 초기화 (JSONArray 버전)
//        editor.putString("checked_date_list", "[]")
//
////      적용
//        editor.apply()

        val isChecked = prefs.getBoolean(key, false)
        // 2. 체크박스 상태 설정
        binding.checkBoxSolved.isChecked = isChecked
        // 3. 이미 체크되어 있으면 다시 건드릴 수 없도록 비활성화
        binding.checkBoxSolved.isEnabled = !isChecked

        binding.checkBoxSolved.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                prefs.edit().putBoolean(key, true).apply()
                binding.checkBoxSolved.isEnabled = false

                // 오늘 날짜를 저장
                val today = LocalDate.now().toString()
                val jsonKey = "checked_date_list"
                val existingJson = prefs.getString(jsonKey, "[]")
                val jsonArray = JSONArray(existingJson)

                jsonArray.put(today)  // 중복 허용

                prefs.edit().putString(jsonKey, jsonArray.toString()).apply()

                Log.d("DetailFragment", "체크한 날짜 저장됨 (JSONArray): $today")
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

        for (resId in imageResIds) {
            val imageView = PhotoView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 16, 0, 16)
                }
                adjustViewBounds = true
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
            imageView.setImageResource(resId)
            binding.imageContainer.addView(imageView)
        }



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
