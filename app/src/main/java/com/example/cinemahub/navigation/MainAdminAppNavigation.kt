package com.example.cinemahub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.cinemahub.navigation.routes.Movies
import com.example.cinemahub.navigation.routes.MoviesGraph
import com.example.cinemahub.navigation.routes.Users
import com.example.cinemahub.navigation.routes.UsersGraph
import com.example.cinemahub.ui.screens.admin.movies.MoviesScreen
import com.example.cinemahub.ui.screens.admin.movies.MoviesViewModel

@Composable
fun MainAdminAppNavigation(
    navController: NavHostController,
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: Any = MoviesGraph
) {
//    val searchScreenViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
//        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
//    }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        moviesGraph(
            navController = navController
        )
//        reviewsGraph(
//            navController = navController
//        )
        usersGraph(
            navController = navController
        )
    }
}

fun NavGraphBuilder.moviesGraph(
    navController: NavHostController
) {
    navigation<MoviesGraph>(Movies) {
        composable<Movies> {
            val viewModel: MoviesViewModel = hiltViewModel()
            MoviesScreen(
                viewModel = viewModel,
                onMovieClick = {

                },
                onAddMovieClick = {

                },
                onSearchClick = {

                }
            )
        }
    }
}

//fun NavGraphBuilder.reviewsGraph(
//    navController: NavHostController
//) {
//    navigation<ReviewsGraph>(Reviews) {
//        composable<Reviews> {
//
//        }
//    }
//}

fun NavGraphBuilder.usersGraph(
    navController: NavHostController
) {
    navigation<UsersGraph>(Users) {
        composable<Users> {

        }
    }
}
