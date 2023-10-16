package com.example.open_cv_android.ui

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.open_cv_android.adapter.MenuAdapter
import com.example.open_cv_android.databinding.ActivityImageProcessingBinding
import com.example.open_cv_android.databinding.BottomSheetDialogBinding
import com.example.open_cv_android.extension.loadMatFromUri
import com.example.open_cv_android.extension.showToast
import com.example.open_cv_android.extension.uriToBitmap
import com.example.open_cv_android.utils.Constant
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Core.LUT
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc


class ImageProcessingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageProcessingBinding
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var finalBitmapImage: Bitmap
    private val buttonMenuList = listOf("Bluer","RGB","Adjust Brightness","Resize","GrayScale","Filter")
    private lateinit var imageUri: Uri
    private var blurStrength = 0f
    private var brightnessValue = 0.0
    private var contrastFactor = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageProcessingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageUri = intent.extras?.get(Constant.IMAGE_URI_KEY) as Uri
        binding.imageView.setImageURI(imageUri)

        if(OpenCVLoader.initDebug()){
            Log.i(MainActivity.TAG, "onCreate: init")
        }else{
            Log.i(MainActivity.TAG, "onCreate: not init")
        }
        //setting rv
        settingRecycleView()
        filterButtonClickListener()

        menuAdapter.setOnItemClickListener {
            Log.i(TAG, "onCreate: $it")
            imageProcessing(it)
        }

        binding.saveImageButton.setOnClickListener {
            saveImageToGallery(finalBitmapImage)
        }
        binding.shareImageButton.setOnClickListener {
            shareImage(finalBitmapImage)
        }
    }

    private fun settingRecycleView() {
        menuAdapter = MenuAdapter()
        binding.imageProcessingRecycleView.apply {
            adapter = menuAdapter
            layoutManager = LinearLayoutManager(this@ImageProcessingActivity).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        }
        menuAdapter.differ.submitList(buttonMenuList)
    }

    private fun imageProcessing(source: String){
        val imageBitmap = uriToBitmap(imageUri)
        val mat = Mat(imageBitmap!!.width, imageBitmap.height, CvType.CV_8UC3)
        Utils.bitmapToMat(imageBitmap, mat)
        when(source){
            MenuButton.GRAYSCALE.label -> {
                binding.seekBar.visibility = View.GONE
                binding.filterConstraintLayout.visibility = View.GONE
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY)
                Utils.matToBitmap(mat, imageBitmap)
                finalBitmapImage = imageBitmap
                binding.imageView.setImageBitmap(finalBitmapImage)
            }
            MenuButton.BLUER.label -> {
                binding.filterConstraintLayout.visibility = View.GONE
                seekbarChange(source)
                Imgproc.GaussianBlur(mat, mat, Size(25.0, 25.0), blurStrength.toDouble())
                finalBitmapImage = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888)
                Utils.matToBitmap(mat, finalBitmapImage)
                binding.imageView.setImageBitmap(finalBitmapImage)
            }
            MenuButton.RGB.label -> {
                binding.seekBar.visibility = View.GONE
                binding.filterConstraintLayout.visibility = View.GONE
                val rgbMat = Mat(mat.width(), mat.height(), CvType.CV_8UC3)
                Imgproc.cvtColor(mat, rgbMat, Imgproc.COLOR_BGR2RGB)
                // Convert the RGB Mat object back to an image.
                finalBitmapImage = Bitmap.createBitmap(rgbMat.width(), rgbMat.height(), Bitmap.Config.ARGB_8888)
                Utils.matToBitmap(rgbMat, finalBitmapImage)
                binding.imageView.setImageBitmap(finalBitmapImage)
            }
            MenuButton.RESIZE.label -> showBottomSheet()
            MenuButton.ADJUST_BRIGHTNESS.label -> {
                binding.filterConstraintLayout.visibility = View.GONE
                seekbarChange(source)
                mat.convertTo(mat, -1, 1.0, brightnessValue)
                finalBitmapImage = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
                Utils.matToBitmap(mat, finalBitmapImage)
                binding.imageView.setImageBitmap(finalBitmapImage)
            }
            MenuButton.FILTER.label ->{
                binding.seekBar.visibility = View.GONE
                binding.filterConstraintLayout.isVisible = !binding.filterConstraintLayout.isVisible
            }
        }
    }

    private fun seekbarChange(source: String) {
        binding.seekBar.visibility = View.VISIBLE
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                blurStrength = progress / 10f
                brightnessValue = progress.toDouble()
                contrastFactor = progress.toFloat() / 100.0f
                Log.i(TAG, "onProgressChanged: $progress  //  ${(progress/10f).toDouble()}")
                when(source){
                    MenuButton.GRAYSCALE.label -> imageProcessing(source)
                    MenuButton.BLUER.label -> imageProcessing(source)
                    MenuButton.RGB.label -> imageProcessing(source)
                    MenuButton.RESIZE.label -> imageProcessing(source)
                    MenuButton.ADJUST_BRIGHTNESS.label -> imageProcessing(source)
                    MenuButton.SHARP.label -> imageProcessing(source)
                    MenuButton.SATURATION.label -> imageProcessing(source)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun showBottomSheet(){
        binding.filterConstraintLayout.visibility = View.GONE
        binding.seekBar.visibility = View.GONE
        val dialog = BottomSheetDialog(this)
        val binding = BottomSheetDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        binding.BtnDismiss.setOnClickListener {
            dialog.dismiss()
        }
        binding.BtnResize.setOnClickListener{
            val newHeight = binding.imgHeight.text.toString()
            val newWidth = binding.imgWidth.text.toString()
            if(newHeight.isNotEmpty() && newWidth.isNotEmpty()) reSizing(newHeight.toInt(),newWidth.toInt())
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun reSizing(newHeight: Int, newWidth: Int) {
        val originalMat = loadMatFromUri(imageUri)
        val resizedMat = Mat()
        val newSize = Size(newWidth.toDouble(), newHeight.toDouble())
        Imgproc.resize(originalMat, resizedMat, newSize)
        // Convert the resized Mat back to a bitmap
        finalBitmapImage = Bitmap.createBitmap(resizedMat.width(), resizedMat.height(), Bitmap.Config.RGB_565)
        Utils.matToBitmap(resizedMat, finalBitmapImage)
        // Update the ImageView with the resized image
        binding.imageView.setImageBitmap(finalBitmapImage)
    }

    private fun filterButtonClickListener() {
        val imageBitmap = uriToBitmap(imageUri)
        val mat = Mat(imageBitmap!!.width, imageBitmap.height, CvType.CV_8UC3)
        Bitmap.createBitmap(imageBitmap.width, imageBitmap.height, Bitmap.Config.RGB_565)
        Utils.bitmapToMat(imageBitmap, mat)
        binding.cartoonFilterButton.setOnClickListener{
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2BGR)
            val result = cartoon(mat)
            finalBitmapImage = Bitmap.createBitmap(result.cols(), result.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(result, finalBitmapImage)
            binding.imageView.setImageBitmap(finalBitmapImage)
        }
        binding.grayFilterButton.setOnClickListener{
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY)
            val lut = createLUT(5)
            LUT(mat, lut, mat)
            finalBitmapImage = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(mat, finalBitmapImage)
            binding.imageView.setImageBitmap(finalBitmapImage)
        }

        binding.thresholdingFilterButton.setOnClickListener{
            adaptiveThreshold()
        }

        binding.houghButton.setOnClickListener{
            val result = reduceColors(mat)
            finalBitmapImage = Bitmap.createBitmap(result.cols(), result.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(result, finalBitmapImage)
            binding.imageView.setImageBitmap(finalBitmapImage)
        }

        binding.medianFilterButton.setOnClickListener {
            medianFilter()
        }
    }

    private fun medianFilter() {
        val options = BitmapFactory.Options()
        options.inScaled = false // Leaving it to true enlarges the decoded image size.
        val original = uriToBitmap(imageUri)
        val img1 = Mat()
        Utils.bitmapToMat(original, img1)
        val medianFilter = Mat()
        Imgproc.cvtColor(img1, medianFilter, Imgproc.COLOR_BGR2GRAY)
        Imgproc.medianBlur(medianFilter, medianFilter, 15)
        finalBitmapImage = Bitmap.createBitmap(medianFilter.cols(), medianFilter.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(medianFilter, finalBitmapImage)
        binding.imageView.setImageBitmap(finalBitmapImage)
    }

    private fun adaptiveThreshold() {
        val options = BitmapFactory.Options()
        options.inScaled = false // Leaving it to true enlarges the decoded image size.
        val original = uriToBitmap(imageUri)
        val adaptiveTh = Mat()
        Utils.bitmapToMat(original, adaptiveTh)
        Imgproc.cvtColor(adaptiveTh, adaptiveTh, Imgproc.COLOR_BGR2GRAY)
        Imgproc.medianBlur(adaptiveTh, adaptiveTh, 15)
        Imgproc.adaptiveThreshold(
            adaptiveTh,
            adaptiveTh,
            255.0,
            Imgproc.ADAPTIVE_THRESH_MEAN_C,
            Imgproc.THRESH_BINARY,
            9,
            2.0
        )
        finalBitmapImage = Bitmap.createBitmap(adaptiveTh.cols(), adaptiveTh.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(adaptiveTh, finalBitmapImage)
        binding.imageView.setImageBitmap(finalBitmapImage)
    }

    private fun cartoon(img: Mat): Mat {
        val reducedColorImage = reduceColors(img)
        val result = Mat()
        Imgproc.cvtColor(img, result, Imgproc.COLOR_BGR2GRAY)
        Imgproc.medianBlur(result, result, 15)
        Imgproc.adaptiveThreshold(
            result,
            result,
            255.0,
            Imgproc.ADAPTIVE_THRESH_MEAN_C,
            Imgproc.THRESH_BINARY,
            15,
            2.0
        )
        Imgproc.cvtColor(result, result, Imgproc.COLOR_GRAY2BGR)
        Core.bitwise_and(reducedColorImage, result, result)
        return result
    }

    private fun reduceColors(img: Mat): Mat {
        try{
            val redLUT = createLUT(80)
            val greenLUT = createLUT(15)
            val blueLUT = createLUT(10)
            val bgr: List<Mat> = java.util.ArrayList(3)
            Core.split(img, bgr) // splits the image into its channels in the List of Mat arrays.
            LUT(bgr[0], blueLUT, bgr[0])
            LUT(bgr[1], greenLUT, bgr[1])
            LUT(bgr[2], redLUT, bgr[2])
            Core.merge(bgr, img)
        }catch (e:Exception){
            Log.i(TAG, "reduceColors: ")
        }
        return img
    }

    private fun createLUT(numColors: Int): Mat? {
        // When numColors=1 the LUT will only have 1 color which is black.
        if (numColors < 0 || numColors > 256) {
            println("Invalid Number of Colors. It must be between 0 and 256 inclusive.")
            return null
        }
        val lookupTable = Mat.zeros(Size(1.0, 256.0), CvType.CV_8UC1)
        var startIdx = 0
        var x = 0
        while (x < 256) {
            lookupTable.put(x, 0, x.toDouble())
            for (y in startIdx until x) {
                if (lookupTable[y, 0][0] == 0.0) {
                    lookupTable.put(y, 0, *lookupTable[x, 0])
                }
            }
            startIdx = x
            x += (256.0 / numColors).toInt()
        }
        return lookupTable
    }

    private fun saveImageToGallery(imageBitmap: Bitmap) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "OpenCV Image")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val outputStream = contentResolver.openOutputStream(uri!!)
        outputStream?.let { imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        outputStream?.close()
        showToast("Image Saved Successfully!")
    }

    private fun shareImage(imageBitmap: Bitmap) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, imageBitmap, null, null)))
        startActivity(intent)
    }

    companion object{
        const val TAG = "ImageProcessingInfo"
    }
}
enum class MenuButton(val label: String){
    GRAYSCALE("GrayScale"),
    BLUER("Bluer"),
    RGB("RGB"),
    RESIZE("Resize"),
    ADJUST_BRIGHTNESS("Adjust Brightness"),
    SHARP("Sharp"),
    SATURATION("Saturation"),
    FILTER("Filter"),
}