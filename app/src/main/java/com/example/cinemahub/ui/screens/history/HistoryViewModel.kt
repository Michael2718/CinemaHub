package com.example.cinemahub.ui.screens.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemahub.data.CinemaHubRepository
import com.example.cinemahub.network.HistoryRequestStatus
import com.example.cinemahub.network.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CinemaHubRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        HistoryScreenUiState(
            userId = savedStateHandle.get<Int>("userId")?.toInt() ?: -1
        )
    )

    val uiState: StateFlow<HistoryScreenUiState> = _uiState

    init {
        fetchHistory()
    }

    fun fetchHistory(userId: Int = uiState.value.userId) {
        _uiState.update {
            it.copy(
                historyRequestStatus = RequestStatus.Loading()
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    historyRequestStatus = try {
                        RequestStatus.Success(repository.getHistory(userId))
                    } catch (e: Exception) {
                        RequestStatus.Error(e)
                    }
                )
            }
        }
    }
}

data class HistoryScreenUiState(
    val historyRequestStatus: HistoryRequestStatus = RequestStatus.Loading(),
    val userId: Int
)
