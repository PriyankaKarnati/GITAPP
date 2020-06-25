package com.example.slides


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import com.droidninja.imageeditengine.ImageEditor
import com.example.slides.models.ImagePath
import com.example.slides.models.ImagesPaths
import com.example.slides.myGallery.MyGalFragment
import kotlinx.android.synthetic.main.activity_mains.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mains)
        setPermissions()//ask permission to read storage
    }

    private fun setPermissions() {
        val permission =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)//check if permission is already granted
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    22)//if not granted request it
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            22 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("continue", "granted Permission")
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    finish()
                }
                return
            }
        }
    }

    //    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        for (fragment in supportFragmentManager.fragments) {
//            fragment.onActivityResult(requestCode, resultCode, data)
//        }
//    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        for(frag)
//        fragmentsuper.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ImageEditor.RC_IMAGE_EDITOR ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val imagePath = data.getStringArrayListExtra(ImageEditor.EXTRA_EDITED_PATH)

                    var listToSend = ImagesPaths()
                    for (i in imagePath) {
                        val x = ImagePath(i, System.currentTimeMillis())
                        listToSend.add(x)
                        Log.i("Edited Imagessssss", "$imagePath")
                    }

                    //this.startActivityForResult(data,requestCode)
                    //this@De.findNavController().navigate(DeviceGalFragmentDirections.actionDeviceGalFragmentToMyGalFragment().setSelectedImagesInGal(listToSend))
                    sendToMyGal(listToSend)
                }
        }

    }

    private fun sendToMyGal(paths: ImagesPaths) {
        val bundle = Bundle()
        bundle.putParcelable("DataFromImageEditor", paths)

        val navController = findNavController(R.id.nav_host_fragmentSlide)
        navController.setGraph(navController.graph, bundle)
//        val fragMyGal = MyGalFragment()
//        fragMyGal.arguments = bundle
//        Log.i("sendList", paths.size.toString())
//        val transaction = this.supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.nav_host_fragmentSlide, fragMyGal)
//        transaction.commit()
    }


}


