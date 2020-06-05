package droidninja.filepicker.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.PickerManager
import droidninja.filepicker.R
import droidninja.filepicker.adapters.FileAdapterListener
import droidninja.filepicker.adapters.FileListAdapter
import droidninja.filepicker.models.Document
import droidninja.filepicker.models.FileType

class DocFragment : BaseFragment(), FileAdapterListener {
    var recyclerView: RecyclerView? = null
    var emptyView: TextView? = null
    private var mListener: DocFragmentListener? = null
    private var selectAllItem: MenuItem? = null
    private var fileListAdapter: FileListAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_picker, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is DocFragmentListener) {
            context
        } else {
            throw RuntimeException(
                    "$context must implement PhotoPickerFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    val fileType: FileType
        get() = arguments!!.getParcelable(BaseFragment.Companion.FILE_TYPE)!!

    override fun onItemSelected() {
        mListener!!.onItemSelected()
        if (fileListAdapter != null && selectAllItem != null) {
            if (fileListAdapter!!.itemCount == fileListAdapter!!.selectedItemCount) {
                selectAllItem!!.setIcon(R.drawable.ic_select_all)
                selectAllItem!!.isChecked = true
            }
        }
    }

    interface DocFragmentListener {
        fun onItemSelected()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerview)
        emptyView = view.findViewById(R.id.empty_view)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        recyclerView!!.visibility = View.GONE
    }

    fun updateList(dirs: List<Document?>) {
        if (view == null) return
        if (dirs.size > 0) {
            recyclerView!!.visibility = View.VISIBLE
            emptyView!!.visibility = View.GONE
            fileListAdapter = recyclerView!!.adapter as FileListAdapter?
            if (fileListAdapter == null) {
                fileListAdapter = FileListAdapter(activity!!.applicationContext, dirs, PickerManager.instance.selectedFiles,
                        this)
                recyclerView!!.adapter = fileListAdapter
            } else {
                fileListAdapter!!.setData(dirs)
                fileListAdapter!!.notifyDataSetChanged()
            }
            onItemSelected()
        } else {
            recyclerView!!.visibility = View.GONE
            emptyView!!.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.doc_picker_menu, menu)
        selectAllItem = menu.findItem(R.id.action_select)
        if (PickerManager.instance.hasSelectAll()) {
            selectAllItem!!.setVisible(true)
            onItemSelected()
        } else {
            selectAllItem!!.setVisible(false)
        }
        val search = menu.findItem(R.id.search)
        val searchView: SearchView = search.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (fileListAdapter != null) {
                    fileListAdapter!!.filter.filter(newText)
                }
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        return if (itemId == R.id.action_select) {
            if (fileListAdapter != null) {
                if (selectAllItem != null) {
                    if (selectAllItem!!.isChecked) {
                        fileListAdapter!!.clearSelection()
                        PickerManager.instance.clearSelections()
                        selectAllItem!!.setIcon(R.drawable.ic_deselect_all)
                    } else {
                        fileListAdapter!!.selectAll()
                        PickerManager.instance
                                .add(fileListAdapter!!.selectedPaths, FilePickerConst.FILE_TYPE_DOCUMENT)
                        selectAllItem!!.setIcon(R.drawable.ic_select_all)
                    }
                }
                selectAllItem!!.isChecked = !selectAllItem!!.isChecked
                mListener!!.onItemSelected()
            }
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val TAG = DocFragment::class.java.simpleName
        fun newInstance(fileType: FileType?): DocFragment {
            val photoPickerFragment = DocFragment()
            val bun = Bundle()
            bun.putParcelable(BaseFragment.Companion.FILE_TYPE, fileType)
            photoPickerFragment.arguments = bun
            return photoPickerFragment
        }
    }
}