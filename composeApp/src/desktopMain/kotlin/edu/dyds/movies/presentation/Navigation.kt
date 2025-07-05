package edu.dyds.movies.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.dyds.movies.di.MoviesDependencyInjector
import edu.dyds.movies.presentation.detail.DetailScreen
import edu.dyds.movies.presentation.home.HomeScreen

private const val HOME = "home"

private const val DETAIL = "detail"

private const val MOVIE_TITLE = "movieTitle"

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HOME) {
        homeDestination(navController)
        detailDestination(navController)
    }
}

private fun NavGraphBuilder.homeDestination(navController: NavHostController) {
    composable(HOME) {
        HomeScreen(
            viewModel = MoviesDependencyInjector.getHomeScreenViewModel(),
            onGoodMovieClick = {
                navController.navigate("$DETAIL/${it.title}")
            }
        )
    }
}

private fun NavGraphBuilder.detailDestination(navController: NavHostController) {
    composable(
        route = "$DETAIL/{$MOVIE_TITLE}",
        arguments = listOf(navArgument(MOVIE_TITLE) { type = NavType.Companion.StringType })
    ) { backstackEntry ->
        val movieId = backstackEntry.arguments?.getString(MOVIE_TITLE)

        movieId?.let {
            DetailScreen(
                MoviesDependencyInjector.getDetailScreenViewModel(),
                it,
                onBack = { navController.popBackStack() })
        }
    }
}