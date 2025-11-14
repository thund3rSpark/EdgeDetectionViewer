package com.example.edgedetection

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

class ImageAnalyzer(private val onFrameProcessed: () -> Unit) : ImageAnalysis.Analyzer {

    override fun analyze(imageProxy: ImageProxy) {
        try {
            // Process image here if needed
            // For now, just count frames for FPS
        } finally {
            imageProxy.close()
            onFrameProcessed()
        }
    }

    private fun ImageProxy.toByteArray(): ByteArray {
        val planes = this.planes
        val buffer: ByteBuffer = planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return bytes
    }
}