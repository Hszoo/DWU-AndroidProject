package com.example.finalproject.network

import com.example.finalproject.data.Place
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface IParkAPIService {
    @GET("openapi/API_CIA_082/request")
    fun getPlaceResult(
        @Query("serviceKey") serviceKey: String,
        @Query("keyword") keyword: String,
        @Header("accept") acceptHeader: String
    ): Call<Place>
}