package com.example.cinemahub.ui.screens.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.ui.theme.CinemaHubTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    onBack: () -> Unit,
    onLoginClick: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sign In") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = stringResource(R.string.back)
//                        )
//                    }
//                }
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .padding(it)
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
                verticalArrangement = Arrangement.spacedBy(16.dp),
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
                Text(
                    "Forgot Password?",
                    color = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(vertical = 4.dp)
                        .clickable {

                        }
                )
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
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Don't have an account?")
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Sign Up",
                        color = MaterialTheme.colorScheme.inversePrimary,
                        modifier = Modifier.clickable {

                        }
                    )
                }

                when (uiState.tokenRequestStatus) {
                    is RequestStatus.Error -> Text("Invalid credentials", color = MaterialTheme.colorScheme.error)

                    is RequestStatus.Loading -> {}

                    is RequestStatus.Success -> Text("Login Successful!", color = Color.Green)
                }
            }
        }
    }
}

@Composable
fun CommonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)?,
    imeAction: ImeAction,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        label = label,
        onValueChange = onValueChange,
        shape = CircleShape,
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = null,
                modifier = Modifier.padding(start = 4.dp)
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction
        ),
        modifier = modifier
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)?,
    imeAction: ImeAction,
    modifier: Modifier = Modifier
) {
//    var password by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = value,
        label = label,
        onValueChange = onValueChange,
        shape = CircleShape,
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
        modifier = modifier
    )
}

@Composable
@Preview
private fun SignInPreview() {
    CinemaHubTheme {
//        SignInScreen(
//            onBack = {},
//            onLoginClick = {}
//        )
    }
}