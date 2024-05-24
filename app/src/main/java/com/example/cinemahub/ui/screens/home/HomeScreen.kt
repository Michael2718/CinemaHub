package com.example.cinemahub.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Hello world",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}