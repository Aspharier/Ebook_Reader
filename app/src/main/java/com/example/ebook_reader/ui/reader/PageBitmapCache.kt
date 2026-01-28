package com.example.ebook_reader.ui.reader

import android.graphics.Bitmap
import androidx.collection.LruCache
import androidx.room.Index

class PageBitmapCache {
    private val cache =
        object : LruCache<Int, Bitmap>(10) {} // 10 Pages max

    fun get(pageIndex: Int): Bitmap? = cache.get(pageIndex)

    fun put(pageIndex: Int, bitmap: Bitmap) {
        cache.put(pageIndex, bitmap)
    }
    fun clear() {
        cache.evictAll()
    }
}