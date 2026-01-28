package com.example.ebook_reader.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

fun saveCoverBitmap(
    context: Context,
    bookId: String,
    bitmap: Bitmap
): String {
    val coverFile = File(context.filesDir, "$bookId-cover.png")
    FileOutputStream(coverFile).use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    }
    return coverFile.absolutePath
}