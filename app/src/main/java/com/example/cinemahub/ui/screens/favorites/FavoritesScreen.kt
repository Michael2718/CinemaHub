package com.example.cinemahub.ui.screens.favorites

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cinemahub.R
import com.example.cinemahub.model.api.favorite.FavoriteResponse
import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.ui.components.ErrorScreen
import com.example.cinemahub.ui.components.LoadingScreen
import com.example.cinemahub.ui.theme.CinemaHubTheme
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()

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
//                modifier = Modifier
            )
        },
        modifier = modifier
    ) {
        FavoritesScreenContent(
            uiState = uiState,
            pullRefreshState = pullRefreshState,
            onRefresh = {
                viewModel.fetchFavorites()
//                    pullRefreshState.startRefresh()
            },
            onFavoriteClick = { movieId ->
                viewModel.removeFavorite(movieId)
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
//                        Box(
//                            modifier = Modifier.fillMaxSize()
//                        ) {}
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
                                favorite = favorite,
                                isFavorite = true,
                                onFavoriteClick = onFavoriteClick,
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

@Composable
fun MovieListItemCompact(
    favorite: FavoriteResponse,
    isFavorite: Boolean,
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val movie = remember { favorite.movie }
    val addedDate = remember { favorite.addedDate }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (movie.primaryImageUrl.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.broken_image_24), // Placeholder image
                    contentDescription = null,
                    modifier = Modifier,
                    contentScale = ContentScale.Fit
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(movie.primaryImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = movie.title,
                    modifier = Modifier
                        .height(128.dp)
                        .aspectRatio(2 / 3f)
                        .animateContentSize(),
                    placeholder = painterResource(R.drawable.loading_24),
                    error = painterResource(R.drawable.broken_image_24),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        top = 4.dp,
                        end = 0.dp,
                        bottom = 4.dp
                    )
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Release Date: ${movie.releaseDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Rating: ${
                        "%.${2}f".format(movie.voteAverage).toDouble()
                    } (${movie.voteCount} votes)",
//                    text = "Rating: ${movie.voteAverage} (${movie.voteCount} votes)",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Added to the list on $addedDate",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = {
                    onFavoriteClick(movie.movieId)
                }
            ) {
                Icon(
                    imageVector = if (isFavorite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    contentDescription = null
                )
            }
        }
    }
}


@Composable
@Preview
fun PreviewMovieListItemCompact() {
    val movieSample = Movie(
        "1",
        "Movie Title",
        LocalDate.parse("2002-01-01"), // Example date format, adjust based on your actual data structure
        8.503248,
        1000,
        "Plot summary of the movie...",
        false,
        75,
//        10.99, // Example price, adjust based on your actual data structure
        "https://m.media-amazon.com/images/M/MV5BY2I4MmM1N2EtM2YzOS00OWUzLTkzYzctNDc5NDg2N2IyODJmXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg"
    )
    val favorite = FavoriteResponse(
        movieSample,
        LocalDate.parse("2024-01-01")
    )

    val favorites = listOf(favorite, favorite, favorite, favorite)
    CinemaHubTheme {
        Surface {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(items = favorites) { favorite ->
                    MovieListItemCompact(
                        favorite = favorite,
                        isFavorite = true,
                        onFavoriteClick = {

                        },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}