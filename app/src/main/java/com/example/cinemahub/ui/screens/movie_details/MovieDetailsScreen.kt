package com.example.cinemahub.ui.screens.movie_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cinemahub.model.api.movie.MovieSearchResponse
import com.example.cinemahub.model.api.review.ReviewResponse
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.network.ReviewsRequestStatus
import com.example.cinemahub.network.UserReviewRequestStatus
import com.example.cinemahub.ui.composables.ErrorScreen
import com.example.cinemahub.ui.composables.ImageCard
import com.example.cinemahub.ui.composables.LoadingScreen
import com.example.cinemahub.ui.theme.CinemaHubTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Movie Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        MovieDetailsContent(
            uiState = uiState,
            pullRefreshState = pullRefreshState,
            onRefresh = {
                viewModel.fetchMovie()
                viewModel.fetchReviews()
            },
            onLikeClick = { movieId, userId ->
                viewModel.likeReview(movieId, userId)
                viewModel.fetchReviews()
//                pullRefreshState.startRefresh()
            },
            onDislikeClick = { movieId, userId ->
                viewModel.dislikeReview(movieId, userId)
                viewModel.fetchReviews()
//                pullRefreshState.startRefresh()
            },
            onSubmit = { vote, comment ->
                viewModel.addReview(vote, comment)
                viewModel.fetchReviews()
            },
            onFavoriteClick = {
                viewModel.onFavoriteClick(it)
                viewModel.fetchMovie()
            },
            modifier = Modifier
                .padding(innerPadding)
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
fun MovieDetailsContent(
    uiState: MovieDetailsUiState,
    pullRefreshState: PullToRefreshState,
    onRefresh: () -> Unit,
    onSubmit: (Int, String) -> Unit,
    onLikeClick: (String, Int) -> Unit,
    onDislikeClick: (String, Int) -> Unit,
    onFavoriteClick: (Boolean) -> Unit,
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
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            when (uiState.movieRequestStatus) {
                is RequestStatus.Error -> {
                    ErrorScreen(
                        onRefresh = { pullRefreshState.startRefresh() },
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                is RequestStatus.Loading -> {
                    LoadingScreen(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                is RequestStatus.Success -> {
                    val movie = uiState.movieRequestStatus.data
                    MovieDetails(
                        movie = movie,
                        onFavoriteClick = onFavoriteClick
                    )
                    Reviews(
                        onSubmit = onSubmit,
                        reviewsRequestStatus = uiState.reviewsRequestStatus,
                        userReviewRequest = uiState.userReviewRequestStatus,
                        onLikeClick = onLikeClick,
                        onDislikeClick = onDislikeClick
                    )
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
fun MovieDetails(
    movie: MovieSearchResponse,
    onFavoriteClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
//    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
    ) {
        ListItem(
            headlineContent = {
                ImageCard(
                    imageLink = movie.primaryImageUrl,
                    context = LocalContext.current,
                    modifier = Modifier
                        .height(320.dp)
                )
            }
        )
        ListItem(
            headlineContent = {
                Text(
                    text = movie.title,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        )
        ListItem(
            headlineContent = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${movie.releaseDate.year}",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${movie.duration.hours}h ${movie.duration.minutes}m",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "⭐${"%.${1}f".format(movie.voteAverage)} (${movie.voteCount} votes)",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        )
        ListItem(
            headlineContent = {
//                var isFavorite by remember { mutableStateOf(true) }
                if (movie.isFavorite) {
//                    IconButton(
//                        onClick = {}
//                    ) {
//                        Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
//                    }
                    OutlinedButton(
                        onClick = { onFavoriteClick(true) }
                    ) {
                        Icon(imageVector = Icons.Default.Done, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Remove from favorites")
                    }
                } else {
                    Button(
                        onClick = { onFavoriteClick(false) },
                    ) {
                        Text("Add to favorites")
                    }
                }
            }
        )


        ListItem(
            headlineContent = { Text("Plot", fontWeight = FontWeight.SemiBold) },
            supportingContent = { Text(movie.plot) }
        )

        Row {
            ListItem(
                headlineContent = { Text("Price", fontWeight = FontWeight.SemiBold) },
                supportingContent = { Text(movie.price.toString()) },
                modifier = Modifier.weight(1f)
            )

            ListItem(
                headlineContent = { Text("Adult", fontWeight = FontWeight.SemiBold) },
                supportingContent = { Text(if (movie.isAdult) "Yes" else "No") },
                modifier = Modifier.weight(1f)
            )
        }

    }
}

@Composable
fun Reviews(
    onSubmit: (Int, String) -> Unit,
    reviewsRequestStatus: ReviewsRequestStatus,
    userReviewRequest: UserReviewRequestStatus,
    onLikeClick: (String, Int) -> Unit,
    onDislikeClick: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
//        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text("Your review:")
        when(userReviewRequest) {
            is RequestStatus.Error -> {
                Text("Something went wrong...")
                Text(userReviewRequest.exception.toString())
            }
            is RequestStatus.Loading -> {
                LoadingScreen(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            is RequestStatus.Success -> {
                if (userReviewRequest.data == null) {
                    AddReview(
                        onSubmit = onSubmit
                    )
                } else {
                    ReviewCard(
                        userReviewRequest.data,
                        onLikeClick = onLikeClick,
                        onDislikeClick = onDislikeClick
                    )
                }
            }
        }
        Text("Reviews:")
        when (reviewsRequestStatus) {
            is RequestStatus.Error -> {
                Text("Something went wrong...")
            }

            is RequestStatus.Loading -> {
                LoadingScreen(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            is RequestStatus.Success -> {
                val reviews = reviewsRequestStatus.data
                if (reviews.isEmpty()) {
                    Text("No reviews")
                } else {
                    reviews.forEach {
                        ReviewCard(
                            it,
                            onLikeClick = onLikeClick,
                            onDislikeClick = onDislikeClick
                        )
                    }
                }
            }
        }
//        when (requestStatus) {
//            is RequestStatus.Error -> {
//                items(1) {
//                    Text("Something went wrong...")
//                }
//            }
//
//            is RequestStatus.Loading -> {
//                items(1) {
//                    LoadingScreen(
//                        modifier = Modifier
//                            .fillMaxSize()
//                    )
//                }
//            }
//
//            is RequestStatus.Success -> {
//                val reviews = requestStatus.data
//                if (reviews.isEmpty()) {
//                    items(1) {
//                        Text("No reviews")
//                    }
//                } else {
//                    items(items = reviews, key = { it.username }) {
//                        ReviewCard(it)
//                    }
//                }
//            }
//        }
    }
}

@Composable
fun ReviewCard(
//    currentUsername: String,
    review: ReviewResponse,
    onLikeClick: (String, Int) -> Unit,
    onDislikeClick: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "@${review.username}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "⭐${review.vote}/10",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium
            )

//            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            onLikeClick(review.movieId, review.userId)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ThumbUp,
                            contentDescription = "Likes",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
//                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${review.likes}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            onDislikeClick(review.movieId, review.userId)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ThumbUp,
                            contentDescription = "Likes",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.graphicsLayer {
                                rotationX = 180f
                                rotationY = 180f
                            }
                        )
                    }
//                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${review.dislikes}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun AddReview(
    onSubmit: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var comment by remember { mutableStateOf("") }
    var vote by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Add Your Review",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comment") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = vote,
            onValueChange = { vote = it },
            label = { Text("Vote (0-10)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (comment.isNotEmpty() && vote.isNotEmpty() && vote.toInt() <= 10 && vote.toInt() >= 0) {
                    onSubmit(vote.toInt(), comment)
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieDetailsPreview(modifier: Modifier = Modifier) {
    CinemaHubTheme {
//        MovieDetails(
//            movie = Movie(
//                "tt0086190",
//                "Star Wars: Episode VI - Return of the Jedi",
//                LocalDate.parse("1983-05-25"),
//                PGInterval(0, 0, 0, 2, 11, 0.0),
//                8.3,
//                1123655,
//                "After rescuing Han Solo from Jabba the Hutt, the Rebel Alliance attempt to destroy the second Death Star, while Luke struggles to help Darth Vader back from the dark side.",
//                false,
//                0,
//                PGmoney("$9.99"),
//                "https://m.media-amazon.com/images/M/MV5BOWZlMjFiYzgtMTUzNC00Y2IzLTk1NTMtZmNhMTczNTk0ODk1XkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg"
//            )
//        )
    }
}