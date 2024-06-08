package com.example.cinemahub.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cinemahub.PreferenceManagerSingleton
import com.example.cinemahub.ui.screens.main.MainScreen
import com.example.cinemahub.ui.screens.signin.SignInScreen
import com.example.cinemahub.ui.screens.signin.SignInViewModel
import com.example.cinemahub.ui.screens.signin.SignInWelcomeScreen
import com.example.cinemahub.ui.screens.signup.SignUpScreen
import com.example.cinemahub.ui.screens.signup.SignUpViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RootAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
//    startDestination: String = Routes.SignIn.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = SignIn,
//        route = Root
    ) {
        composable<SignIn> {
            val viewModel: SignInViewModel = hiltViewModel()
            val coroutineScope = rememberCoroutineScope()

            if (viewModel.isLoggedIn()) {
                SignInWelcomeScreen(
                    PreferenceManagerSingleton.getUsername()
                )

                LaunchedEffect(Unit) {
                    delay(500)
                    if (PreferenceManagerSingleton.getAudience() == "admin") {
                        navController.navigate(MainAdmin)
                    } else {
                        navController.navigate(MainUser)
                    }
                }
            } else {
                SignInScreen(
                    viewModel = viewModel,
                    onUsernameChange = {
                        viewModel.updateUsername(it)
                    },
                    onPasswordChange = {
                        viewModel.updatePassword(it)
                    },
                    onLoginClick = {
                        coroutineScope.launch {
                            val loginSuccessful = viewModel.signIn()
                            if (loginSuccessful) {
                                if (PreferenceManagerSingleton.getAudience() == "admin") {
                                    navController.navigate(MainAdmin)
                                } else {
                                    navController.navigate(MainUser)
                                }
                            }
                        }
                    },
                    onSignUpClick = {
                        navController.navigate(SignUp)
                    },
                )
            }
        }

        composable<SignUp> {
            val viewModel: SignUpViewModel = hiltViewModel()

            val coroutineScope = rememberCoroutineScope()
            SignUpScreen(
                viewModel = viewModel,
                onSignUpClick = {
                    coroutineScope.launch {
                        val signInSuccessful = viewModel.signUp()
                        if (signInSuccessful) {
                            navController.navigate(MainUser)
                        }
                    }
                },
                onBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<MainAdmin> {
            Column {
                Text("Admin panel")
            }
        }

        composable<MainUser> {
            MainScreen(
                onLogOut = {
                    PreferenceManagerSingleton.logOut()
                    navController.navigate(SignIn) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}
