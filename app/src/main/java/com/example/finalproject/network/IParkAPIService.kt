package com.example.finalproject.network

import com.example.finalproject.data.Root
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface IParkAPIService {
    @GET("openapi/API_CIA_082/request")
    @Headers("accept: application/json",
        "content-type: application/json"
    )
    fun getPlaceResult(
        @Query("serviceKey") serviceKey: String,
        @Query("keyword") keyword: String,

    ): Call<Root>

}