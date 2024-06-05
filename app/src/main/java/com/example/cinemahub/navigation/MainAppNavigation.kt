package com.example.cinemahub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.cinemahub.ui.screens.favorites.FavoritesScreen
import com.example.cinemahub.ui.screens.favorites.FavoritesViewModel
import com.example.cinemahub.ui.screens.home.HomeScreen
import com.example.cinemahub.ui.screens.home.HomeViewModel
import com.example.cinemahub.ui.screens.movie_details.MovieDetailsScreen
import com.example.cinemahub.ui.screens.movie_details.MovieDetailsViewModel
import com.example.cinemahub.ui.screens.profile.ProfileScreen
import com.example.cinemahub.ui.screens.profile.ProfileViewModel
import com.example.cinemahub.ui.screens.search.SearchFiltersScreen
import com.example.cinemahub.ui.screens.search.SearchScreen
import com.example.cinemahub.ui.screens.search.SearchViewModel

@Composable
fun MainAppNavigation(
    navController: NavHostController,
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = Routes.HomeGraph.route
) {
    val searchScreenViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        homeGraph(navController = navController)
        searchGraph(
            navController = navController,
            viewModelStoreOwner = searchScreenViewModelStoreOwner
        )
        favoritesGraph(navController = navController)
        profileGraph(
            navController = navController,
            onLogOut = onLogOut
        )
        composable<MovieDetails> {
//            val args = it.toRoute<MovieDetails>()
            val viewModel: MovieDetailsViewModel = hiltViewModel()
            MovieDetailsScreen(
//                movieId = args.movieId,
                viewModel = viewModel,
                onBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavHostController
) {
    navigation(startDestination = Routes.Home.route, route = Routes.HomeGraph.route) {
        composable(Routes.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel
            )
        }
    }
}

fun NavGraphBuilder.searchGraph(
    navController: NavHostController,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    navigation(startDestination = Routes.Search.route, route = Routes.SearchGraph.route) {
        composable(Routes.Search.route) {
            val viewModel: SearchViewModel = hiltViewModel(viewModelStoreOwner)
            SearchScreen(
                viewModel = viewModel,
                onFilter = {
                    navController.navigate(Routes.SearchFilter.route)
                },
                onMovieClick = {
                    navController.navigate(MovieDetails(it))
                }
            )
        }

        composable(Routes.SearchFilter.route) {
            val viewModel: SearchViewModel = hiltViewModel(viewModelStoreOwner)
            SearchFiltersScreen(
                viewModel = viewModel,
                onBack = {
                    viewModel.search()
                    navController.navigateUp()
                },
                onApply = {
                    viewModel.search()
                    navController.navigateUp()
                }
            )
        }
    }
}

fun NavGraphBuilder.favoritesGraph(
    navController: NavHostController
) {
    navigation(startDestination = Routes.Favorites.route, route = Routes.FavoritesGraph.route) {
        composable(Routes.Favorites.route) {
            val viewModel: FavoritesViewModel = hiltViewModel()
            viewModel.fetchFavorites()
            FavoritesScreen(
                viewModel = viewModel,
                onMovieClick = {
                    navController.navigate(MovieDetails(it))
                }
            )
        }
    }
}

fun NavGraphBuilder.profileGraph(
    navController: NavHostController,
    onLogOut: () -> Unit
) {
    navigation(startDestination = Routes.Profile.route, route = Routes.ProfileGraph.route) {
        composable(Routes.Profile.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                viewModel = viewModel,
                onLogOut = onLogOut
            )
        }
    }
}
