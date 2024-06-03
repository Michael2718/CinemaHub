package com.example.cinemahub.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cinemahub.ui.composables.FilterChipGroupSingle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFiltersScreen(
    viewModel: SearchViewModel,
    onBack: () -> Unit,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Filters") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onApply
                ) {
                    Text("Apply")
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        SearchFiltersContent(
            uiState = uiState,
            onRatingSliderChange = {
                viewModel.updateRatingSlider(it)
            },
            onMinReleaseYearChange = {
                viewModel.updateMinReleaseYear(it)
            },
            onMaxReleaseYearChange = {
                viewModel.updateMaxReleaseYear(it)
            },
            runtimes = viewModel.getRuntimes(),
            onRuntimesSelectionChange = {
                viewModel.updateSelectedRuntimeIndex(it)
            },
            onPriceSliderChange = {
                viewModel.updatePriceSlider(it)
            },
            onAdultSwitchChange = {
                viewModel.updateAdult(it)
            },
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        )
    }
}

@Composable
fun SearchFiltersContent(
    uiState: SearchScreenUiState,
    onRatingSliderChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onMinReleaseYearChange: (String) -> Unit,
    onMaxReleaseYearChange: (String) -> Unit,
    runtimes: List<String>,
    onRuntimesSelectionChange: (Int) -> Unit,
    onPriceSliderChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onAdultSwitchChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Rating")
        RangeSlider(
            value = uiState.ratingSlider,
            steps = 9,
            onValueChange = onRatingSliderChange,
            valueRange = 0f..10f
        )
        if (uiState.ratingSlider.start.toInt() == uiState.ratingSlider.endInclusive.toInt()) {
            Text("⭐${uiState.ratingSlider.start.toInt()}/10")
        } else {
            Text("⭐${uiState.ratingSlider.start.toInt()}/10 - ⭐${uiState.ratingSlider.endInclusive.toInt()}/10")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Release year")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.minReleaseYear,
                onValueChange = onMinReleaseYearChange,
                singleLine = true,
                label = { Text("Min Release Year") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = "-",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            OutlinedTextField(
                value = uiState.maxReleaseYear,
                onValueChange = onMaxReleaseYearChange,
                singleLine = true,
                label = { Text("Max Release Year") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Runtime")
        FilterChipGroupSingle(
            items = runtimes,
            selectedItemIndex = uiState.selectedRuntimeIndex,
            onSelectionChange = onRuntimesSelectionChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Price range")
        RangeSlider(
            value = uiState.priceSlider,
            steps = 29,
            onValueChange = onPriceSliderChange,
            valueRange = 0f..30f
        )
        Text("$${uiState.priceSlider.start.toInt()} - $${uiState.priceSlider.endInclusive.toInt()}")
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Adult Content")
            Switch(
                checked = uiState.isAdult,
                onCheckedChange = onAdultSwitchChange
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}



//@Preview(showBackground = true)
//@Composable
//fun PreviewFilterScreen() {
//    CinemaHubTheme {
//        Surface {
//            SearchFilterScreen(
//                viewModel = hiltViewModel(),
//                onBack = { }
//            )
//        }
//    }
//}