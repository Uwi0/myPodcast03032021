package com.kakapo.podcap.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakapo.podcap.service.ItunesApiService
import com.kakapo.podcap.service.PodcastResponse
import com.kakapo.podcap.utils.DateUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Collections.emptyList

class SearchViewModel: ViewModel() {

    private val podcastApiService = ItunesApiService()
    private val compositeDisposable = CompositeDisposable()

    val loadPodcast = MutableLiveData<Boolean>()
    val podcastResponse = MutableLiveData<PodcastResponse.Podcast>()
    val podcastLoadingError = MutableLiveData<Boolean>()

    data class PodcastSummaryViewData(
        var name: String? = "",
        var lastUpdated: String? = "",
        var imageUrl: String? = "",
        var feedUrl: String? = ""
    )

    private fun getPodcastFromAPi(term: String, callback: (List<PodcastResponse.ItunesPodcast>?) -> Unit ){
        loadPodcast.value = true
        compositeDisposable.add(
            podcastApiService
                .getPodcast(term)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PodcastResponse.Podcast>(){
                    override fun onSuccess(value: PodcastResponse.Podcast?) {
                        loadPodcast.value = true
                        podcastResponse.value = value!!
                        podcastLoadingError.value = false
                        callback(value.results)
                    }

                    override fun onError(e: Throwable?) {
                        loadPodcast.value = false
                        podcastLoadingError.value = true
                        callback(null)
                        Log.e("Error Load From API", "Cannot Get Podcast", e)
                        e!!.printStackTrace()
                    }

                })
        )
    }

    private fun itunesPodcastToPodcastSummeryView(
        itunesPodcast: PodcastResponse.ItunesPodcast
    ): PodcastSummaryViewData{
        return PodcastSummaryViewData(
            itunesPodcast.collectionName,
            DateUtils.jsonDateToShortDate(itunesPodcast.releaseDate),
            itunesPodcast.artworkUrl30,
            itunesPodcast.feedUrl
        )
    }

    fun searchPodcastInAPI(term: String, callback: (List<PodcastSummaryViewData>) -> Unit){
        getPodcastFromAPi(term){result ->
            if (result == null){
                callback(emptyList())
            }else{
                val searchView = result as Sequence<*>
                val searchViewValue = searchView.map { results ->
                    itunesPodcastToPodcastSummeryView(results as PodcastResponse.ItunesPodcast)
                }

                callback(searchViewValue.toList())
            }
        }
    }
}