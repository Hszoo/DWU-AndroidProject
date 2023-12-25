package com.example.finalproject.data

import com.google.gson.annotations.SerializedName
import java.util.Objects

data class Place(
    val body: Body,
    val header: Header
)

data class Header(
    val resultCode: String,
    val resultMsg: String
)

data class Body(
    @SerializedName("items")
    val items: Items,
    val numOfRows: String,
    val pageNo: String,
    val totalCount: String
)

data class Items(
    val item:List<Item>
)
data class Item(
    val title: String,
    @SerializedName("issuedDate")
    val issuedDate: String,
    val category1: String,
    val category2: String,
    val category3: String,
    val information: String,
    val tel: String,
    val url: String,
    val address: String,
    val coordinates: String
)
