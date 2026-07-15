package com.Vedang.careerflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.Vedang.careerflow.model.Job
import com.Vedang.careerflow.ui.components.ErrorContent
import com.Vedang.careerflow.ui.components.LoadingContent
import com.Vedang.careerflow.viewmodel.JobDetailUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    uiState: JobDetailUiState,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    onUpdateTracking: () -> Unit,
    onApply: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Job details") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading && uiState.job == null -> LoadingContent(
                modifier = Modifier.padding(innerPadding)
            )

            uiState.errorMessage != null && uiState.job == null -> ErrorContent(
                message = uiState.errorMessage,
                onRetry = onRefresh,
                modifier = Modifier.padding(innerPadding)
            )

            uiState.job != null -> JobDetailContent(
                job = uiState.job,
                uiState = uiState,
                contentPadding = innerPadding,
                onUpdateTracking = onUpdateTracking,
                onApply = onApply
            )
        }
    }
}

@Composable
private fun JobDetailContent(
    job: Job,
    uiState: JobDetailUiState,
    contentPadding: PaddingValues,
    onUpdateTracking: () -> Unit,
    onApply: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = contentPadding.calculateTopPadding() + 12.dp,
            end = 20.dp,
            bottom = contentPadding.calculateBottomPadding() + 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(job.title, style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = job.company,
                    modifier = Modifier.padding(top = 6.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = job.location,
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = job.source.replaceFirstChar { it.uppercase() },
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                job.salary?.takeIf { it.isNotBlank() }?.let { salary ->
                    Text(
                        text = salary,
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        item {
            Text("About this role", style = MaterialTheme.typography.titleLarge)
            Text(
                text = job.description?.takeIf { it.isNotBlank() }
                    ?: "No job description was provided.",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        item {
            Column {
                Button(
                    onClick = { onApply(job.jobUrl) },
                    enabled = job.jobUrl.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply on ${job.source.replaceFirstChar { it.uppercase() }}")
                }
                OutlinedButton(
                    onClick = onUpdateTracking,
                    enabled = !uiState.isUpdatingTracking,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    val label = when {
                        uiState.isUpdatingTracking -> "Updating…"
                        job.isTracked -> "Remove from saved jobs"
                        else -> "Save job"
                    }
                    Text(label)
                }
                uiState.message?.let { message ->
                    Text(
                        text = message,
                        modifier = Modifier.padding(top = 12.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                uiState.errorMessage?.let { message ->
                    Text(
                        text = message,
                        modifier = Modifier.padding(top = 12.dp),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
