package com.example.finalproject.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "공원을 검색해서 산책 루트를 만들어보세요."
    }
    val text: LiveData<String> = _text
}