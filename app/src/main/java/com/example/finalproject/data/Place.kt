package com.example.finalproject.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Objects
data class Root(
    val response: Response,
)

data class Response(
    val header: Header,
    val body: Body,
)

data class Header(
    val resultCode: String,
    val resultMsg: String,
)

data class Body(
    val items: Items,
    val numOfRows: String,
    val pageNo: String,
    val totalCount: String,
)

data class Items(
    val item: List<Item>,
)

@Entity(tableName = "place_table")
data class Item(
    @PrimaryKey(autoGenerate=true)
    val _id : Int,
    val title: String?,
    val information: String?,
    val address: String?,
)
