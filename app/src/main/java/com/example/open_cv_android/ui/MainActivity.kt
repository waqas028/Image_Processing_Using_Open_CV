package com.example.open_cv_android.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.open_cv_android.databinding.ActivityMainBinding
import com.example.open_cv_android.extension.bitmapToUri
import com.example.open_cv_android.extension.showToast
import com.example.open_cv_android.utils.Constant
import org.opencv.android.OpenCVLoader


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var imageUri: Uri? = null
    private var imageBitmap: Bitmap? = null
    companion object{
        const val TAG = "MainActivityInfo"
        const val PERMISSION_CODE = 1000
        const val CAMERA_REQUEST_CODE = 1
    }
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            imageUri = uri
            val intent = Intent(this, ImageProcessingActivity::class.java)
            intent.putExtra(Constant.IMAGE_URI_KEY,uri)
            startActivity(intent)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        if(OpenCVLoader.initDebug()){
            Log.i(TAG, "onCreate: init")
        }else{
            Log.i(TAG, "onCreate: not init")
        }

        binding.galleryButtonImageView.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.cameraButtonImageView.setOnClickListener{
            // Check if camera permission is granted
            if (hasCameraPermission) {
                // Camera permission is granted, start camera functionality
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                // Request camera permission
                requestPermission()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the image bitmap from the Intent.
            imageBitmap = data?.extras?.get("data") as? Bitmap

            val intent = Intent(this, ImageProcessingActivity::class.java)
            intent.putExtra(Constant.IMAGE_URI_KEY, imageBitmap?.let { bitmapToUri(it) })
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, CAMERA_REQUEST_CODE)
                } else{
                    //permission from popup was denied
                    showToast("Permission Denied")
                }
            }
        }
    }

    private fun requestPermission() {
        val permissions = arrayOf(Manifest.permission.CAMERA)
        requestPermissions(
            permissions,
            PERMISSION_CODE
        )
    }

}