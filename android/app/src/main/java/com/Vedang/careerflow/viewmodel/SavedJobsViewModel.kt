package com.Vedang.careerflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Vedang.careerflow.data.repository.JobRepository
import com.Vedang.careerflow.model.Job
import com.Vedang.careerflow.util.AppResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SavedJobsUiState(
    val isLoading: Boolean = false,
    val jobs: List<Job> = emptyList(),
    val errorMessage: String? = null
)

class SavedJobsViewModel(
    private val repository: JobRepository = JobRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(SavedJobsUiState())
    val uiState: StateFlow<SavedJobsUiState> = _uiState.asStateFlow()

    fun loadSavedJobs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = repository.getTrackedJobs()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isLoading = false, jobs = result.data)
                }

                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }
}
