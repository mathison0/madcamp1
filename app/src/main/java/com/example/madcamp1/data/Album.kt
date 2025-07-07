package com.example.madcamp1.data

data class Album(
    val id: Int, // 문제 번호
    val title: String, // "백준 {문제 번호}번 문제
    val images: List<Int> // drawable 에 있는 이미지 고유 id
)