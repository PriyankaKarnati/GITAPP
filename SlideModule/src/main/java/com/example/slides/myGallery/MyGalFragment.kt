package com.example.slides.myGallery

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy

import com.example.slides.R
import com.example.slides.database.MyGalDao
import com.example.slides.database.MyGalDb
import com.example.slides.databinding.FragmentMyGalBinding
import com.example.slides.deviceGallery.DeviceGalFragment
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths


class MyGalFragment : Fragment() {
    private var tracker: SelectionTracker<ImagePath>? = null

    @RequiresApi(Build.VERSION_CODES.M)
    private lateinit var viewModel: MyGalViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    private lateinit var adapter: MyGalPhotoAdapter
    private lateinit var dataSource: MyGalDao

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMyGalBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val toolBar = binding.toolBarId2
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolBar)//display custom toolbar as action bar
        }


        val selectedList: ImagesPaths? = if (arguments != null) {
            // MyGalFragmentArgs.fromBundle(requireArguments()).selectedImagesInGal// if any list from DeviceGalFragment
            requireArguments().getParcelable("DataFromImageEditor")


        } else {
            ImagesPaths()
        }
        val buttonID = binding.fab
        buttonID.setOnClickListener {
            //this.findNavController().navigate(R.id.action_myGalFragment_to_deviceGalFragment)
            //button for navigating to DeviceGalFragment for fetching selected list
            val fragDeviceGal = DeviceGalFragment()
            val transaction = this.parentFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragmentSlide, fragDeviceGal)
            transaction.commit()
        }
        Log.i("reQuireArgs", "${requireArguments().getParcelable<ImagesPaths>("DataFromImageEditor")}")

        val application = requireNotNull(this.activity).application//for context in viewModel
        dataSource = MyGalDb.getInstance(application).myGalDao// Dao

        val viewModelFactory = MyGalViewModelFactory(dataSource, selectedList, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MyGalViewModel::class.java)
        binding.myViewModel = viewModel
        adapter = MyGalPhotoAdapter(onClickListener = MyGalPhotoAdapter.OnClickListener {
            this.findNavController().navigate(MyGalFragmentDirections.actionMyGalFragmentToGalViewPager(it))
        })
        binding.intGalFrag.adapter = adapter


        viewModel.getUpdateList.observe(viewLifecycleOwner, Observer {

            adapter.submitList(it)
            if (savedInstanceState != null) {
                //on resume get saved state

                tracker?.onRestoreInstanceState(savedInstanceState)
            }

        })


        tracker = SelectionTracker.Builder(
                "mySelection",//to identify our selection in the context
                binding.intGalFrag,//recycler view where selection is to be applied
                MyGalPhotoAdapter.MyItemKeyProvider(adapter),//source of selection keys
                MyGalPhotoAdapter.MyItemDetailsLookup(binding.intGalFrag),//source of information about selected keys
                StorageStrategy.createParcelableStorage(ImagePath::class.java)//strategy for type safe storage of selected state
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()//allows to select multiple items
        ).build()

        adapter.tracker = tracker



        tracker!!.addObserver(//after selecting ,observe these selections to perform any function
                object : SelectionTracker.SelectionObserver<ImagePath>() {
                    override fun onSelectionChanged() {
                        super.onSelectionChanged()
                        val items = tracker!!.selection!!.size()
                        val checkBox = view?.findViewById<CheckBox>(R.id.checkBoxinMyGal)
                        val selectedItemsText = view?.findViewById<TextView>(R.id.selectedImagesInMyGal)

                        if (items > 0) {//if any item is selected
                            viewModel.setTracker(true)
                            checkBox?.setOnClickListener {//if checkbox is checked , select all items or deselect if unchecked
                                if (checkBox.isChecked) {
                                    // Log.i("TrackerSelectedItems","${viewModel.getUpdateList.val}")
                                    var countii = 0
                                    for (i in viewModel.getUpdateList.value!!) {

                                        if (i == null) {
                                            Log.i("TrackerSelectionState", "$countii")
                                        } else tracker!!.select(i)
                                        countii++
                                    }
                                    //tracker!!.setItemsSelected(viewModel.getUpdateList.value!!.snapshot(), true)
                                } else {
                                    tracker!!.clearSelection()
                                }
                            }

                            checkBox!!.isChecked = tracker!!.selection!!.size() == viewModel.getUpdateList.value!!.size
                            selectedItemsText?.text = "${tracker!!.selection.size()}/${viewModel.getUpdateList.value!!.size}"//display number of
                            //items selected in toolbar
                        } else {
                            //if no items are selected
                            viewModel.setTracker(false)
                        }
                    }
                })

        val buttonDel = binding.deleteSelectedItemsButton
        buttonDel.setOnClickListener {
            viewModel.deleteSelected(dataSource, tracker!!)
        }


        return binding.root

    }


    override fun onSaveInstanceState(outState: Bundle) {
        //on configuration change save state
        super.onSaveInstanceState(outState)
        tracker?.onSaveInstanceState(outState)
    }


}
