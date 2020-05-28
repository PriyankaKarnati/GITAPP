package com.example.slides


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlin.system.exitProcess


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

}


