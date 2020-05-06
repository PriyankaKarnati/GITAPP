package com.example.gitapp.OLD.network.repository.paging

import androidx.paging.PagedList
import com.example.gitapp.models.GitProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

//
//class PostBoundaryCallBack(
//        val since: Int,
//        val coroutineContext: CoroutineContext,
//        val handleResponse: (Int, List<GitProperty>) -> Unit,
//        val onZeroLoad: suspend (Int) -> List<GitProperty>?,
//        val onLoadMore: suspend (Int, Int) -> List<GitProperty>?
//) : PagedList.BoundaryCallback<GitProperty>() {
//
//    // val networkState: MutableLiveData<Thread.State> = MutableLiveData()
//
//    override fun onZeroItemsLoaded() {
//        CoroutineScope(this.coroutineContext).launch {
//            val newProps: List<GitProperty>? = this@PostBoundaryCallBack.onZeroLoad(
//                    since
//            )
//            if (newProps!!.isEmpty()) {
//                return@launch
//            }
//            if (newProps == null) {
//                return@launch
//            }
//            this@PostBoundaryCallBack.handleResponse(since, newProps)
//        }
//
//    }
//
//    override fun onItemAtEndLoaded(itemAtEnd: GitProperty) {
//        CoroutineScope(this.coroutineContext).launch {
//            val moreProps: List<GitProperty>? = this@PostBoundaryCallBack.onLoadMore(
//                    since,
//                    itemAtEnd.id
//            )
//            if (moreProps!!.isEmpty()) {
//                return@launch
//            }
//            if (moreProps == null) {
//                return@launch
//            }
//            this@PostBoundaryCallBack.handleResponse(since, moreProps)
//        }
//    }
//}