package com.kakapo.podcap.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakapo.podcap.databinding.ActivityPodcastBinding
import com.kakapo.podcap.ui.adapter.PodcastListAdapter
import com.kakapo.podcap.ui.viewmodel.SearchViewModel

class PodcastActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityPodcastBinding
    private lateinit var mSearchViewModel: SearchViewModel
    private lateinit var podcastListAdapter: PodcastListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPodcastBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mSearchViewModel = SearchViewModel()
        podcastViewModelObserver()
//        mPodcastViewModel.getPodcastFromAPi("Android developer")
        mBinding.test.text = ("berhasil")

    }

    private fun podcastViewModelObserver(){
        mSearchViewModel.podcastResponse.observe(this, { podcastResponse ->
            podcastResponse?.let {
                Log.i("PodcastInfo", "$podcastResponse")
            }
        })

        mSearchViewModel.podcastLoadingError.observe(this, { dataError ->
            dataError?.let{
                Log.i("PodcastInfoError", "$dataError")
            }
        })

        mSearchViewModel.loadPodcast.observe(this, { loadPodcast ->
            loadPodcast?.let {
                Log.i("PodcastInfoLoadData", "$loadPodcast")
            }
        })
    }

}