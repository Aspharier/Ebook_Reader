package com.example.ebook_reader.utils

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.File

data class PdfProcessingResult(
    val pageCount: Int,
    val coverBitmap: Bitmap?
)

fun processPdf(file: File): PdfProcessingResult? {
    return try {
        val fileDescriptor =
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)

        val renderer = PdfRenderer(fileDescriptor)

        val pageCount = renderer.pageCount

        val coverBitmap =
            if (pageCount > 0) {
                val page = renderer.openPage(0)

                val targetWidth = 300
                val scale = targetWidth.toFloat() / page.width
                val targetHeight = (page.height * scale).toInt()

                val bitmap = Bitmap.createBitmap(
                    targetWidth,
                    targetHeight,
                    Bitmap.Config.ARGB_8888
                )
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()
                bitmap
            } else {
                null
            }

        renderer.close()
        fileDescriptor.close()

        PdfProcessingResult(
            pageCount = pageCount,
            coverBitmap = coverBitmap
        )
    } catch (e: Exception) {
        Log.e("PdfUtils", "Error processing PDF: ${file.path}", e)
        null
    }
}
