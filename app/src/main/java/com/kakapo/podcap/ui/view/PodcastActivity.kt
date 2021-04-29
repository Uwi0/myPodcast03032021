package com.kakapo.podcap.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.kakapo.podcap.databinding.ActivityPodcastBinding
import com.kakapo.podcap.ui.viewmodel.PodcastViewModel

class PodcastActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityPodcastBinding
    private lateinit var mPodcastViewModel: PodcastViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPodcastBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mPodcastViewModel = PodcastViewModel()
        podcastViewModelObserver()
        mPodcastViewModel.getPodcastFromAPi()
        mBinding.test.text = ("berhasil")

    }

    private fun podcastViewModelObserver(){
        mPodcastViewModel.podcastResponse.observe(this, {podcastResponse ->
            podcastResponse?.let {
                Log.i("PodcastInfo", "$podcastResponse")
            }
        })

        mPodcastViewModel.podcastLoadingError.observe(this, { dataError ->
            dataError?.let{
                Log.i("PodcastInfoError", "$dataError")
            }
        })

        mPodcastViewModel.loadPodcast.observe(this, {loadPodcast ->
            loadPodcast?.let {
                Log.i("PodcastInfoLoadData", "$loadPodcast")
            }
        })
    }

}