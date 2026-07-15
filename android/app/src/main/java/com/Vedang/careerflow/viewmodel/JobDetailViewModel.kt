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

data class JobDetailUiState(
    val isLoading: Boolean = false,
    val isUpdatingTracking: Boolean = false,
    val job: Job? = null,
    val message: String? = null,
    val errorMessage: String? = null
)

class JobDetailViewModel(
    private val repository: JobRepository = JobRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(JobDetailUiState())
    val uiState: StateFlow<JobDetailUiState> = _uiState.asStateFlow()

    fun loadJob(jobId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = repository.getJob(jobId)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isLoading = false, job = result.data)
                }

                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }

    fun updateTrackedState() {
        val job = _uiState.value.job ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isUpdatingTracking = true, message = null, errorMessage = null) }
            val result = if (job.isTracked) {
                repository.untrackJob(job.id)
            } else {
                repository.trackJob(job.id)
            }

            when (result) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        isUpdatingTracking = false,
                        job = job.copy(isTracked = !job.isTracked),
                        message = result.data
                    )
                }

                is AppResult.Error -> _uiState.update {
                    it.copy(isUpdatingTracking = false, errorMessage = result.message)
                }
            }
        }
    }
}
