package com.example.cinemahub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cinemahub.ui.screens.main.MainScreen
import com.example.cinemahub.ui.screens.signin.SignInScreen
import com.example.cinemahub.ui.screens.signin.SignInViewModel

@Composable
fun RootAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.SignIn.route// Routes.Main.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        route = null
    ) {
        composable(Routes.SignIn.route) {
            val viewModel: SignInViewModel = hiltViewModel()
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
                    viewModel.login()
                }
            )
        }

        composable(Routes.Main.route) {
            MainScreen()
        }
    }
}
