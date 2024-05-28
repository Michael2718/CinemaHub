package com.example.cinemahub.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cinemahub.R
import com.example.cinemahub.model.api.user.User
import com.example.cinemahub.network.RequestStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) {
        Column {
            when (uiState.userRequestStatus) {
                is RequestStatus.Error -> Text(
                    (uiState.userRequestStatus as RequestStatus.Error<User>).exception.toString(),
                    modifier = Modifier.padding(it)
                )

                is RequestStatus.Loading -> Text("Profile is loading", modifier = Modifier.padding(it))
                is RequestStatus.Success -> {
                    ProfileContent(
                        (uiState.userRequestStatus as RequestStatus.Success<User>).data,
                        modifier = Modifier
                            .padding(it)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileContent(
    user: User,
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

        val userInfoItems = remember {
            listOf(
                UserInfoField("First name", user.firstName, true),
                UserInfoField("Last name", user.lastName, true),
                UserInfoField("Username", user.username, true),
                UserInfoField("Email", user.email, true),
                UserInfoField("Phone number", user.phoneNumber, false),
                UserInfoField("Birth date", user.birthDate.toString(), false)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            userInfoItems.forEach { (field, value, isEditable) ->
                UserInfoItem(
                    fieldName = field,
                    value = value,
                    isEditable = isEditable
                )
            }
        }
    }
}

@Composable
fun UserInfoItem(
    fieldName: String,
    value: String,
    isEditable: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        modifier = modifier
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            )
    ) {
        Text(
            text = fieldName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .requiredWidth(width = 100.dp)
        )
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = value,
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .requiredWidth(width = 212.dp)
            )
            if (isEditable) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = 0.9f),
                    modifier = Modifier
                        .requiredSize(size = 20.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .requiredSize(size = 20.dp)
                )
            }
        }
    }
}

data class UserInfoField(
    val fieldName: String,
    val value: String,
    val isEditable: Boolean
)

@Composable
@Preview
fun ProfileScreenPreview() {
    val viewModel: ProfileViewModel = hiltViewModel()
    ProfileScreen(
        viewModel = viewModel,
        onBack = {}
    )
}
