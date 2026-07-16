package com.Vedang.careerflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.Vedang.careerflow.ui.components.EmptyContent
import com.Vedang.careerflow.ui.components.ErrorContent
import com.Vedang.careerflow.ui.components.JobCard
import com.Vedang.careerflow.ui.components.LoadingContent
import com.Vedang.careerflow.ui.components.BackNavigationButton
import com.Vedang.careerflow.viewmodel.SavedJobsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedJobsScreen(
    uiState: SavedJobsUiState,
    onRefresh: () -> Unit,
    onFindJobs: () -> Unit,
    onBack: () -> Unit,
    onJobClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved jobs", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { BackNavigationButton(onClick = onBack) },
                actions = { OutlinedButton(onClick = onRefresh) { Text("Refresh") } }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading && uiState.jobs.isEmpty() -> LoadingContent(
                modifier = Modifier.padding(innerPadding)
            )

            uiState.errorMessage != null && uiState.jobs.isEmpty() -> ErrorContent(
                message = uiState.errorMessage,
                onRetry = onRefresh,
                modifier = Modifier.padding(innerPadding)
            )

            uiState.jobs.isEmpty() -> EmptyContent(
                title = "Your shortlist is empty",
                description = "Save promising roles and they’ll be ready here when you are.",
                actionLabel = "Explore jobs",
                onAction = onFindJobs,
                modifier = Modifier.padding(innerPadding)
            )

            else -> SavedJobList(
                uiState = uiState,
                contentPadding = innerPadding,
                onJobClick = onJobClick
            )
        }
    }
}

@Composable
private fun SavedJobList(
    uiState: SavedJobsUiState,
    contentPadding: PaddingValues,
    onJobClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = contentPadding.calculateTopPadding() + 8.dp,
            end = 20.dp,
            bottom = contentPadding.calculateBottomPadding() + 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text("Your shortlist", style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = "${uiState.jobs.size} roles worth coming back to.",
                    modifier = Modifier.padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                uiState.errorMessage?.let { message ->
                    Text(
                        text = message,
                        modifier = Modifier.padding(top = 10.dp),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        items(uiState.jobs, key = { it.id }) { job ->
            JobCard(job = job, onClick = { onJobClick(job.id) })
        }
    }
}
