package com.kakapo.podcap.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakapo.podcap.service.ItunesApiService
import com.kakapo.podcap.service.PodcastResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class PodcastViewModel: ViewModel() {

    private val podcastApiService = ItunesApiService()
    private val compositeDisposable = CompositeDisposable()

    val loadPodcast = MutableLiveData<Boolean>()
    val podcastResponse = MutableLiveData<PodcastResponse.Podcast>()
    val podcastLoadingError = MutableLiveData<Boolean>()

    fun getPodcastFromAPi(){
        loadPodcast.value = true
        compositeDisposable.add(
            podcastApiService
                .getPodcast()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PodcastResponse.Podcast>(){
                    override fun onSuccess(value: PodcastResponse.Podcast?) {
                        loadPodcast.value = true
                        podcastResponse.value = value!!
                        podcastLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadPodcast.value = false
                        podcastLoadingError.value = true
                        Log.e("Error Load From API", "Cannot Get Podcast", e)
                        e!!.printStackTrace()
                    }

                })
        )
    }
}