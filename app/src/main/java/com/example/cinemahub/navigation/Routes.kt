package com.example.cinemahub.navigation

sealed class Routes(val route: String) {
    data object Home : Routes("home")
}