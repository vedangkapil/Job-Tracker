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

data class HomeUiState(
    val isLoading: Boolean = false,
    val jobs: List<Job> = emptyList(),
    val errorMessage: String? = null
)

class HomeViewModel(
    private val repository: JobRepository = JobRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadJobs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = repository.getJobs()) {
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
