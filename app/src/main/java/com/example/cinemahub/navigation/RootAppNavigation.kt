package com.example.cinemahub.navigation

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
import com.example.cinemahub.ui.screens.main.MainViewModel
import com.example.cinemahub.ui.screens.signin.SignInScreen
import com.example.cinemahub.ui.screens.signin.SignInViewModel
import com.example.cinemahub.ui.screens.signin.SignInWelcomeScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RootAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.SignIn.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        route = null
    ) {
        composable(Routes.SignIn.route) {
            val viewModel: SignInViewModel = hiltViewModel()
            val coroutineScope = rememberCoroutineScope()

            if (viewModel.isLoggedIn()) {
                SignInWelcomeScreen(
                    PreferenceManagerSingleton.getUsername()!!
                )

                LaunchedEffect(Unit) {
                    delay(500)
                    navController.navigate(Routes.Main.route) {
                        popUpTo(0)
                    }
                }
            } else {
                SignInScreen(
                    viewModel = viewModel,
                    onBack = {

                    },
                    onUsernameChange = {
                        viewModel.updateUsername(it)
                    },
                    onPasswordChange = {
                        viewModel.updatePassword(it)
                    },
                    onLoginClick = {
                        coroutineScope.launch {
                            val loginSuccessful = viewModel.login()
                            if (loginSuccessful) {
                                navController.navigate(Routes.Main.route) {
                                    popUpTo(0)
                                }
                            }
                        }
                    }
                )
            }
        }

        composable(Routes.Main.route) {
            val viewModel: MainViewModel = hiltViewModel()
            MainScreen(viewModel = viewModel)
        }
    }
}
