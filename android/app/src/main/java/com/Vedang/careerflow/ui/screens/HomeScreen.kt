package com.Vedang.careerflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.Vedang.careerflow.ui.components.EmptyContent
import com.Vedang.careerflow.ui.components.ErrorContent
import com.Vedang.careerflow.ui.components.JobCard
import com.Vedang.careerflow.ui.components.LoadingContent
import com.Vedang.careerflow.viewmodel.HomeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onRefresh: () -> Unit,
    onFindJobs: () -> Unit,
    onSavedJobs: () -> Unit,
    onJobClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("CareerFlow") },
                actions = {
                    TextButton(onClick = onSavedJobs) {
                        Text("Saved")
                    }
                }
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
                title = "Your job list is ready",
                description = "Find a role to discover opportunities and save the ones you like.",
                actionLabel = "Find jobs",
                onAction = onFindJobs,
                modifier = Modifier.padding(innerPadding)
            )

            else -> JobList(
                uiState = uiState,
                contentPadding = innerPadding,
                onRefresh = onRefresh,
                onFindJobs = onFindJobs,
                onJobClick = onJobClick
            )
        }
    }
}

@Composable
private fun JobList(
    uiState: HomeUiState,
    contentPadding: PaddingValues,
    onRefresh: () -> Unit,
    onFindJobs: () -> Unit,
    onJobClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = contentPadding.calculateTopPadding() + 12.dp,
            end = 20.dp,
            bottom = contentPadding.calculateBottomPadding() + 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text("Your opportunities", style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = "Review saved results or start a fresh search.",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(
                    onClick = onFindJobs,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Find new jobs")
                }
                if (uiState.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }
                uiState.errorMessage?.let { message ->
                    TextButton(onClick = onRefresh) {
                        Text("$message Tap to retry.")
                    }
                }
            }
        }
        items(uiState.jobs, key = { it.id }) { job ->
            JobCard(job = job, onClick = { onJobClick(job.id) })
        }
    }
}
