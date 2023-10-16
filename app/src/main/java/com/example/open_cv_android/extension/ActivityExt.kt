package com.example.open_cv_android.extension

import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import org.opencv.android.Utils
import org.opencv.core.Mat

fun Activity.uriToBitmap(uri: Uri): Bitmap? {
    val inputStream = contentResolver.openInputStream(uri) ?: return null
    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream.close()
    return bitmap
}
fun Activity.bitmapToUri(bitmap: Bitmap): Uri {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.TITLE, "OpenCV Image")
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    val outputStream = contentResolver.openOutputStream(uri!!)
    if (outputStream != null) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    }
    outputStream?.close()
    return uri
}

fun Activity.loadMatFromUri(uri: Uri): Mat {
    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
    val mat = Mat()
    Utils.bitmapToMat(bitmap, mat)
    return mat
}

fun Activity.showToast(message: String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}