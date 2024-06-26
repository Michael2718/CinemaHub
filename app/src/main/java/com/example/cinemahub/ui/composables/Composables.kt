package com.example.cinemahub.ui.composables

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cinemahub.R
import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.model.api.movie.MovieSearchResponse
import com.example.cinemahub.model.api.user.User
import com.example.cinemahub.ui.theme.CinemaHubTheme
import kotlinx.datetime.LocalDate

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
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean? = null,
    onFavoriteClick: ((String) -> Unit)? = null
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
                        "%.${1}f".format(movie.voteAverage)
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
            if (onFavoriteClick != null && isFavorite != null) {
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
}

@Composable
fun MovieListItemCompact(
    movieSearchResponse: MovieSearchResponse,
    supportingText: String,
    onFavoriteClick: (String, Boolean) -> Unit,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onMovieClick(movieSearchResponse.movieId) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (movieSearchResponse.primaryImageUrl.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.broken_image_24),
                    contentDescription = null,
                    modifier = Modifier,
                    contentScale = ContentScale.Fit
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(movieSearchResponse.primaryImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = movieSearchResponse.title,
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
                        "%.${2}f".format(movieSearchResponse.voteAverage).toDouble()
                    }",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = movieSearchResponse.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${movieSearchResponse.releaseDate.year}",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${movieSearchResponse.duration.hours}h ${movieSearchResponse.duration.minutes}m",
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
                    onFavoriteClick(movieSearchResponse.movieId, movieSearchResponse.isFavorite)
                }
            ) {
                Icon(
                    imageVector = if (movieSearchResponse.isFavorite) {
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

@Composable
fun CommonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit),
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    isValid: Boolean = true,
    errorMessage: String = "",
    isEnabled: Boolean = true,
    onIconClick: () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: Shape = CircleShape
) {
    OutlinedTextField(
        value = value,
        label = label,
        onValueChange = onValueChange,
        shape = shape,
        singleLine = true,
        leadingIcon = if (leadingIcon == null) {
            null
        } else {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable { onIconClick() }
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction
        ),
        isError = !isValid,
        supportingText = if (!isValid) {
            {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            null
        },
        enabled = isEnabled,
        visualTransformation = visualTransformation,
        modifier = modifier
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)?,
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    isValid: Boolean = true,
    errorMessage: String = "",
    shape: Shape = CircleShape
) {
    OutlinedTextField(
        value = value,
        label = label,
        onValueChange = onValueChange,
        shape = shape,
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                modifier = Modifier.padding(start = 4.dp)
            )
        },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        isError = !isValid,
        supportingText = if (!isValid) {
            {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            null
        },
        modifier = modifier
    )
}

@Composable
fun ImageCard(
    imageLink: String?,
    context: Context,
    contentScale: ContentScale,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .animateContentSize(),
        shape = RectangleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        if (imageLink == null) {
            Image(
                painter = painterResource(R.drawable.broken_image_24),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight(),
                alignment = Alignment.Center,
                contentScale = contentScale
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context = context)
                    .data(imageLink)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight(),
                placeholder = painterResource(R.drawable.loading_24),
                error = painterResource(R.drawable.broken_image_24),
                alignment = Alignment.Center,
                contentScale = contentScale
            )
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    onUserClick: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onUserClick(user.userId) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    text = "UserId: " + user.userId.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Username: " + user.username,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Name: " + user.firstName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Last name: " + user.lastName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Email: " + user.email,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Phone number: " + user.phoneNumber,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Birthday: " + user.birthDate,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = {
                    onDelete(user.userId)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
fun UserItemsListPreview() {
    CinemaHubTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(16.dp)
            ) {
                UserListItem(
                    user = User(
                        1,
                        "user1",
                        "John",
                        "Doe",
                        "example@gmail.com",
                        "1231231212",
                        LocalDate.parse("1999-01-01")
                    ),
                    onUserClick = {},
                    onDelete = {}
                )
                UserListItem(
                    user = User(
                        1,
                        "user1",
                        "John",
                        "Doe",
                        "example@gmail.com",
                        "1231231212",
                        LocalDate.parse("1999-01-01")
                    ),
                    onUserClick = {},
                    onDelete = {}
                )
            }
        }
    }
}

