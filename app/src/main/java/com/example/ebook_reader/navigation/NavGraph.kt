package com.example.ebook_reader.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ebook_reader.ui.library.LibraryScreen
import com.example.ebook_reader.ui.reader.ReaderScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Library.route
    ) {
        composable(Routes.Library.route) {
            LibraryScreen(
                onBookClick = { bookId ->
                    navController.navigate(
                        Routes.Reader.createRoute(bookId)
                    )
                }
            )
        }

        composable(
            route = Routes.Reader.route,
            arguments = listOf(
                navArgument("bookId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
            ReaderScreen(bookId = bookId)
        }
    }
}