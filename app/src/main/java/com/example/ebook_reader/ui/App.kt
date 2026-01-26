package com.example.ebook_reader.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.ebook_reader.navigation.NavGraph

@Composable
fun App() {
    val navController = rememberNavController()
    NavGraph(navController = navController)
}