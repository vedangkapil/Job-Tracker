package com.Vedang.careerflow.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.Vedang.careerflow.ui.components.EmptyContent
import com.Vedang.careerflow.ui.components.ErrorContent
import com.Vedang.careerflow.ui.components.JobCard
import com.Vedang.careerflow.ui.components.LoadingContent
import com.Vedang.careerflow.ui.components.BrandLockup
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
            TopAppBar(
                title = {
                    BrandLockup()
                },
                actions = {
                    OutlinedButton(onClick = onSavedJobs) {
                        Text("Saved jobs")
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
                title = "Ready when you are",
                description = "Find a role to discover opportunities and keep your favourites close.",
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
            top = contentPadding.calculateTopPadding() + 8.dp,
            end = 20.dp,
            bottom = contentPadding.calculateBottomPadding() + 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            OpportunityHero(onFindJobs = onFindJobs)
        }
        item {
            Column {
                Text("Latest search results", style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = "${uiState.jobs.size} opportunities from your latest search",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (uiState.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                uiState.errorMessage?.let { message ->
                    TextButton(onClick = onRefresh, modifier = Modifier.padding(top = 6.dp)) {
                        Text("$message  Retry")
                    }
                }
            }
        }
        items(uiState.jobs, key = { it.id }) { job ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically()
            ) {
                JobCard(job = job, onClick = { onJobClick(job.id) })
            }
        }
    }
}

@Composable
private fun OpportunityHero(onFindJobs: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(
                Brush.linearGradient(
                    listOf(colors.primaryContainer, colors.secondaryContainer)
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "YOUR NEXT MOVE",
                style = MaterialTheme.typography.labelLarge,
                color = colors.onPrimaryContainer
            )
            Text(
                text = "Find a role\nyou’ll love.",
                modifier = Modifier.padding(top = 10.dp),
                style = MaterialTheme.typography.headlineMedium,
                color = colors.onPrimaryContainer
            )
            Text(
                text = "Explore fresh opportunities tailored to your goals.",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onPrimaryContainer
            )
            Button(
                onClick = onFindJobs,
                modifier = Modifier.padding(top = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.onPrimaryContainer,
                    contentColor = colors.primaryContainer
                )
            ) {
                Text("Find new jobs")
            }
        }
    }
}
