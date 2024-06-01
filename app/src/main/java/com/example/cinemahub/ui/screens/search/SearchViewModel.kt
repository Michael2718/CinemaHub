package com.example.cinemahub.ui.screens.search

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.network.RequestStatus
import com.example.cinemahub.network.SearchRequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: CinemaHubRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SearchScreenUiState()
    )

    val uiState: StateFlow<SearchScreenUiState> = _uiState

//    fun updateQuery(query: TextFieldValue) {
//        _uiState.update {
//            it.copy(
//                query = query
//            )
//        }
//    }

    fun updateQuery(query: String) {
        _uiState.update {
            it.copy(
                query = TextFieldValue(
                    text = query,
                    selection = TextRange(query.length)
                )
            )
        }
    }

    fun setSearching(isSearching: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(100)
            _uiState.update {
                it.copy(
                    isSearching = isSearching
                )
            }
        }
    }

    fun search(query: String = uiState.value.query.text) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    searchRequestStatus = try {
                        RequestStatus.Success(
                            repository.searchMovies(
                                query,
                                minVoteAverage = uiState.value.ratingSlider.start.toInt()
                                    .toDouble(),
                                maxVoteAverage = uiState.value.ratingSlider.endInclusive.toInt()
                                    .toDouble(),
                                minReleaseDate = if (uiState.value.minReleaseYear.isEmpty()) {
                                    null
                                } else {
                                    LocalDate.parse(uiState.value.minReleaseYear + "-01-01")
                                },
                                maxReleaseDate = if (uiState.value.maxReleaseYear.isEmpty()) {
                                    null
                                } else {
                                    LocalDate.parse(uiState.value.maxReleaseYear + "-01-01")
                                },
//                                minDuration,
//                                maxDuration,
//                                minPrice,
//                                maxPrice,
                                isAdult = uiState.value.isAdult,
                            )

                        )
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
        }
    }

    fun updateRatingSlider(ratingSlider: ClosedFloatingPointRange<Float>) {
        _uiState.update {
            it.copy(
                ratingSlider = ratingSlider
            )
        }
    }

    fun updateMinReleaseYear(minReleaseYear: String) {
        _uiState.update {
            it.copy(
                minReleaseYear = minReleaseYear
            )
        }
    }

    fun updateMaxReleaseYear(maxReleaseYear: String) {
        _uiState.update {
            it.copy(
                maxReleaseYear = maxReleaseYear
            )
        }
    }

    fun updateSelectedRuntimes(selectedRuntimes: Set<String>) {
        _uiState.update {
            it.copy(
                selectedRuntimes = selectedRuntimes
            )
        }
    }

    fun updatePriceSlider(priceSlider: ClosedFloatingPointRange<Float>) {
        _uiState.update {
            it.copy(
                priceSlider = priceSlider
            )
        }
    }

    fun updateAdult(isAdult: Boolean) {
        _uiState.update {
            it.copy(
                isAdult = isAdult
            )
        }
    }
}

data class SearchScreenUiState(
    val query: TextFieldValue = TextFieldValue(),
    val isSearching: Boolean = false,
    val searchRequestStatus: SearchRequestStatus = RequestStatus.Loading(),
    // Filters
//    val minReleaseYear: Int? = null,
//    val maxReleaseYear: Int? = null,
//    val minDuration: PGInterval?,
//    val maxDuration: PGInterval?,
//    val minPrice: PGmoney?,
//    val maxPrice: PGmoney?,
//    val isAdult: Boolean? = null,

    val ratingSlider: ClosedFloatingPointRange<Float> = 0f..10f,
    val minReleaseYear: String = "",
    val maxReleaseYear: String = "",
//    val runtimes: List<FilterChip> = listOf(
//        FilterChip("1 hour or less"),
//        FilterChip("1-2 hours"),
//        FilterChip("2-3 hours"),
//        FilterChip("3-4 hours"),
//        FilterChip("4+ hours")
//    ),
    val runtimes: List<String> = listOf(
        "1 hour or less",
        "1-2 hours",
        "2-3 hours",
        "3-4 hours",
        "4+ hours"
    ),
    val selectedRuntimes: Set<String> = setOf(),

    val priceSlider: ClosedFloatingPointRange<Float> = 0f..30f,
    val isAdult: Boolean = false
)

//data class FilterChip(
//    val label: String,
//    val isSelected: Boolean = false
//)

