package com.example.slides.myGallery

import android.annotation.SuppressLint
import android.content.Context
import android.icu.util.ValueIterator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import com.example.slides.R
import com.example.slides.database.MyGalDao
import com.example.slides.database.MyGalDb
import com.example.slides.databinding.FragmentMyGalBinding
import com.example.slides.deviceGallery.DevicePhotoAdapter
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths
import java.lang.reflect.Field


class MyGalFragment : Fragment() {
    private lateinit var tracker: SelectionTracker<ImagePath>
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

//        if(arguments==null){
//
//            val viewS = inflater.inflate(R.layout.fragment_my_gal,container,false)
//            val toolbar = viewS.findViewById<Toolbar>(R.id.tool_bar_id2)
//            if (activity is AppCompatActivity) {
//                (activity as AppCompatActivity).setSupportActionBar(toolbar)
//            }
//
//        }
        val binding = FragmentMyGalBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val toolBar = binding.toolBarId2
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolBar)
        }

        val buttonID = binding.fab
        buttonID.setOnClickListener {
            this.findNavController().navigate(R.id.action_myGalFragment_to_deviceGalFragment)
        }
        val selectedList: ImagesPaths?

        if (arguments != null)
            selectedList = MyGalFragmentArgs.fromBundle(requireArguments()).selectedImagesInGal
        else {
            selectedList = ImagesPaths()
        }


        val application = requireNotNull(this.activity).application

        dataSource = MyGalDb.getInstance(application).myGalDao
        val viewModelFactory = MyGalViewModelFactory(dataSource, selectedList, application)

        viewModel =
                ViewModelProviders.of(this, viewModelFactory).get(MyGalViewModel::class.java)




            adapter =
                    MyGalPhotoAdapter()
        binding.intGalFrag.adapter = adapter


        viewModel.getUpdateList.observe(viewLifecycleOwner, Observer {
            // Log.i("ExtGal","$it")
            adapter.submitList(it)
        })
        binding.myViewModel = viewModel
        if (savedInstanceState != null) {
            tracker.onRestoreInstanceState(savedInstanceState)
        }
//  val x =ImagePath()
        tracker = SelectionTracker.Builder(
                "mySelection",
                binding.intGalFrag,
                MyGalPhotoAdapter.MyItemKeyProvider(adapter),
                MyGalPhotoAdapter.MyItemDetailsLookup(binding.intGalFrag),
                StorageStrategy.createParcelableStorage(ImagePath::class.java)
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build()
        adapter.tracker = tracker


        tracker?.addObserver(
                object : SelectionTracker.SelectionObserver<ImagePath>() {
                    override fun onSelectionChanged() {
                        super.onSelectionChanged()
                        val items = tracker.selection!!.size()
                        val buttonDel = view?.findViewById<Button>(R.id.deleteSelectedItemsButton)
                        val checkBox = view?.findViewById<CheckBox>(R.id.checkBoxinMyGal)
                        val selectedItemsText = view?.findViewById<TextView>(R.id.selectedImagesInMyGal)

                        if (items > 0) {
                            buttonDel?.visibility = View.VISIBLE
                            checkBox?.visibility = View.VISIBLE

                            checkBox?.setOnClickListener {
                                if (checkBox.isChecked) {
                                    tracker.setItemsSelected(viewModel.getUpdateList.value!!, true)
                                } else {
                                    tracker.clearSelection()
                                }
                            }
                            buttonDel?.setOnClickListener {

                                viewModel.deleteSelected(dataSource, tracker)

                            }
                            selectedItemsText?.text = "${tracker.selection.size()}/${viewModel.getUpdateList.value!!.size}"
                        } else {
                            buttonDel?.visibility = View.GONE
                            checkBox?.visibility = View.GONE
                            selectedItemsText?.text = ""

                        }

                    }

                })
        //    val checkBoxInMYGal = binding.checkBoxinMyGal

//        viewModel.set.observe(viewLifecycleOwner, Observer {
//            adapter.getAllVisible(it)
//            checkBoxInMYGal.setOnClickListener {
//
//                if (checkBoxInMYGal.isChecked) {
//                    adapter.checkSelectedAll = true
//                    adapter.notifyDataSetChanged()
//                } else {
//                    adapter.checkSelectedAll = false
//                    adapter.notifyDataSetChanged()
//                }
//            }
//        })
//        if(checkBoxInMYGal.visibility==View.VISIBLE){
//            adapter.checkVisibleAll=true
//            adapter.notifyDataSetChanged()
//        }









        return binding.root

    }

//
//    @SuppressLint("NewApi")
//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
//    fun showPopupInGal(wrapper: Context, view: View) {
//        val popupMenu = PopupMenu(wrapper, view, Gravity.FILL, 0, R.style.PopUpMenu)
//        popupMenu.menuInflater.inflate(R.menu.fragment_gal_fab_menu, popupMenu.menu)
//        popupMenu.setOnMenuItemClickListener {
//
//            when (it!!.itemId) {
//                R.id.GoToGallery -> {
//                    this.findNavController().navigate(R.id.action_myGalFragment3_to_deviceGalFragment)
//                }
//                R.id.DeleteSelectedItems -> viewModel.deleteSelected(dataSource//)
//                R.id.DeleteDB -> viewModel.deleteAll(dataSource)
//            }
//            true
//        }
//        val menuHelper: Any?
//        val argTypes: Array<Class<*>?>
//        try {
//            val fMenuHelper: Field = PopupMenu::class.java.getDeclaredField("mPopup")
//            fMenuHelper.isAccessible = true
//            menuHelper = fMenuHelper.get(popupMenu)
//            argTypes = arrayOf(Boolean::class.javaPrimitiveType)
//            menuHelper.javaClass.getDeclaredMethod("setForceShowIcon", *argTypes)
//                    .invoke(menuHelper, true)
//        } catch (e: Exception) {
//        }
//        popupMenu.show()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker.onSaveInstanceState(outState)
    }

}
