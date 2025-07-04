package com.example.madcamp1.ui.tab1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Tab1ViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "문제집"
    }
    val text: LiveData<String> = _text
}