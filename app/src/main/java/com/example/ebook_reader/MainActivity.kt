package com.example.ebook_reader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ebook_reader.ui.App
import com.example.ebook_reader.ui.theme.Ebook_ReaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ebook_ReaderTheme {
                App()
                }
            }
        }
    }