package com.example.ebook_reader.navigation

sealed class Routes(val route: String) {
    object Library : Routes("library")
    object Reader : Routes("reader/{bookId}") {
        fun createRoute(bookId: String) = "reader/$bookId"
    }
}