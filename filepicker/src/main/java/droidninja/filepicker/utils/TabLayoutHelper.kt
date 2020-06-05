/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package droidninja.filepicker.utils


import android.database.DataSetObserver
//import android.support.design.widget.TabLayout
//import android.support.v4.view.PagerAdapter
//import android.support.v4.view.ViewCompat
//import android.support.v4.view.ViewPager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import androidx.core.view.ViewCompat

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.NonNull
import java.lang.ref.WeakReference
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

open class TabLayoutHelper(@NonNull tabLayout: TabLayout?, @NonNull viewPager: ViewPager?) {
    protected var mTabLayout: TabLayout?
    protected var mViewPager: ViewPager?
    protected var mUserOnTabSelectedListener: TabLayout.OnTabSelectedListener? = null
    protected var mInternalOnTabSelectedListener: TabLayout.OnTabSelectedListener?
    protected var mInternalTabLayoutOnPageChangeListener: FixedTabLayoutOnPageChangeListener?
    protected var mInternalOnAdapterChangeListener: ViewPager.OnAdapterChangeListener?
    protected var mInternalDataSetObserver: DataSetObserver?
    protected var mAdjustTabModeRunnable: Runnable? = null
    protected var mSetTabsFromPagerAdapterRunnable: Runnable? = null
    protected var mUpdateScrollPositionRunnable: Runnable? = null
    protected var mAutoAdjustTabMode = false
    protected var mDuringSetTabsFromPagerAdapter = false
    //
    // public methods
    //
    /**
     * Retrieve underlying TabLayout instance.
     *
     * @return TabLayout instance
     */
    val tabLayout: TabLayout?
        get() = mTabLayout

    /**
     * Retrieve ViewPager instance.
     *
     * @return ViewPager instance
     */
    val viewPager: ViewPager?
        get() = mViewPager

    /**
     * Gets whether auto tab mode adjustment is enabled.
     *
     * @return True for enabled, otherwise false.
     */
    /**
     * Sets auto tab mode adjustment enabled
     *
     * @param enabled True for enabled, otherwise false.
     */
    var isAutoAdjustTabModeEnabled: Boolean
        get() = mAutoAdjustTabMode
        set(enabled) {
            if (mAutoAdjustTabMode == enabled) {
                return
            }
            mAutoAdjustTabMode = enabled
            if (mAutoAdjustTabMode) {
                adjustTabMode(-1)
            } else {
                cancelPendingAdjustTabMode()
            }
        }

    /**
     * Sets [TabLayout.OnTabSelectedListener]
     *
     * @param listener Listener
     */
    @Deprecated("Use {@link TabLayout#addOnTabSelectedListener(TabLayout.OnTabSelectedListener)} instead.")
    fun setOnTabSelectedListener(listener: TabLayout.OnTabSelectedListener?) {
        mUserOnTabSelectedListener = listener
    }

    /**
     * Unregister internal listener objects, release object references, etc.
     * This method should be called in order to avoid memory leaks.
     */
    fun release() {
        cancelPendingAdjustTabMode()
        cancelPendingSetTabsFromPagerAdapter()
        cancelPendingUpdateScrollPosition()
        if (mInternalOnAdapterChangeListener != null) {
            mViewPager?.removeOnAdapterChangeListener(mInternalOnAdapterChangeListener!!)
            mInternalOnAdapterChangeListener = null
        }
        if (mInternalDataSetObserver != null) {
            mViewPager?.adapter!!.unregisterDataSetObserver(mInternalDataSetObserver!!)
            mInternalDataSetObserver = null
        }
        if (mInternalOnTabSelectedListener != null) {
            mTabLayout?.removeOnTabSelectedListener(mInternalOnTabSelectedListener!!)
            mInternalOnTabSelectedListener = null
        }
        if (mInternalTabLayoutOnPageChangeListener != null) {
            mViewPager?.removeOnPageChangeListener(mInternalTabLayoutOnPageChangeListener!!)
            mInternalTabLayoutOnPageChangeListener = null
        }
        mUserOnTabSelectedListener = null
        mViewPager = null
        mTabLayout = null
    }

    fun updateAllTabs() {
        val count: Int = mTabLayout!!.tabCount
        for (i in 0 until count) {
            updateTab(mTabLayout!!.getTabAt(i)!!)
        }
    }

    /**
     * Override this method if you want to use custom tab layout.
     *
     * @param tabLayout TabLayout
     * @param adapter   PagerAdapter
     * @param position  Position of the item
     * @return TabLayout.Tab
     */
    protected fun onCreateTab(tabLayout: TabLayout, adapter: PagerAdapter, position: Int): TabLayout.Tab {
        val tab: TabLayout.Tab = tabLayout.newTab()
        tab.text = adapter.getPageTitle(position)
        return tab
    }

    /**
     * Override this method if you want to use custom tab layout
     *
     * @param tab Tab
     */
    protected fun onUpdateTab(tab: TabLayout.Tab) {
        if (tab.customView == null) {
            tab.customView = null // invokes update() method internally.
        }
    }

    //
    // internal methods
    //
    protected fun handleOnDataSetChanged() {
        cancelPendingUpdateScrollPosition()
        cancelPendingSetTabsFromPagerAdapter()
        if (mSetTabsFromPagerAdapterRunnable == null) {
            mSetTabsFromPagerAdapterRunnable = Runnable { setTabsFromPagerAdapter(mTabLayout, mViewPager?.adapter, mViewPager!!.getCurrentItem()) }
        }
        mTabLayout!!.post(mSetTabsFromPagerAdapterRunnable)
    }

    protected fun handleOnTabSelected(tab: TabLayout.Tab) {
        if (mDuringSetTabsFromPagerAdapter) {
            return
        }
        mViewPager?.currentItem = tab.position
        cancelPendingUpdateScrollPosition()
        if (mUserOnTabSelectedListener != null) {
            mUserOnTabSelectedListener!!.onTabSelected(tab)
        }
    }

    protected fun handleOnTabUnselected(tab: TabLayout.Tab?) {
        if (mDuringSetTabsFromPagerAdapter) {
            return
        }
        if (mUserOnTabSelectedListener != null) {
            mUserOnTabSelectedListener!!.onTabUnselected(tab)
        }
    }

    protected fun handleOnTabReselected(tab: TabLayout.Tab?) {
        if (mDuringSetTabsFromPagerAdapter) {
            return
        }
        if (mUserOnTabSelectedListener != null) {
            mUserOnTabSelectedListener!!.onTabReselected(tab)
        }
    }

    private fun handleOnAdapterChanged(viewPager: ViewPager, oldAdapter: PagerAdapter?, newAdapter: PagerAdapter?) {
        if (mViewPager !== viewPager) {
            return
        }
        oldAdapter?.unregisterDataSetObserver(mInternalDataSetObserver!!)
        newAdapter?.registerDataSetObserver(mInternalDataSetObserver!!)
        setTabsFromPagerAdapter(mTabLayout, newAdapter, mViewPager?.currentItem!!)
    }

    private fun cancelPendingAdjustTabMode() {
        if (mAdjustTabModeRunnable != null) {
            mTabLayout!!.removeCallbacks(mAdjustTabModeRunnable)
            mAdjustTabModeRunnable = null
        }
    }

    private fun cancelPendingSetTabsFromPagerAdapter() {
        if (mSetTabsFromPagerAdapterRunnable != null) {
            mTabLayout!!.removeCallbacks(mSetTabsFromPagerAdapterRunnable)
            mSetTabsFromPagerAdapterRunnable = null
        }
    }

    private fun cancelPendingUpdateScrollPosition() {
        if (mUpdateScrollPositionRunnable != null) {
            mTabLayout?.removeCallbacks(mUpdateScrollPositionRunnable)
            mUpdateScrollPositionRunnable = null
        }
    }

    private fun adjustTabMode(prevScrollX: Int) {
        var prevScrollX = prevScrollX
        if (mAdjustTabModeRunnable != null) {
            return
        }
        if (prevScrollX < 0) {
            prevScrollX = mTabLayout!!.scrollX
        }
        if (ViewCompat.isLaidOut(mTabLayout!!)) {
            adjustTabModeInternal(mTabLayout, prevScrollX)
        } else {
            val prevScrollX1 = prevScrollX
            mAdjustTabModeRunnable = Runnable {
                mAdjustTabModeRunnable = null
                adjustTabModeInternal(mTabLayout, prevScrollX1)
            }
            mTabLayout!!.post(mAdjustTabModeRunnable)
        }
    }

    private fun createNewTab(tabLayout: TabLayout, adapter: PagerAdapter, position: Int): TabLayout.Tab {
        return onCreateTab(tabLayout, adapter, position)
    }

    private fun setupWithViewPager(@NonNull tabLayout: TabLayout, @NonNull viewPager: ViewPager) {
        val adapter: PagerAdapter = viewPager.adapter
                ?: throw IllegalArgumentException("ViewPager does not have a PagerAdapter set")
        setTabsFromPagerAdapter(tabLayout, adapter, viewPager.currentItem)
        viewPager.adapter!!.registerDataSetObserver(mInternalDataSetObserver!!)
        viewPager.addOnPageChangeListener(mInternalTabLayoutOnPageChangeListener!!)
        viewPager.addOnAdapterChangeListener(mInternalOnAdapterChangeListener!!)
        tabLayout.addOnTabSelectedListener(mInternalOnTabSelectedListener!!)
    }

    private fun setTabsFromPagerAdapter(
            @NonNull tabLayout: TabLayout?,
            adapter: PagerAdapter?,
            currentItem: Int
    ) {
        var currentItems = currentItem
        try {
            mDuringSetTabsFromPagerAdapter = true
            val prevSelectedTab: Int = tabLayout!!.selectedTabPosition
            val prevScrollX: Int = tabLayout.scrollX

            // remove all tabs
            tabLayout.removeAllTabs()

            // add tabs
            if (adapter != null) {
                val count: Int = adapter.count
                for (i in 0 until count) {
                    val tab: TabLayout.Tab = createNewTab(tabLayout, adapter, i)
                    tabLayout.addTab(tab, false)
                    updateTab(tab)
                }

                // select current tab
                currentItems = Math.min(currentItem, count - 1)
                if (currentItem >= 0) {
                    tabLayout.getTabAt(currentItem)!!.select()
                }
            }

            // adjust tab mode & gravity
            if (mAutoAdjustTabMode) {
                adjustTabMode(prevScrollX)
            } else {
                // restore scroll position if needed
                val curTabMode: Int = tabLayout.tabMode
                if (curTabMode == TabLayout.MODE_SCROLLABLE) {
                    tabLayout.scrollTo(prevScrollX, 0)
                }
            }
        } finally {
            mDuringSetTabsFromPagerAdapter = false
        }
    }

    protected fun updateTab(tab: TabLayout.Tab) {
        onUpdateTab(tab)
    }

    private fun determineTabMode(@NonNull tabLayout: TabLayout?): Int {
        val slidingTabStrip = tabLayout!!.getChildAt(0) as LinearLayout
        val childCount = slidingTabStrip.childCount

        // NOTE: slidingTabStrip.getMeasuredWidth() method does not return correct width!
        // Need to measure each tabs and calculate the sum of them.
        val tabLayoutWidth: Int = tabLayout.measuredWidth - tabLayout.paddingLeft - tabLayout.paddingRight
        val tabLayoutHeight: Int = tabLayout.measuredHeight - tabLayout.paddingTop - tabLayout.paddingBottom
        if (childCount == 0) {
            return TabLayout.MODE_FIXED
        }
        var stripWidth = 0
        var maxWidthTab = 0
        val tabHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(tabLayoutHeight, View.MeasureSpec.EXACTLY)
        for (i in 0 until childCount) {
            val tabView = slidingTabStrip.getChildAt(i)
            tabView.measure(View.MeasureSpec.UNSPECIFIED, tabHeightMeasureSpec)
            val tabWidth = tabView.measuredWidth
            stripWidth += tabWidth
            maxWidthTab = Math.max(maxWidthTab, tabWidth)
        }
        return if (stripWidth < tabLayoutWidth && maxWidthTab < tabLayoutWidth / childCount) TabLayout.MODE_FIXED else TabLayout.MODE_SCROLLABLE
    }

    protected fun adjustTabModeInternal(@NonNull tabLayout: TabLayout?, prevScrollX: Int) {
        val prevTabMode: Int = tabLayout!!.tabMode
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.tabGravity = TabLayout.GRAVITY_CENTER
        val newTabMode = determineTabMode(tabLayout)
        cancelPendingUpdateScrollPosition()
        if (newTabMode == TabLayout.MODE_FIXED) {
            tabLayout.tabGravity = TabLayout.GRAVITY_FILL
            tabLayout.tabMode = TabLayout.MODE_FIXED
        } else {
            val slidingTabStrip = tabLayout.getChildAt(0) as LinearLayout
            slidingTabStrip.gravity = Gravity.CENTER_HORIZONTAL
            if (prevTabMode == TabLayout.MODE_SCROLLABLE) {
                // restore scroll position
                tabLayout.scrollTo(prevScrollX, 0)
            } else {
                // scroll to current selected tab
                mUpdateScrollPositionRunnable = Runnable {
                    mUpdateScrollPositionRunnable = null
                    updateScrollPosition()
                }
                mTabLayout!!.post(mUpdateScrollPositionRunnable)
            }
        }
    }

    private fun updateScrollPosition() {
        mTabLayout!!.setScrollPosition(mTabLayout!!.selectedTabPosition, 0F, false)
    }

    protected class FixedTabLayoutOnPageChangeListener(tabLayout: TabLayout) : ViewPager.OnPageChangeListener {
        private val mTabLayoutRef: WeakReference<TabLayout> = WeakReference<TabLayout>(tabLayout)
        private var mPreviousScrollState = 0
        private var mScrollState = 0
        override fun onPageScrollStateChanged(state: Int) {
            mPreviousScrollState = mScrollState
            mScrollState = state
        }

        override fun onPageScrolled(position: Int, positionOffset: Float,
                                    positionOffsetPixels: Int) {
            val tabLayout: TabLayout? = mTabLayoutRef.get()
            if (tabLayout != null) {
                if (shouldUpdateScrollPosition()) {
                    // Update the scroll position, only update the text selection if we're being
                    // dragged (or we're settling after a drag)
                    val updateText = (mScrollState == ViewPager.SCROLL_STATE_DRAGGING
                            || (mScrollState == ViewPager.SCROLL_STATE_SETTLING
                            && mPreviousScrollState == ViewPager.SCROLL_STATE_DRAGGING))
                    tabLayout.setScrollPosition(position, positionOffset, updateText)
                }
            }
        }

        override fun onPageSelected(position: Int) {
            val tabLayout: TabLayout? = mTabLayoutRef.get()
            if (tabLayout != null && tabLayout.selectedTabPosition !== position) {
                // Select the tab, only updating the indicator if we're not being dragged/settled
                // (since onPageScrolled will handle that).
                Internal.selectTab(tabLayout, tabLayout.getTabAt(position),
                        mScrollState == ViewPager.SCROLL_STATE_IDLE)
            }
        }

        private fun shouldUpdateScrollPosition(): Boolean {
            return mScrollState == ViewPager.SCROLL_STATE_DRAGGING ||
                    mScrollState == ViewPager.SCROLL_STATE_SETTLING && mPreviousScrollState == ViewPager.SCROLL_STATE_DRAGGING
        }

    }

    internal object Internal {
        private var mMethodSelectTab: Method? = null

        @Throws(RuntimeException::class)
        private fun getAccessiblePrivateMethod(targetClass: Class<*>, methodName: String, vararg params: Class<*>): Method {
            return try {
                val m = targetClass.getDeclaredMethod(methodName, *params)
                m.isAccessible = true
                m
            } catch (e: NoSuchMethodException) {
                throw IllegalStateException(e)
            }
        }

        fun selectTab(tabLayout: TabLayout?, tab: TabLayout.Tab?, updateIndicator: Boolean) {
            try {
                mMethodSelectTab!!.invoke(tabLayout, tab, updateIndicator)
            } catch (e: IllegalAccessException) {
                IllegalStateException(e)
            } catch (e: InvocationTargetException) {
                throw handleInvocationTargetException(e)
            }
        }

        private fun handleInvocationTargetException(e: InvocationTargetException): RuntimeException {
            val targetException = e.targetException
            if (targetException is RuntimeException) {
                throw targetException
            } else {
                throw IllegalStateException(targetException)
            }
        }

        init {
            mMethodSelectTab = getAccessiblePrivateMethod(TabLayout::class.java, "selectTab", TabLayout.Tab::class.java, Boolean::class.javaPrimitiveType!!)
        }
    }

    /**
     * Constructor.
     *
     * @param tabLayout TabLayout instance
     * @param viewPager ViewPager instance
     */
    init {
        val adapter: PagerAdapter = viewPager!!.adapter
                ?: throw IllegalArgumentException("ViewPager does not have a PagerAdapter set")
        mTabLayout = tabLayout
        mViewPager = viewPager
        mInternalDataSetObserver = object : DataSetObserver() {
            override fun onChanged() {
                handleOnDataSetChanged()
            }
        }
        mInternalOnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                handleOnTabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                handleOnTabUnselected(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                handleOnTabReselected(tab)
            }
        }
        mInternalTabLayoutOnPageChangeListener = FixedTabLayoutOnPageChangeListener(mTabLayout!!)
        mInternalOnAdapterChangeListener = ViewPager.OnAdapterChangeListener { viewPager, oldAdapter, newAdapter -> handleOnAdapterChanged(viewPager!!, oldAdapter, newAdapter) }
        setupWithViewPager(mTabLayout!!, mViewPager!!)
    }
}