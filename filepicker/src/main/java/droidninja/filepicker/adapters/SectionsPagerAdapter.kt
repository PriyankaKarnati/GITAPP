package droidninja.filepicker.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class SectionsPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    private val mFragmentList: MutableList<Fragment> = ArrayList<Fragment>()
    private val mFragmentTitles: MutableList<String?> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    // Show 3 total pages.
//    private val count: Int  =// Show 3 total pages.
//            mFragmentList.size

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String?) {
        mFragmentList.add(fragment)
        mFragmentTitles.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitles[position]
    }
}