package com.Vedang.careerflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.Vedang.careerflow.model.JobApplication
import com.Vedang.careerflow.ui.components.BackNavigationButton
import com.Vedang.careerflow.ui.components.EmptyContent
import com.Vedang.careerflow.ui.components.ErrorContent
import com.Vedang.careerflow.ui.components.LoadingContent
import com.Vedang.careerflow.viewmodel.ApplicationTrackerUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationTrackerScreen(
    uiState: ApplicationTrackerUiState,
    onRefresh: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Application tracker", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { BackNavigationButton(onClick = onBack) },
                actions = { OutlinedButton(onClick = onRefresh) { Text("Refresh") } }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading && uiState.applications.isEmpty() -> LoadingContent(Modifier.padding(innerPadding))
            uiState.errorMessage != null && uiState.applications.isEmpty() -> ErrorContent(
                message = uiState.errorMessage,
                onRetry = onRefresh,
                modifier = Modifier.padding(innerPadding)
            )
            uiState.applications.isEmpty() -> EmptyContent(
                title = "No applications yet",
                description = "Applications you add will appear here, organized by their current stage.",
                actionLabel = "Refresh",
                onAction = onRefresh,
                modifier = Modifier.padding(innerPadding)
            )
            else -> ApplicationList(uiState.applications, innerPadding)
        }
    }
}

@Composable
private fun ApplicationList(applications: List<JobApplication>, contentPadding: PaddingValues) {
    val applied = applications.count { it.status.equals("applied", true) }
    val interview = applications.count { it.status in setOf("interview", "interviewed") }
    val rejected = applications.count { it.status.equals("rejected", true) }
    val offer = applications.count { it.status in setOf("offer", "offered") }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp, contentPadding.calculateTopPadding() + 8.dp, 20.dp, 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Your progress", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Keep an eye on every opportunity in one place.",
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        item { StatusSummary(applied, interview, rejected, offer) }
        item { Text("All applications", style = MaterialTheme.typography.titleLarge) }
        items(applications, key = { it.id }) { ApplicationCard(it) }
    }
}

@Composable
private fun StatusSummary(applied: Int, interview: Int, rejected: Int, offer: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        listOf("Applied" to applied, "Interview" to interview, "Rejected" to rejected, "Offer" to offer).forEach { (label, count) ->
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("$count", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    Text(label, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
private fun ApplicationCard(application: JobApplication) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("Application #${application.jobId}", style = MaterialTheme.typography.titleMedium)
            Text(application.status.replaceFirstChar { it.uppercase() }, color = MaterialTheme.colorScheme.primary)
            application.notes?.takeIf { it.isNotBlank() }?.let {
                Text(it, modifier = Modifier.padding(top = 8.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
