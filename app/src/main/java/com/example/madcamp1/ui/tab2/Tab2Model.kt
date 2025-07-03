package com.example.madcamp1.ui.tab2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Tab2Model : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "풀이"
    }
    val text: LiveData<String> = _text
}