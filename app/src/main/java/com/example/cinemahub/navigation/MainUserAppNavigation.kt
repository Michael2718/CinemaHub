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
import androidx.navigation.navigation
import com.example.cinemahub.navigation.routes.Favorites
import com.example.cinemahub.navigation.routes.FavoritesGraph
import com.example.cinemahub.navigation.routes.History
import com.example.cinemahub.navigation.routes.Home
import com.example.cinemahub.navigation.routes.HomeGraph
import com.example.cinemahub.navigation.routes.MovieDetails
import com.example.cinemahub.navigation.routes.Profile
import com.example.cinemahub.navigation.routes.ProfileGraph
import com.example.cinemahub.navigation.routes.Search
import com.example.cinemahub.navigation.routes.SearchFilter
import com.example.cinemahub.navigation.routes.SearchGraph
import com.example.cinemahub.ui.screens.user.favorites.FavoritesScreen
import com.example.cinemahub.ui.screens.user.favorites.FavoritesViewModel
import com.example.cinemahub.ui.screens.user.history.HistoryScreen
import com.example.cinemahub.ui.screens.user.history.HistoryViewModel
import com.example.cinemahub.ui.screens.user.home.HomeScreen
import com.example.cinemahub.ui.screens.user.home.HomeViewModel
import com.example.cinemahub.ui.screens.user.movie_details.MovieDetailsScreen
import com.example.cinemahub.ui.screens.user.movie_details.MovieDetailsViewModel
import com.example.cinemahub.ui.screens.user.profile.ProfileScreen
import com.example.cinemahub.ui.screens.user.profile.ProfileViewModel
import com.example.cinemahub.ui.screens.user.search.SearchFiltersScreen
import com.example.cinemahub.ui.screens.user.search.SearchScreen
import com.example.cinemahub.ui.screens.user.search.SearchViewModel

@Composable
fun MainUserAppNavigation(
    navController: NavHostController,
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: Any = HomeGraph
) {
    val searchScreenViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        homeGraph(
            navController = navController
        )
        searchGraph(
            navController = navController,
            viewModelStoreOwner = searchScreenViewModelStoreOwner
        )
        favoritesGraph(navController = navController)
        profileGraph(
//            navController = navController,
            onLogOut = onLogOut
        )
        composable<MovieDetails> {
            val viewModel: MovieDetailsViewModel = hiltViewModel()
            MovieDetailsScreen(
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
    navigation<HomeGraph>(Home) {
        composable<Home> {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel,
                onMovieClick = {
                    navController.navigate(MovieDetails(it))
                }
            )
        }
    }
}

fun NavGraphBuilder.searchGraph(
    navController: NavHostController,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    navigation<SearchGraph>(Search) {
        composable<Search> {
            val viewModel: SearchViewModel = hiltViewModel(viewModelStoreOwner)
            SearchScreen(
                viewModel = viewModel,
                onFilter = {
                    navController.navigate(SearchFilter)
                },
                onMovieClick = {
                    navController.navigate(MovieDetails(it))
                }
            )
        }

        composable<SearchFilter> {
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
    navigation<FavoritesGraph>(Favorites) {
        composable<Favorites> {
            val viewModel: FavoritesViewModel = hiltViewModel()
            viewModel.fetchFavorites()
            FavoritesScreen(
                viewModel = viewModel,
                onMovieClick = {
                    navController.navigate(MovieDetails(it))
                },
                onHistory = {
                    navController.navigate(History(it))
                }
            )
        }
        composable<History> {
            val viewModel: HistoryViewModel = hiltViewModel()
            HistoryScreen(
                viewModel = viewModel,
                onBack = {
                    navController.navigateUp()
                },
                onMovieClick = {
                    navController.navigate(MovieDetails(it))
                }
            )
        }
    }
}

fun NavGraphBuilder.profileGraph(
    onLogOut: () -> Unit
) {
    navigation<ProfileGraph>(Profile) {
        composable<Profile> {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                viewModel = viewModel,
                onLogOut = onLogOut
            )
        }
    }
}
