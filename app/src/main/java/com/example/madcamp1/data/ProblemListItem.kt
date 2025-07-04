package com.example.madcamp1.data

sealed class ProblemListItem {
    data class Header(val title: String) : ProblemListItem()
    data class Item(val name: String) : ProblemListItem()
}