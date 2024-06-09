package com.example.cinemahub.ui.screens.admin.users

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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cinemahub.R
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.ui.composables.ErrorScreen
import com.example.cinemahub.ui.composables.LoadingScreen
import com.example.cinemahub.ui.composables.UserListItem
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    uiState: UsersUiState,
    onUserClick: (Int) -> Unit,
    onRefresh: () -> Unit,
    onDelete: (Int) -> Unit,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    onClear: () -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Users",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = onLogOut
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "LogOut",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = modifier
            )
        },
        modifier = modifier
    ) {
        UsersScreenContent(
            uiState = uiState,
            pullRefreshState = pullRefreshState,
            onUserClick = onUserClick,
            onRefresh = onRefresh,
            onDeleteClick = onDelete,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            onActiveChange = onActiveChange,
            onBack = onBack,
            onClear = onClear,
            modifier = Modifier
                .padding(it)
                .padding(
                    start = 8.dp,
                    top = 0.dp,
                    end = 8.dp,
                    bottom = 0.dp
                )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreenContent(
    uiState: UsersUiState,
    onUserClick: (Int) -> Unit,
    onRefresh: () -> Unit,
    onDeleteClick: (Int) -> Unit,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    onClear: () -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
    pullRefreshState: PullToRefreshState,
    modifier: Modifier = Modifier
) {
    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            delay(1500)
            onRefresh()
            pullRefreshState.endRefresh()
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            query = uiState.query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            active = uiState.isSearching,
            enabled = true,
            onActiveChange = onActiveChange,
            placeholder = { Text(text = "Search for users") },
            leadingIcon = {
                if (uiState.isSearching) {
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
        ) {}
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .nestedScroll(pullRefreshState.nestedScrollConnection)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (uiState.usersRequestStatus) {
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
                        val users = uiState.usersRequestStatus.data
                        if (users.isEmpty()) {
                            items(1) {
                                Text("No users ")
                            }
                        } else {
                            items(items = users, key = { it.userId }) { user ->
                                UserListItem(
                                    user,
                                    onUserClick = onUserClick,
                                    onDelete = onDeleteClick,
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
}
