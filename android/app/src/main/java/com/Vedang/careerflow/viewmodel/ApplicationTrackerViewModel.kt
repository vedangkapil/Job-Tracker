package com.Vedang.careerflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Vedang.careerflow.data.repository.JobRepository
import com.Vedang.careerflow.model.JobApplication
import com.Vedang.careerflow.util.AppResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ApplicationTrackerUiState(
    val isLoading: Boolean = false,
    val applications: List<JobApplication> = emptyList(),
    val errorMessage: String? = null
)

class ApplicationTrackerViewModel(
    private val repository: JobRepository = JobRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(ApplicationTrackerUiState())
    val uiState: StateFlow<ApplicationTrackerUiState> = _uiState.asStateFlow()

    fun loadApplications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = repository.getApplications()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isLoading = false, applications = result.data)
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }
}
