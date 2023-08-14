package com.omar.orangetask.api

import com.omar.orangetask.models.NewsResponse
import com.omar.orangetask.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String ,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("sortBy") // TO sort it by time "Results should be ordered by date"
        sortBy:String="publishedAt",
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>
}