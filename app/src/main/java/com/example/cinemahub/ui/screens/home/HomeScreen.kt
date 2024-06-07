package com.example.cinemahub.ui.screens.home

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cinemahub.R
import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.network.GenresMoviesRequestStatus
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.ui.composables.ErrorScreen
import com.example.cinemahub.ui.composables.ImageCard
import com.example.cinemahub.ui.composables.LoadingScreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = modifier
            )
        },
        modifier = modifier
    ) {
        HomeScreenContent(
            genresMoviesRequestStatus = uiState.genresMoviesRequestStatus,
            onMovieClick = onMovieClick,
            pullRefreshState = pullRefreshState,
            context = context,
            onRefresh = {
                viewModel.fetchAllGenresMovies()
            },
            modifier = Modifier
                .padding(it)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    genresMoviesRequestStatus: GenresMoviesRequestStatus,
    pullRefreshState: PullToRefreshState,
    context: Context,
    onMovieClick: (String) -> Unit,
    onRefresh: () -> Unit,
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
            modifier = Modifier.fillMaxWidth(),
//            contentPadding = PaddingValues(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (genresMoviesRequestStatus) {
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
                    val genresMovies = genresMoviesRequestStatus.data?.filterValues { it.isNotEmpty() }
                    if (genresMovies.isNullOrEmpty()) {
                        items(1) {
                            Text("No genres")
                        }
                    } else {
                        items(items = genresMovies.keys.toList(), key = { it }) { name ->
                            GenresListItem(
                                title = name,
                                movies = genresMovies[name] ?: listOf(),
                                onMovieClick = onMovieClick,
                                context = context,
                                modifier = Modifier
                                    .animateItem(fadeInSpec = null, fadeOutSpec = null)
                                    .padding(4.dp)
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
fun GenresListItem(
    title: String,
    movies: List<Movie>,
    context: Context,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ListItem(
            headlineContent = {
                Text(title, style = MaterialTheme.typography.titleLarge)
            },
        )
        LazyRow(
            modifier = Modifier.height(180.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = movies, key = { it.movieId }) { movie ->
                ImageCard(
                    imageLink = movie.primaryImageUrl,
                    context = context,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2 / 3f)
                        .clickable {
                            onMovieClick(movie.movieId)
                        }
                )
            }
        }
    }
}
