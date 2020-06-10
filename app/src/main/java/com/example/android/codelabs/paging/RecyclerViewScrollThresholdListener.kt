package com.example.android.codelabs.paging

import android.util.Log
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewScrollThresholdListener(recyclerView: RecyclerView) {

    companion object{
        val TAG: String = "'${RecyclerViewScrollThresholdListener}'"
    }

    private val listeners: MutableList<(eventArgs: RecyclerViewScrolledEvent)-> Unit> = mutableListOf()

    init {
        val layoutManager = recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                notifyListeners(RecyclerViewScrolledEvent(totalItemCount, visibleItemCount, lastVisibleItem))
            }
        })
    }

    fun addOnScrollListener(listener: (eventArgs: RecyclerViewScrolledEvent)-> Unit){
        listeners.add(listener)
    }

    fun addOnThresholdListener(threshold: Int, listener: ()-> Unit){
        listeners.add{
            if(it.visibleItemCount + it.lastVisibleItemPosition + threshold >= it.totalItemCount){
                Log.d(TAG, "threshold reached")
                listener()
            }
        }
    }

    private fun notifyListeners(eventArgs: RecyclerViewScrolledEvent) {
        listeners.forEach {
            it(eventArgs)
        }
    }
}

data class RecyclerViewScrolledEvent(
        val totalItemCount: Int,
        val visibleItemCount: Int,
        val lastVisibleItemPosition: Int
)