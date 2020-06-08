package com.droidninja.imageeditengine

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter

class EditorViewPagerAdapter(val list: ArrayList<String>, fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return PhotoEditorFragment.newInstance(list[position])
    }
}