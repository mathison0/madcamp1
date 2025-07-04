package com.example.madcamp1.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.madcamp1.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 나중에 intent로 데이터 받아서 여기에 표시
        binding.textViewTitle.text = "문제 페이지"
    }
}
