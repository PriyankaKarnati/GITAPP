package com.example.slides.myGallery

//import androidx.recyclerView!!.widget.GridLayoutManager
//import androidx.recyclerView!!.widget.LinearLayoutManager
//import androidx.recyclerView!!.widget.RecyclerView
//
//
//abstract class PaginationScrollListener(var layoutManager: GridLayoutManager) : recyclerView!!.OnScrollListener() {
//    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//        super.onScrolled(recyclerView, dx, dy)
//        val visibleItemCount = layoutManager.childCount
//        val totalItemCount = layoutManager.itemCount
//        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//        if (!isLoading() && !isLastPage()) {
//            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
//                    && firstVisibleItemPosition >= 0) {
//                loadMoreItems()
//            }
//        }
//    }
//
//    protected abstract fun loadMoreItems()
//
//    abstract fun isLastPage(): Boolean
//
//    abstract fun isLoading(): Boolean
//
//}