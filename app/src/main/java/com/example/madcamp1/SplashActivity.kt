package com.example.madcamp1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val splashImage: ImageView = findViewById(R.id.splashImage)

        // 애니메이션 로딩 및 시작
        val scaleAnim = AnimationUtils.loadAnimation(this, R.anim.splash_scale)
        splashImage.startAnimation(scaleAnim)

        // 일정 시간 후 MainActivity로 전환
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish() // 스플래시 액티비티는 종료
        }, 2000) // 2초 대기
    }
}
