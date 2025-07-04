package com.example.madcamp1.ui.tab3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Tab3Model : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "스트릭"
    }
    val text: LiveData<String> = _text
}