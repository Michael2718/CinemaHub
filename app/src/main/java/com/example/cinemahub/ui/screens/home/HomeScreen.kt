package com.example.cinemahub.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cinemahub.R
import com.example.cinemahub.model.api.movie.Movie
import com.example.cinemahub.network.RequestStatus

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            HomeScreenTopBar()
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            when (uiState.moviesRequestStatus) {
                is RequestStatus.Error -> Text("Error")
                is RequestStatus.Loading -> Text("This is Home screen! Loading....")
                is RequestStatus.Success -> {
                    Text(
                        (uiState.moviesRequestStatus as RequestStatus.Success<List<Movie>>).data[32].title
                    )
                }
            }
        }
    }



    Spacer(modifier = Modifier.height(8.dp))

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        modifier = modifier
    )
}
