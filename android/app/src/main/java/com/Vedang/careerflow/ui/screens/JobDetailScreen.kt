package com.Vedang.careerflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.Vedang.careerflow.model.Job
import com.Vedang.careerflow.ui.components.ErrorContent
import com.Vedang.careerflow.ui.components.LoadingContent
import com.Vedang.careerflow.ui.components.BackNavigationButton
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
            TopAppBar(
                title = { Text("Job details", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { BackNavigationButton(onClick = onBack) }
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
            top = contentPadding.calculateTopPadding() + 8.dp,
            end = 20.dp,
            bottom = contentPadding.calculateBottomPadding() + 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CompanyMark(company = job.company)
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(
                                text = job.company,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = job.source.replaceFirstChar { it.uppercase() },
                                modifier = Modifier.padding(top = 2.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Text(
                        text = job.title,
                        modifier = Modifier.padding(top = 20.dp),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = job.location,
                        modifier = Modifier.padding(top = 10.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    job.salary?.takeIf { it.isNotBlank() }?.let { salary ->
                        Text(
                            text = salary,
                            modifier = Modifier.padding(top = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
        item {
            Text("About this role", style = MaterialTheme.typography.titleLarge)
            Text(
                text = job.description?.takeIf { it.isNotBlank() }
                    ?: "No job description was provided.",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    Text(
                        when {
                            uiState.isUpdatingTracking -> "Updating…"
                            job.isTracked -> "Remove from saved jobs"
                            else -> "Save to shortlist"
                        }
                    )
                }
                uiState.message?.let { message ->
                    Text(
                        text = message,
                        modifier = Modifier.padding(top = 12.dp),
                        color = MaterialTheme.colorScheme.secondary,
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

@Composable
private fun CompanyMark(company: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(MaterialTheme.colorScheme.onPrimaryContainer, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = company.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primaryContainer
        )
    }
}
