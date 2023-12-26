package com.example.finalproject.ui.home

import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.finalproject.DetailActivity
import com.example.finalproject.data.Item
import com.example.finalproject.data.Root

class PlaceViewModel : ViewModel() {

    private val _placeList = MutableLiveData<List<Item>>()
    val placeList: LiveData<List<Item>> get() = _placeList
    private val adapter: PlaceAdapter = PlaceAdapter()

    private val _text = MutableLiveData<String>().apply {
        value = "공원을 검색해서 나만의 산책 루트를 만들어보세요."
    }
    val text: LiveData<String> = _text
    fun processApiResponse(root: Root) {
        val items: List<Item> = root.response.body.items.item
        _placeList.value = items
    }
}