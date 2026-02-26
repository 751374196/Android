package com.cpic.deviceinspection

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cpic.deviceinspection.model.DeviceInfo
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CaptureImageActivity : AppCompatActivity() {
    
    private lateinit var viewFinder: PreviewView
    private lateinit var captureButton: Button
    private lateinit var imageView: ImageView
    private lateinit var confirmButton: Button
    private lateinit var retryButton: Button

    private var imageCapture: ImageCapture? = null
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var capturedImage: Bitmap? = null
    private var deviceInfo: DeviceInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_image)

        deviceInfo = intent.getSerializableExtra("deviceInfo") as? DeviceInfo

        viewFinder = findViewById(R.id.view_finder)
        captureButton = findViewById(R.id.btn_capture)
        imageView = findViewById(R.id.image_view)
        confirmButton = findViewById(R.id.btn_confirm)
        retryButton = findViewById(R.id.btn_retry)

        checkCameraPermission()

        captureButton.setOnClickListener {
            takePhoto()
        }

        confirmButton.setOnClickListener {
            capturedImage?.let {bitmap ->
                recognizeText(bitmap)
            }
        }

        retryButton.setOnClickListener {
            resetCamera()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1002)
        } else {
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1002) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "需要相机权限才能拍照", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({ val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get(); bindCameraUseCases(cameraProvider) }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build().also { it.setSurfaceProvider(viewFinder.surfaceProvider) }

        imageCapture = ImageCapture.Builder().build()

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        } catch (exc: Exception) {
            Toast.makeText(this, "相机启动失败", Toast.LENGTH_SHORT).show()
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(externalMediaDirs.firstOrNull(), SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(System.currentTimeMillis()) + ".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = photoFile.absolutePath
                capturedImage = BitmapFactory.decodeFile(savedUri)
                showCapturedImage()
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(this@CaptureImageActivity, "拍照失败", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showCapturedImage() {
        capturedImage?.let {
            imageView.setImageBitmap(it)
            imageView.visibility = ImageView.VISIBLE
            viewFinder.visibility = PreviewView.GONE
            captureButton.visibility = Button.GONE
            confirmButton.visibility = Button.VISIBLE
            retryButton.visibility = Button.VISIBLE
        }
    }

    private fun resetCamera() {
        capturedImage = null
        imageView.setImageBitmap(null)
        imageView.visibility = ImageView.GONE
        viewFinder.visibility = PreviewView.VISIBLE
        captureButton.visibility = Button.VISIBLE
        confirmButton.visibility = Button.GONE
        retryButton.visibility = Button.GONE
        startCamera()
    }

    private fun recognizeText(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val recognizedText = visionText.text
                val numericValue = extractNumericValue(recognizedText)
                navigateToDataEntry(numericValue)
            }
            .addOnFailureListener {
                Toast.makeText(this, "识别失败，请重试", Toast.LENGTH_SHORT).show()
            }
    }

    private fun extractNumericValue(text: String): Double {
        val regex = "[\\d.]+"toRegex()
        val matches = regex.findAll(text)
        for (match in matches) {
            return match.value.toDoubleOrNull() ?: 0.0
        }
        return 0.0
    }

    private fun navigateToDataEntry(value: Double) {
        val intent = Intent(this, DataEntryActivity::class.java)
        intent.putExtra("deviceInfo", deviceInfo)
        intent.putExtra("recognizedValue", value)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
