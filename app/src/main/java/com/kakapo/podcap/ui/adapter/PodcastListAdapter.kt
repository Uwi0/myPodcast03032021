package com.kakapo.podcap.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kakapo.podcap.databinding.SearchItemBinding
import com.kakapo.podcap.ui.viewmodel.SearchViewModel

class PodcastListAdapter(
    private var podcastSummaryViewList: List<SearchViewModel.PodcastSummaryViewData>?,
    private val podcastListAdapterListener: PodcastListAdapterListener,
    private val parentActivity: Activity
) : RecyclerView.Adapter<PodcastListAdapter.ViewHolder>() {

    interface PodcastListAdapterListener {
        fun onShowDetails(podcastSummaryViewData: SearchViewModel.PodcastSummaryViewData)
    }

    inner class ViewHolder(view: SearchItemBinding) : RecyclerView.ViewHolder(view.root){
        var podcastSummaryViewData: SearchViewModel.PodcastSummaryViewData? = null
        val nameTextView = view.podcastNameTextView
        val lastUpdateTextView = view.podcastLastUpdatedTextView
        val podcastImageView = view.podcastImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : SearchItemBinding = SearchItemBinding
            .inflate(LayoutInflater.from(parentActivity), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchViewList = podcastSummaryViewList ?: return
        val searchView = searchViewList[position]
        holder.podcastSummaryViewData = searchView
        holder.nameTextView.text = searchView.name
        holder.lastUpdateTextView.text = searchView.lastUpdated
        Glide.with(parentActivity)
            .load(searchView.imageUrl)
            .into(holder.podcastImageView)

        holder.itemView.setOnClickListener {
            holder.podcastSummaryViewData?.let {
                podcastListAdapterListener.onShowDetails(it)
            }
        }
    }

    override fun getItemCount(): Int {
        return podcastSummaryViewList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun searchData(podcastSummaryViewData: List<SearchViewModel.PodcastSummaryViewData>){
        podcastSummaryViewList = podcastSummaryViewData
        this.notifyDataSetChanged()

    }
}