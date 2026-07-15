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

data class SearchUiState(
    val keyword: String = "",
    val location: String = "",
    val isLoading: Boolean = false,
    val jobs: List<Job> = emptyList(),
    val message: String? = null,
    val errorMessage: String? = null
)

class SearchViewModel(
    private val repository: JobRepository = JobRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun updateKeyword(keyword: String) {
        _uiState.update { it.copy(keyword = keyword, errorMessage = null) }
    }

    fun updateLocation(location: String) {
        _uiState.update { it.copy(location = location, errorMessage = null) }
    }

    fun findNewJobs() {
        val search = _uiState.value
        val keyword = search.keyword.trim()
        val location = search.location.trim()

        if (keyword.isBlank() || location.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Enter both a job title and location.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null, errorMessage = null) }
            when (val result = repository.scrapeJobs(keyword, location)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        jobs = result.data.jobs,
                        message = result.data.message
                    )
                }

                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }
}
