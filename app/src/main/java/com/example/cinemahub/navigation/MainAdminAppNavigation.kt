package com.example.cinemahub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.cinemahub.navigation.routes.AddMovie
import com.example.cinemahub.navigation.routes.Movies
import com.example.cinemahub.navigation.routes.MoviesGraph
import com.example.cinemahub.navigation.routes.Users
import com.example.cinemahub.navigation.routes.UsersGraph
import com.example.cinemahub.ui.screens.admin.movies.AddMovieScreen
import com.example.cinemahub.ui.screens.admin.movies.AddMovieViewModel
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
            val uiState by viewModel.uiState.collectAsState()
            val keyboardController = LocalSoftwareKeyboardController.current
            MoviesScreen(
                uiState = uiState,
                onMovieClick = {

                },
                onAddMovie = {
                    navController.navigate(AddMovie)
                },
                onRefresh = viewModel::fetchMovies,
                onDelete = viewModel::deleteMovie,
                onQueryChange = viewModel::updateQuery,
                onSearch = {
                    viewModel.updateQuery(it)
                    viewModel.fetchMovies()
                    viewModel.setSearching(false)
                    keyboardController?.hide()
                },
                onActiveChange = viewModel::setSearching,
                onBack = {
                    viewModel.setSearching(false)
                    keyboardController?.hide()
                },
                onClear = {
                    viewModel.updateQuery("")
                    viewModel.fetchMovies()
                },
            )
        }

        composable<AddMovie> {
            val viewModel: AddMovieViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            AddMovieScreen(
                uiState = uiState,
                onSaveClick = viewModel::save,
                updateMovieId = viewModel::updateMovieId,
                updateTitle = viewModel::updateTitle,
                updateReleaseDate = viewModel::updateReleaseDate,
                updateDuration = viewModel::updateDuration,
                updatePlot = viewModel::updatePlot,
                updateAdult = viewModel::updateAdult,
                updatePrice = viewModel::updatePrice,
                updatePrimaryImageUrl = viewModel::updatePrimaryImageUrl,
                onBack = {
                    navController.navigateUp()
                },
                onClear = viewModel::clearUiState
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
