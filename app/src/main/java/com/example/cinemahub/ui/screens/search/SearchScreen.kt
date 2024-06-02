package com.example.cinemahub.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cinemahub.R
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.ui.composables.ErrorScreen
import com.example.cinemahub.ui.composables.LoadingScreen
import com.example.cinemahub.ui.composables.MovieListItemCompact
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            if (uiState.isSearching) {
                SearchTopAppBar(
                    query = uiState.query,
                    onQueryChange = {
                        viewModel.updateQuery(it)
                    },
                    onSearch = {
                        viewModel.updateQuery(it)
                        viewModel.search()
                        viewModel.setSearching(false)
                        keyboardController?.hide()
                    },
                    isSearching = true,
                    onActiveChange = {
                        viewModel.setSearching(it)
                    },
                    onBack = {
                        viewModel.setSearching(false)
                        keyboardController?.hide()
                    },
                    onClear = {
                        viewModel.updateQuery("")
                    },
                    keyboardController = keyboardController,
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            bottom = 8.dp
                        )
                        .fillMaxWidth()
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            text = "Search movies",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = onFilter
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_filter_list_24),
                                contentDescription = "Filters"
                            )
                        }
                        IconButton(
                            onClick = {
                                viewModel.setSearching(true)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        titleContentColor = MaterialTheme.colorScheme.primary,
                        actionIconContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        SearchScreenContent(
            uiState = uiState,
            pullRefreshState = pullRefreshState,
            onRefresh = {
                viewModel.search()
            },
            onFavoriteClick = { movieId, isFavorite ->
                viewModel.onFavoriteClick(movieId, isFavorite)
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
fun SearchScreenContent(
    uiState: SearchScreenUiState,
    pullRefreshState: PullToRefreshState,
    onRefresh: () -> Unit,
    onFavoriteClick: (String, Boolean) -> Unit,
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
            when (uiState.searchRequestStatus) {
                is RequestStatus.Error -> {
                    items(1) {
                        ErrorScreen(
                            onRefresh = {
                                pullRefreshState.startRefresh()
                            },// { pullRefreshState.startRefresh() },
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
                    val movies = uiState.searchRequestStatus.data
                    if (movies.isEmpty()) {
                        items(1) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Text("No movies found :(")
                            }
                        }
                    } else {
                        items(items = movies, key = { it.movieId }) { movie ->
                            MovieListItemCompact(
                                movie = movie,
                                supportingText = "Price: ${movie.price}",
                                onFavoriteClick = onFavoriteClick,
                                onMovieClick = {

                                },
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(
    query: TextFieldValue,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    isSearching: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onBack: () -> Unit,
    onClear: () -> Unit,
    keyboardController: SoftwareKeyboardController?,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isSearching) {
        if (isSearching) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    SearchBar(
        query = query.text,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = isSearching,
        enabled = isSearching,
        onActiveChange = onActiveChange,
        modifier = modifier
            .focusRequester(focusRequester),
        placeholder = { Text(text = "Search for movies") },
        leadingIcon = {
            if (isSearching) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = onClear) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear"
                )
            }
        }
    ) {
        Column {
            val suggestions: List<String> = listOf(
                "Star wars",
                "forrest gump",
                "3"
            )
            for (suggestion in suggestions) {
                ListItem(
                    headlineContent = { Text(suggestion) },
                    leadingContent = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_history_24),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .clickable {
                            onSearch(suggestion)
                            onActiveChange(false)
                        }
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun FilterChipPreview(modifier: Modifier = Modifier) {
    FilterChip(
        selected = true,
        onClick = {},
        label = { Text("Min Vote Average") },
        modifier = Modifier
            .padding(end = 8.dp)
    )
}