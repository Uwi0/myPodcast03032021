package com.kakapo.podcap.service

import com.kakapo.podcap.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ItunesApiService {

    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(ItunesService::class.java)

    fun getPodcast(): Single<PodcastResponse.Podcast>{
        return api.getPodcastAPI(
            Constants.TERM
        )
    }
}