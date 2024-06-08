package com.example.cinemahub.ui.screens.user.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.ui.composables.CommonTextField
import com.example.cinemahub.ui.composables.PasswordTextField
import com.example.cinemahub.ui.theme.CinemaHubTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sign In") },
            )
        },
        modifier = modifier
    ) {
        SignInContent(
            uiState = uiState,
            onLoginClick = onLoginClick,
            onSignUpClick = onSignUpClick,
            onUsernameChange = onUsernameChange,
            onPasswordChange = onPasswordChange,
            modifier = Modifier
                .padding(it)
        )
    }
}

@Composable
fun SignInContent(
    uiState: SignInUiState,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Sign In to your account",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CommonTextField(
                value = uiState.username,
                onValueChange = onUsernameChange,
                label = {
                    Text("Username")
                },
                imeAction = ImeAction.Next,
                leadingIcon = Icons.Default.AccountCircle,
                modifier = Modifier
                    .fillMaxWidth()
            )
            PasswordTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = {
                    Text("Password")
                },
                imeAction = ImeAction.Done,
                modifier = Modifier
                    .fillMaxWidth()
            )
            TextButton(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Text(
                    "Forgot Password?",
                    color = MaterialTheme.colorScheme.inversePrimary,
                )
            }
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    "Sign In",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Don't have an account?")
                TextButton(
                    onClick = onSignUpClick
                ) {
                    Text(
                        "Sign Up",
                        color = MaterialTheme.colorScheme.inversePrimary,
                    )
                }
            }

            when (uiState.tokenRequestStatus) {
                is RequestStatus.Error -> Text(
                    "Invalid credentials",
                    color = MaterialTheme.colorScheme.error
                )

                is RequestStatus.Loading -> {}

                is RequestStatus.Success -> Text("Login Successful!", color = Color.Green)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SignInPreview() {
    CinemaHubTheme {
        SignInContent(
            uiState = SignInUiState(),
            onLoginClick = {},
            onSignUpClick = {},
            onUsernameChange = {},
            onPasswordChange = {}
        )
    }
}