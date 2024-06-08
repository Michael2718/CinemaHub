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
import com.example.cinemahub.navigation.routes.FavoritesGraph
import com.example.cinemahub.navigation.routes.HomeGraph
import com.example.cinemahub.navigation.routes.MainAdmin
import com.example.cinemahub.navigation.routes.MainUser
import com.example.cinemahub.navigation.routes.MoviesGraph
import com.example.cinemahub.navigation.routes.ProfileGraph
import com.example.cinemahub.navigation.routes.SearchGraph
import com.example.cinemahub.navigation.routes.SignIn
import com.example.cinemahub.navigation.routes.SignUp
import com.example.cinemahub.navigation.routes.UsersGraph
import com.example.cinemahub.ui.screens.MainScreen
import com.example.cinemahub.ui.screens.user.signin.SignInScreen
import com.example.cinemahub.ui.screens.user.signin.SignInViewModel
import com.example.cinemahub.ui.screens.user.signin.SignInWelcomeScreen
import com.example.cinemahub.ui.screens.user.signup.SignUpScreen
import com.example.cinemahub.ui.screens.user.signup.SignUpViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RootAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = SignIn
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
            MainScreen(
                navGraphs = listOf(
                    MoviesGraph,
//                    ReviewsGraph,
                    UsersGraph
                ),
                isAdmin = true,
                onLogOut = {
                    PreferenceManagerSingleton.logOut()
                    navController.navigate(SignIn) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable<MainUser> {
            MainScreen(
                navGraphs = listOf(
                    HomeGraph,
                    SearchGraph,
                    FavoritesGraph,
                    ProfileGraph,
                ),
                isAdmin = false,
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
