package com.example.slides.myGallery

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.slides.R
import com.example.slides.database.MyGalDb
import com.example.slides.databinding.FragmentGalBinding
import com.example.slides.extGallery.ExtPhotoAdapter
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths
import java.lang.reflect.Field


class MyGalFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.M)
    private lateinit var viewModel: MyGalViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    private lateinit var adapter: ExtPhotoAdapter

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        if(arguments==null){
//
//            val viewS = inflater.inflate(R.layout.fragment_gal,container,false)
//            val toolbar = viewS.findViewById<Toolbar>(R.id.tool_bar_id2)
//            if (activity is AppCompatActivity) {
//                (activity as AppCompatActivity).setSupportActionBar(toolbar)
//            }
//
//        }
        val binding = FragmentGalBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val toolBar = binding.toolBarId2
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolBar)
        }

        val buttonID = binding.fab
        buttonID?.setOnClickListener {
            val wrapper: Context = ContextThemeWrapper(context, R.style.PopUpMenu)
            showPopupInGal(wrapper, it)

        }
        var selectedList: ImagesPaths?

        if (arguments != null)
            selectedList = MyGalFragmentArgs.fromBundle(requireArguments()).selectedImages
        else {
            selectedList = ImagesPaths()
        }


        val application = requireNotNull(this.activity).application

        val dataSource = MyGalDb.getInstance(application).myGalDao
        val viewModelFactory = MyGalViewModelFactory(dataSource, selectedList, application)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MyGalViewModel::class.java)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            adapter =
                ExtPhotoAdapter(ExtPhotoAdapter.OnClickListener { imagePath: ImagePath, view: View ->
                    viewModel.onImageClick(imagePath, view)
                    //                viewModel.initInsertToDb(dataSource,selectedList)
                    //                viewModel.initGetFromDb(dataSource)
                })
        }

        binding.intGalFrag.adapter = adapter

//        viewModel.getUpdateList.observe(viewLifecycleOwner, Observer {
//            Log.i("ExtGal","$it")
//            adapter.submitList(it)
//        })
        binding.myViewModel = viewModel


        return binding.root

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun showPopupInGal(wrapper: Context, view: View) {
        val popupMenu = PopupMenu(wrapper, view, Gravity.FILL, 0, R.style.PopUpMenu)
        popupMenu.menuInflater.inflate(R.menu.fragment_gal_fab_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {

            when (it!!.itemId) {
                R.id.clickImage -> true
                R.id.GoToGallery -> {
                    this.findNavController().navigate(R.id.action_myGalFragment_to_extGalFragment)
                }
                R.id.DeleteSelectedItems -> true
                R.id.DeleteDB -> true
                else -> false
            }
            true
        }
        val menuHelper: Any
        val argTypes: Array<Class<*>?>
        try {
            val fMenuHelper: Field = PopupMenu::class.java.getDeclaredField("mPopup")
            fMenuHelper.setAccessible(true)
            menuHelper = fMenuHelper.get(popupMenu)
            argTypes = arrayOf(Boolean::class.javaPrimitiveType)
            menuHelper.javaClass.getDeclaredMethod("setForceShowIcon", *argTypes)
                    .invoke(menuHelper, true)
        } catch (e: Exception) {
        }
        popupMenu.show()

//       val menuBuilder = MenuBuilder(this.requireContext())
//       val inflater = MenuInflater(this.requireContext())
//       inflater.inflate(R.menu.fragment_gal_fab_menu, menuBuilder)
//       val optionsMenu = MenuPopupHelper(wrapper, menuBuilder, view)
//       optionsMenu.setForceShowIcon(true)
//       menuBuilder.setCallback(object : MenuBuilder.Callback {
//           override fun onMenuItemSelected(menu: MenuBuilder?, item: MenuItem): Boolean {
//
//               return when (item.itemId) {
//                   R.id.clickImage -> true
//                   R.id.DeleteSelectedItems -> true
//                   R.id.DeleteDB->true
//                   else ->false
//               }
//           }
//
//           override fun onMenuModeChange(menu: MenuBuilder) {}
//       })
//
//       optionsMenu.show()
    }


}
