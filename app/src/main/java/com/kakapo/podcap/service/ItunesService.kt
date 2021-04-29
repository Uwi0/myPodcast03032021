package com.kakapo.podcap.service

import com.kakapo.podcap.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {
    @GET(Constants.API_END_POINT)
    fun getPodcastAPI(
        @Query(Constants.TERM) term: String
    ): Single<PodcastResponse>
}