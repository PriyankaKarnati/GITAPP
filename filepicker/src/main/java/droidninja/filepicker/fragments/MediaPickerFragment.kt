package droidninja.filepicker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.PickerManager
import droidninja.filepicker.R
import droidninja.filepicker.adapters.SectionsPagerAdapter

class MediaPickerFragment : BaseFragment() {
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    private var mListener: MediaPickerFragmentListener? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media_picker, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is MediaPickerFragmentListener) {
            context
        } else {
            throw RuntimeException(context.toString()
                    + " must implement MediaPickerFragment")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface MediaPickerFragmentListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        tabLayout = view.findViewById(R.id.tabs)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        tabLayout!!.tabMode = TabLayout.MODE_FIXED
        val adapter = SectionsPagerAdapter(childFragmentManager)
        if (PickerManager.instance.showImages()) {
            if (PickerManager.instance.isShowFolderView) adapter.addFragment(MediaFolderPickerFragment.Companion.newInstance(FilePickerConst.MEDIA_TYPE_IMAGE), getString(R.string.images)) else adapter.addFragment(MediaDetailPickerFragment.Companion.newInstance(FilePickerConst.MEDIA_TYPE_IMAGE), getString(R.string.images))
        } else tabLayout!!.visibility = View.GONE
        if (PickerManager.instance.showVideo()) {
            if (PickerManager.instance.isShowFolderView) adapter.addFragment(MediaFolderPickerFragment.Companion.newInstance(FilePickerConst.MEDIA_TYPE_VIDEO), getString(R.string.videos)) else adapter.addFragment(MediaDetailPickerFragment.Companion.newInstance(FilePickerConst.MEDIA_TYPE_VIDEO), getString(R.string.videos))
        } else tabLayout!!.visibility = View.GONE
        viewPager!!.adapter = adapter
        tabLayout!!.setupWithViewPager(viewPager)
    }

    companion object {
        fun newinstance(): MediaPickerFragment {
            return MediaPickerFragment()
        }
    }
}