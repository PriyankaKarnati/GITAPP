package com.example.slides


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mains)
//        val permission= ActivityCompat.requestPermissions(this, new String[]{"Manifest.permission.READ_PHONE_STATE"}, 225);
//        if(permission== PackageManager.PERMISSION_GRANTED) {
//            print("YES")
//        }
//        else{
//            return
//        }
        setPermissions()
//        val list:ArrayList<String> =loadImagesFromInternalStorage()
//        var intent = Intent(this, ExtGalFragment::class.java)
//        intent.putStringArrayListExtra("image_url_data",list)
//        startActivity(intent)
//        finish()


    }

    private fun setPermissions() {
        val permission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        Log.i("Callllllllllled", "callllllllled")
        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                22
            );


        }


    }


}


