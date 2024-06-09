package com.example.cinemahub.ui.screens.user.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.ui.composables.CommonTextField
import com.example.cinemahub.ui.composables.PasswordTextField
import com.example.cinemahub.ui.theme.CinemaHubTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onSignUpClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sign Up") },
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
    ) {
        SignUpContent(
            viewModel = viewModel,
            uiState = uiState,
            onSignUpClick = onSignUpClick,
            modifier = Modifier
                .padding(it)
        )
    }
}

@Composable
fun SignUpContent(
    viewModel: SignUpViewModel,
    uiState: SignUpUiState,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        SignUpForm(
            viewModel = viewModel,
            uiState = uiState,
            onSignUpClick = onSignUpClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpForm(
    viewModel: SignUpViewModel,
    uiState: SignUpUiState,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        CommonTextField(
            value = uiState.username,
            onValueChange = { viewModel.updateUsername(it) },
            label = { Text("Username") },
            imeAction = ImeAction.Next,
            leadingIcon = Icons.Filled.AccountCircle,
            isValid = uiState.isUsernameValid,
            errorMessage = "Username should not be empty",
            modifier = Modifier.fillMaxWidth(),
        )
        PasswordTextField(
            value = uiState.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Password") },
            imeAction = ImeAction.Next,
            isValid = uiState.isPasswordValid,
            errorMessage = "Password should be at least 8 characters long",
            modifier = Modifier.fillMaxWidth()
        )
        NameLastNameRow(
            uiState = uiState,
            onNameChange = { viewModel.updateName(it) },
            onLastNameChange = { viewModel.updateLastName(it) }
        )
        CommonTextField(
            value = uiState.email,
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text("Email") },
            imeAction = ImeAction.Next,
            leadingIcon = Icons.Default.Email,
            isValid = uiState.isEmailValid,
            errorMessage = "Email address is not valid",
            modifier = Modifier.fillMaxWidth()
        )
        CommonTextField(
            value = uiState.phoneNumber,
            onValueChange = { viewModel.updatePhoneNumber(it) },
            label = { Text("Phone Number") },
            imeAction = ImeAction.Next,
            leadingIcon = Icons.Default.Phone,
            isValid = uiState.isPhoneNumberValid,
            errorMessage = "Phone number should be at least 10 digits",
            modifier = Modifier.fillMaxWidth()
        )

        var dialogOpened by remember { mutableStateOf(false) }

        CommonTextField(
            value = uiState.birthdate,
            onValueChange = { viewModel.updateBirthdate(it) },
            label = { Text("Birthdate") },
            imeAction = ImeAction.Done,
            leadingIcon = Icons.Default.DateRange,
            isValid = uiState.isBirthdateValid,
            errorMessage = "Birthdate should be in format 1999-01-01 and before today's date",
            onIconClick = {
                dialogOpened = dialogOpened.not()
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (dialogOpened) {
            val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
            val confirmEnabled = remember { true }
            DatePickerDialog(
                onDismissRequest = { dialogOpened = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            dialogOpened = false
                            val dateMillis = datePickerState.selectedDateMillis
                            viewModel.updateBirthdate(
                                if (dateMillis != null) {
                                    Instant.fromEpochMilliseconds(dateMillis).toLocalDateTime(
                                        TimeZone.UTC
                                    ).date.toString()
                                } else {
                                    ""
                                }
                            )
                        },
                        enabled = confirmEnabled
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            dialogOpened = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                )
            }
        }

        Button(
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up", style = MaterialTheme.typography.titleLarge)
        }
        when (uiState.signUpRequestStatus) {
            is RequestStatus.Error -> Text(
                "Sign up failed",
                color = MaterialTheme.colorScheme.error
            )

            is RequestStatus.Loading -> {}

            is RequestStatus.Success -> Text(
                "Sign up Successful!",
                color = Color.Green
            )
        }
    }
}

@Composable
fun NameLastNameRow(
    uiState: SignUpUiState,
    onNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        CommonTextField(
            value = uiState.name,
            onValueChange = onNameChange,
            label = { Text("Name") },
            imeAction = ImeAction.Next,
            leadingIcon = Icons.Default.Face,
            isValid = uiState.isNameValid,
            errorMessage = "Name should not be empty",
            modifier = Modifier.weight(1f)
        )
        CommonTextField(
            value = uiState.lastName,
            onValueChange = onLastNameChange,
            label = { Text("Last Name") },
            imeAction = ImeAction.Next,
            leadingIcon = Icons.Default.Face,
            isValid = uiState.isLastNameValid,
            errorMessage = "Last Name should not be empty",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
@Preview
private fun SignUpPreview() {
    val viewModel: SignUpViewModel = hiltViewModel()
    CinemaHubTheme {
        SignUpScreen(
            viewModel = viewModel,
            onSignUpClick = {},
            onBack = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun DatePickerSample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Pre-select a date for January 4, 2020
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = 1578096000000)
        DatePicker(
            state = datePickerState,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            "Selected date timestamp: ${datePickerState.selectedDateMillis ?: "no selection"}",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}


@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@Preview(showBackground = true)
@Composable
fun ContentDatePickerPopup() {
    // Decoupled snackbar host state from scaffold state for demo purposes.
    val dateResult by remember { mutableStateOf("Date Picker") }
    val openDialog = remember { mutableStateOf(true) }

    CinemaHubTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("DatePicker") })
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 100.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            openDialog.value = true
                        }
                    ) {
                        Text(dateResult)
                    }
                }

            }
        )
    }
}
