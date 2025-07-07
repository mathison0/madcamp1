package com.example.madcamp1.data

sealed class ProblemListItem {
    data class Header(val title: String, var isExpanded: Boolean = true) : ProblemListItem()
    data class Item(val name: String, val id: Int) : ProblemListItem()
}