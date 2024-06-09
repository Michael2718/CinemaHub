package com.example.cinemahub.ui.screens.admin.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.ui.composables.CommonTextField
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMovieScreen(
    uiState: AddMovieUiState,
    onBack: () -> Unit,
    onClear: () -> Unit,
    onSaveClick: () -> Unit,
//    onRefresh: () -> Unit,
    updateMovieId: (String) -> Unit,
    updateTitle: (String) -> Unit,
    updateReleaseDate: (String) -> Unit,
    updateDuration: (String) -> Unit,
    updatePlot: (String) -> Unit,
    updateAdult: (Boolean) -> Unit,
    updatePrice: (String) -> Unit,
    updatePrimaryImageUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
//    val pullRefreshState = rememberPullToRefreshState()
//    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Movies",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = onClear
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear movie fields"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = modifier
            )
        },
        modifier = modifier
    ) {
        AddMovieForm(
            uiState = uiState,
            onSaveClick = onSaveClick,
            updateMovieId = updateMovieId,
            updateTitle = updateTitle,
            updateReleaseDate = updateReleaseDate,
            updateDuration = updateDuration,
            updatePlot = updatePlot,
            updateAdult = updateAdult,
            updatePrice = updatePrice,
            updatePrimaryImageUrl = updatePrimaryImageUrl,
            modifier = Modifier
                .padding(it)
                .padding(
                    start = 16.dp,
                    top = 0.dp,
                    end = 16.dp,
                    bottom = 0.dp
                )
        )
//        AddMovieScreenContent(
//            uiState = uiState,
//            onSaveClick = onSaveClick,
////            onRefresh = onRefresh,
//            updateMovieId = updateMovieId,
//            updateTitle = updateTitle,
//            updateReleaseDate = updateReleaseDate,
//            updateDuration = updateDuration,
//            updatePlot = updatePlot,
//            updateAdult = updateAdult,
//            updatePrice = updatePrice,
//            updatePrimaryImageUrl = updatePrimaryImageUrl,
////            pullRefreshState = pullRefreshState,
//            modifier = Modifier
//                .padding(it)
//                .padding(
//                    start = 16.dp,
//                    top = 0.dp,
//                    end = 16.dp,
//                    bottom = 0.dp
//                )
//        )
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddMovieScreenContent(
//    uiState: AddMovieUiState,
////    pullRefreshState: PullToRefreshState,
//    onSaveClick: () -> Unit,
////    onRefresh: () -> Unit,
//    updateMovieId: (String) -> Unit,
//    updateTitle: (String) -> Unit,
//    updateReleaseDate: (String) -> Unit,
//    updateDuration: (String) -> Unit,
//    updatePlot: (String) -> Unit,
//    updateAdult: (Boolean) -> Unit,
//    updatePrice: (String) -> Unit,
//    updatePrimaryImageUrl: (String) -> Unit,
//    modifier: Modifier = Modifier
//) {
////    if (pullRefreshState.isRefreshing) {
////        LaunchedEffect(Unit) {
////            delay(1500)
////            onRefresh()
////            pullRefreshState.endRefresh()
////        }
////    }
//
//    AddMovieForm(
//        uiState = uiState,
//        onSaveClick = onSaveClick,
//        updateMovieId = updateMovieId,
//        updateTitle = updateTitle,
//        updateReleaseDate = updateReleaseDate,
//        updateDuration = updateDuration,
//        updatePlot = updatePlot,
//        updateAdult = updateAdult,
//        updatePrice = updatePrice,
//        updatePrimaryImageUrl = updatePrimaryImageUrl
//    )
//
////    Box(
////        modifier = modifier
////            .nestedScroll(pullRefreshState.nestedScrollConnection)
////    ) {
////        AddMovieForm(
////            uiState = uiState,
////            onSaveClick = onSaveClick,
////            updateMovieId = updateMovieId,
////            updateTitle = updateTitle,
////            updateReleaseDate = updateReleaseDate,
////            updateDuration = updateDuration,
////            updatePlot = updatePlot,
////            updateAdult = updateAdult,
////            updatePrice = updatePrice,
////            updatePrimaryImageUrl = updatePrimaryImageUrl
////        )
////
////        PullToRefreshContainer(
////            modifier = Modifier
////                .align(Alignment.TopCenter),
////            state = pullRefreshState
////        )
////    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMovieForm(
    uiState: AddMovieUiState,
    onSaveClick: () -> Unit,
    updateMovieId: (String) -> Unit,
    updateTitle: (String) -> Unit,
    updateReleaseDate: (String) -> Unit,
    updateDuration: (String) -> Unit,
    updatePlot: (String) -> Unit,
    updateAdult: (Boolean) -> Unit,
    updatePrice: (String) -> Unit,
    updatePrimaryImageUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        CommonTextField(
            value = uiState.movieId,
            onValueChange = { updateMovieId(it) },
            label = { Text("MovieId") },
            imeAction = ImeAction.Next,
            shape = RectangleShape,
            modifier = Modifier.fillMaxWidth(),
        )
        CommonTextField(
            value = uiState.title,
            onValueChange = { updateTitle(it) },
            label = { Text("Title") },
            imeAction = ImeAction.Next,
            shape = RectangleShape,
            modifier = Modifier.fillMaxWidth(),
        )

        var dialogOpened by remember { mutableStateOf(false) }
        CommonTextField(
            value = uiState.releaseDate,
            onValueChange = { updateReleaseDate(it) },
            label = { Text("Release date") },
            imeAction = ImeAction.Done,
            leadingIcon = Icons.Default.DateRange,
            errorMessage = "Release date should be in format 1999-01-01",
            onIconClick = {
                dialogOpened = dialogOpened.not()
            },
            shape = RectangleShape,
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
                            updateReleaseDate(
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
        CommonTextField(
            value = uiState.duration,
            onValueChange = { updateDuration(it) }, // TODO
            label = { Text("Duration") },
            imeAction = ImeAction.Next,
            shape = RectangleShape,
            modifier = Modifier.fillMaxWidth(),
        )
        CommonTextField(
            value = uiState.plot,
            onValueChange = { updatePlot(it) },
            label = { Text("Plot") },
            imeAction = ImeAction.Next,
            shape = RectangleShape,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Adult")
            Switch(
                checked = uiState.isAdult,
                onCheckedChange = updateAdult
            )
        }
        CommonTextField(
            value = uiState.price,
            onValueChange = { updatePrice(it) },
            label = { Text("Price") },
            imeAction = ImeAction.Next,
            shape = RectangleShape,
            modifier = Modifier.fillMaxWidth(),
        )
        CommonTextField(
            value = uiState.primaryImageUrl,
            onValueChange = { updatePrimaryImageUrl(it) },
            label = { Text("PrimaryImageUrl") },
            imeAction = ImeAction.Next,
            shape = RectangleShape,
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save", style = MaterialTheme.typography.titleLarge)
        }
        when (uiState.addedMovieRequestStatus) {
            is RequestStatus.Error -> Text(
                "Invalid movie info",
                color = MaterialTheme.colorScheme.error
            )

            is RequestStatus.Loading -> {}

            is RequestStatus.Success -> Text(
                "Add Successful!",
                color = Color.Green
            )
        }
    }
}

@Preview
@Composable
private fun AddMovieScreenPreview() {

}