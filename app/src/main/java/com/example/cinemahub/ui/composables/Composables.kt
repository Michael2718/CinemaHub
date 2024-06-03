package com.example.cinemahub.ui.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cinemahub.R
import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.model.api.movie.MovieSearchResponse

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.loading_24),
        contentDescription = "Loading",
        modifier = modifier.size(200.dp)
    )
}

@Composable
fun ErrorScreen(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.connection_error_24),
            contentDescription = null
        )
        Text(
            text = "Loading failed",
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = onRefresh) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun MovieListItemCompact(
    movie: Movie,
    supportingText: String,
    isFavorite: Boolean,
    onFavoriteClick: (String) -> Unit,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onMovieClick(movie) },
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
                    painter = painterResource(id = R.drawable.broken_image_24),
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
                verticalArrangement = Arrangement.spacedBy(4.dp),
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
                    text = "⭐${
                        "%.${2}f".format(movie.voteAverage).toDouble()
                    }",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${movie.releaseDate.year}",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${movie.duration.hours}h ${movie.duration.minutes}m",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = supportingText,
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
fun MovieListItemCompact(
    movie: MovieSearchResponse,
    supportingText: String,
    onFavoriteClick: (String, Boolean) -> Unit,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onMovieClick(movie.movieId) },
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
                    painter = painterResource(id = R.drawable.broken_image_24),
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
                verticalArrangement = Arrangement.spacedBy(4.dp),
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
                    text = "⭐${
                        "%.${2}f".format(movie.voteAverage).toDouble()
                    }",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${movie.releaseDate.year}",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${movie.duration.hours}h ${movie.duration.minutes}m",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = {
                    onFavoriteClick(movie.movieId, movie.isFavorite)
                }
            ) {
                Icon(
                    imageVector = if (movie.isFavorite) {
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
fun FilterChipGroupSingle(
    items: List<String>,
    selectedItemIndex: Int,
    onSelectionChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
    ) {
        items(items.size) { index: Int ->
            val selected = items[selectedItemIndex] == items[index]
            FilterChip(
                label = { Text(items[index]) },
                onClick = {
                    onSelectionChange(index)
                },
                selected = selected,
                leadingIcon = {
                    if (selected) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
            )
        }
    }
}

//@Composable
//fun FilterChipGroupMulti(
//    items: List<String>,
//    selectedItems: Set<String>,
//    onSelectionChange: (Set<String>) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    LazyRow(
//        modifier = modifier
//    ) {
//        items(items = items) { item ->
//            val selected = selectedItems.contains(item)
//            FilterChip(
//                label = { Text(item) },
//                onClick = {
//                    onSelectionChange(
//                        if (selected) {
//                            selectedItems - item
//                        } else {
//                            selectedItems + item
//                        }
//                    )
//                },
//                selected = selected,
//                leadingIcon = {
//                    if (selected) {
//                        Icon(
//                            imageVector = Icons.Default.Done,
//                            contentDescription = null
//                        )
//                    }
//                },
//                modifier = Modifier
//                    .padding(horizontal = 4.dp, vertical = 4.dp)
//            )
//        }
//    }
//}
