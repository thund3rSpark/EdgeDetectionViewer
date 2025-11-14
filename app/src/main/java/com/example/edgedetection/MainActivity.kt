package com.example.edgedetection

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.edgedetection.databinding.ActivityMainBinding
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var fpsTextView: TextView
    private lateinit var toggleButton: Button

    private var isProcessingEnabled = true
    private var frameCount = 0
    private var lastFpsTime = System.nanoTime()

    companion object {
        private const val TAG = "EdgeDetectionApp"
        private const val REQUEST_CAMERA_PERMISSION = 100

        init {
            System.loadLibrary("edgedetection")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        setupClickListeners()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        // Test native library
        Log.d(TAG, stringFromJNI())
        Toast.makeText(this, stringFromJNI(), Toast.LENGTH_SHORT).show()
    }

    private fun initializeViews() {
        fpsTextView = binding.fpsTextView
        toggleButton = binding.toggleButton
    }

    private fun setupClickListeners() {
        toggleButton.setOnClickListener {
            isProcessingEnabled = !isProcessingEnabled
            toggleButton.text = if (isProcessingEnabled) "Show Raw" else "Show Edges"
        }
    }

    private fun allPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, ImageAnalyzer { updateFps() })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun updateFps() {
        frameCount++
        val currentTime = System.nanoTime()
        val elapsedTime = (currentTime - lastFpsTime) / 1_000_000_000.0

        if (elapsedTime >= 1.0) {
            val fps = frameCount / elapsedTime
            runOnUiThread {
                fpsTextView.text = "FPS: ${"%.1f".format(fps)}"
            }
            frameCount = 0
            lastFpsTime = currentTime
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private external fun stringFromJNI(): String
    private external fun processFrame(
        imageData: ByteArray,
        width: Int,
        height: Int,
        outputData: IntArray
    )
}