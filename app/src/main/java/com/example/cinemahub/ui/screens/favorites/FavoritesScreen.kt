package com.example.cinemahub.ui.screens.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.ui.composables.ErrorScreen
import com.example.cinemahub.ui.composables.LoadingScreen
import com.example.cinemahub.ui.composables.MovieListItemCompact
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favorite movies",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            pullRefreshState.startRefresh()
//                            viewModel.fetchFavorites()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                ),
                scrollBehavior = topAppBarScrollBehavior
//                modifier = Modifier
            )
        },
        modifier = modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    ) {
        FavoritesScreenContent(
            uiState = uiState,
            pullRefreshState = pullRefreshState,
            onRefresh = {
                viewModel.fetchFavorites()
//                    pullRefreshState.startRefresh()
            },
            onFavoriteClick = { movieId ->
                viewModel.deleteFavorite(movieId)
            },
            modifier = Modifier
                .padding(it)
                .padding(
                    start = 16.dp,
                    top = 0.dp,
                    end = 16.dp,
                    bottom = 0.dp
                )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreenContent(
    uiState: FavoritesScreenUiState,
    pullRefreshState: PullToRefreshState,
    onRefresh: () -> Unit,
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            delay(1500)
            onRefresh()
            pullRefreshState.endRefresh()
        }
    }

    Box(
        modifier = modifier
            .nestedScroll(pullRefreshState.nestedScrollConnection)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            when (uiState.favoritesRequestStatus) {
                is RequestStatus.Error -> {
                    items(1) {
                        ErrorScreen(
                            onRefresh = { pullRefreshState.startRefresh() },
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }

                is RequestStatus.Loading -> {
                    items(1) {
                        LoadingScreen(
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }

                is RequestStatus.Success -> {
                    val favorites = uiState.favoritesRequestStatus.data
                    if (favorites.isEmpty()) {
                        items(1) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Text("The list is empty")
                                Button(
                                    onClick = {}
                                ) {
                                    Text("Add some movies")
                                }
                            }
                        }
                    } else {
                        items(items = favorites, key = { it.movie.movieId }) { favorite ->
                            MovieListItemCompact(
                                movie = favorite.movie,
                                supportingText = "Added to the list on ${favorite.addedDate}",
                                isFavorite = true,
                                onFavoriteClick = onFavoriteClick,
                                onMovieClick = {

                                },
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        PullToRefreshContainer(
            modifier = Modifier
                .align(Alignment.TopCenter),
            state = pullRefreshState
        )
    }
}




//@Composable
//@Preview
//fun PreviewMovieListItemCompact() {
//    val movieSample = Movie(
//        "1",
//        "Movie Title",
//        LocalDate.parse("2002-01-01"), // Example date format, adjust based on your actual data structure
//        8.503248,
//        1000,
//        "Plot summary of the movie...",
//        false,
//        75,
////        10.99, // Example price, adjust based on your actual data structure
//        "https://m.media-amazon.com/images/M/MV5BY2I4MmM1N2EtM2YzOS00OWUzLTkzYzctNDc5NDg2N2IyODJmXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg"
//    )
//    val favorite = FavoriteResponse(
//        movieSample,
//        LocalDate.parse("2024-01-01")
//    )
//
//    val favorites = listOf(favorite, favorite, favorite, favorite)
//    CinemaHubTheme {
//        Surface {
//            LazyColumn(
//                verticalArrangement = Arrangement.spacedBy(16.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                items(items = favorites) { favorite ->
//                    MovieListItemCompact(
//                        movie = favorite.movie,
//                        supportingText = "Added to the list on ${favorite.addedDate}",
//                        isFavorite = true,
//                        onFavoriteClick = {
//
//                        },
//                        modifier = Modifier
//                    )
//                }
//            }
//        }
//    }
//}