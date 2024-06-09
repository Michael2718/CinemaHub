package com.example.cinemahub.ui.screens.user.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.ui.composables.CommonTextField
import com.example.cinemahub.ui.composables.ErrorScreen
import com.example.cinemahub.ui.composables.LoadingScreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    IconButton(onClick = onLogOut) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "LogOut",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.error
                ),
            )
        },
        modifier = modifier
    ) {
        ProfileScreenContent(
            viewModel = viewModel,
            uiState = uiState,
            pullRefreshState = pullRefreshState,
            onRefresh = {
                viewModel.fetchUser()
            },
            onSaveClick = {
                focusManager.clearFocus()
                viewModel.updateUser()
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
fun ProfileScreenContent(
    viewModel: ProfileViewModel,
    uiState: ProfileScreenUiState,
    pullRefreshState: PullToRefreshState,
    onRefresh: () -> Unit,
    onSaveClick: () -> Unit,
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
            when (uiState.userRequestStatus) {
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
//                    val user = requestStatus.data
                    ProfileForm(
                        viewModel = viewModel,
                        uiState = uiState,
//                        user = user,
                        onSaveClick = onSaveClick,
                        modifier = Modifier
                            .fillMaxSize()
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
fun ProfileForm(
    viewModel: ProfileViewModel,
    uiState: ProfileScreenUiState,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .requiredSize(size = 64.dp)
                    .clip(shape = CircleShape)
                    .background(color = Color(0xfff6f6f6))
            ) {
                Image(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Rectangle 1",
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Text(
                text = "Edit profile image",
                color = Color(0xff0d99ff),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CommonTextField(
                value = uiState.firstName,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("First name") },
                imeAction = ImeAction.Default,
                leadingIcon = Icons.Default.Face,
                isEnabled = true
            )
            CommonTextField(
                value = uiState.lastName,
                onValueChange = { viewModel.updateLastName(it) },
                label = { Text("Last name") },
                imeAction = ImeAction.Default,
                leadingIcon = Icons.Default.Face,
                isEnabled = true
            )
            CommonTextField(
                value = uiState.username,
                onValueChange = { viewModel.updateUsername(it) },
                label = { Text("Username") },
                imeAction = ImeAction.Default,
                leadingIcon = Icons.Default.AccountCircle,
                isEnabled = true
            )
            CommonTextField(
                value = uiState.email,
                onValueChange = { viewModel.updateEmail(it) },
                label = { Text("Email") },
                imeAction = ImeAction.Default,
                leadingIcon = Icons.Default.Email,
                isEnabled = true
            )
            CommonTextField(
                value = uiState.phoneNumber,
                onValueChange = { },
                label = { Text("Phone number") },
                imeAction = ImeAction.Default,
                leadingIcon = Icons.Default.Phone,
                isEnabled = false
            )
            CommonTextField(
                value = uiState.birthdate,
                onValueChange = { },
                label = { Text("Birth date") },
                imeAction = ImeAction.Default,
                leadingIcon = Icons.Default.DateRange,
                isEnabled = false
            )
        }

        Button(
            onClick = onSaveClick
        ) {
            Text("Save Profile")
        }
        when (uiState.updateRequestStatus) {
            is RequestStatus.Error -> {
                Text("Invalid user info", color = MaterialTheme.colorScheme.error)
            }

            is RequestStatus.Loading -> {}

            is RequestStatus.Success -> {
                Text("User profile changed successfully", color = Color.Green)
                LaunchedEffect(Unit) {
                    delay(4000)
                    viewModel.setUpdateRequestStatus()
                }
            }
        }
    }
}

//data class UserInfoField(
//    val fieldName: String,
//    val value: String,
//    val isEditable: Boolean,
//    val icon: ImageVector
//)

//@Composable
//@Preview(showBackground = true)
//fun ProfileFormPreview() {
//    CinemaHubTheme {
//        ProfileForm(
//            user = User(
//                userId = 1,
//                username = "JohnDoe",
//                firstName = "John",
//                lastName = "Doe",
//                email = "example@gmail.com",
//                phoneNumber = "1234567890",
//                birthDate = LocalDate.parse("2000-01-01"),
//            ),
//            onSaveClick = {},
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//}
