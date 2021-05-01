package com.kakapo.podcap.ui.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakapo.podcap.R
import com.kakapo.podcap.databinding.ActivityPodcastBinding
import com.kakapo.podcap.ui.adapter.PodcastListAdapter
import com.kakapo.podcap.ui.viewmodel.SearchViewModel

class PodcastActivity : AppCompatActivity(), PodcastListAdapter.PodcastListAdapterListener {

    private lateinit var mBinding: ActivityPodcastBinding
    private lateinit var mSearchViewModel: SearchViewModel
    private lateinit var podcastListAdapter: PodcastListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPodcastBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mSearchViewModel = SearchViewModel()

        setupToolbar()
        podcastViewModelObserver()
        updateControls()
        handleIntent(intent)
//        mPodcastViewModel.getPodcastFromAPi("Android developer")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

        val searchMenuItem = menu?.findItem(R.id.search_item)
        val searchView = searchMenuItem?.actionView as SearchView

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return super.onCreateOptionsMenu(menu)
    }


    override fun onShowDetails(podcastSummaryViewData: SearchViewModel.PodcastSummaryViewData) {
        TODO("Not yet implemented")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent!!)
    }

    private fun setupToolbar(){
        setSupportActionBar(mBinding.toolbar)
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

    private fun performSearch(term: String){
        showProgressBar()
        mSearchViewModel.searchPodcastInAPI(term){result ->
            hideProgressBar()
            mBinding.toolbar.title = resources.getString(R.string.search_results)
            podcastListAdapter.searchData(result)
        }
    }

    private fun handleIntent(intent: Intent){
        if(Intent.ACTION_SEARCH == intent.action){
            val query = intent.getStringExtra(SearchManager.QUERY)
            if (query != null){
                performSearch(query)
            }else{
                Log.e("error search data", "cannot find any searching")
            }
        }
    }

    private fun updateControls(){
        mBinding.podcastRecyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        mBinding.podcastRecyclerView.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(
            mBinding.podcastRecyclerView.context, layoutManager.orientation
        )
        podcastListAdapter = PodcastListAdapter(
            null,
            this,
            this
        )

        mBinding.podcastRecyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun showProgressBar(){
        mBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        mBinding.progressBar.visibility = View.GONE
    }


}