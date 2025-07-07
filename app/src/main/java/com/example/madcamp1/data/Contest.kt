package com.example.madcamp1.data

data class Contest(
    val name: String,           // 대회 이름 (예: UCPC 2023)
    val albumList: List<Album> // 대회 내 포함된 문제 풀이 (앨범들)
)