package com.example.cinemahub.ui.screens.favorites

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.cinemahub.ui.components.LoadingScreen
import com.example.cinemahub.ui.theme.CinemaHubTheme
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favorite movies",
//                        color = MaterialTheme.colorScheme,
//                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                modifier = modifier
            )
        },
        modifier = modifier
    ) {
        FavoritesScreenContent(
            uiState = uiState,
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

@Composable
fun FavoritesScreenContent(
    uiState: FavoritesScreenUiState,
    modifier: Modifier = Modifier
) {
    when (uiState.favoritesRequestStatus) {
        is RequestStatus.Error -> {
            Text(
                uiState.favoritesRequestStatus.exception.toString(),
                modifier = modifier
            )
        }

        is RequestStatus.Loading -> {
            LoadingScreen(
                modifier = modifier
                    .fillMaxSize()
            )
        }

        is RequestStatus.Success -> {
            val favorites =
                uiState.favoritesRequestStatus.data
            LazyColumn(
//                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                items(items = favorites) { favorite ->
                    MovieListItemCompact(
                        favorite = favorite,
                        isFavorite = true,
                        onFavoriteClick = {

                        },
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MovieListItemCompact(
    favorite: FavoriteResponse,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val movie = favorite.movie
    val addedDate = favorite.addedDate

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
                onClick = onFavoriteClick
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