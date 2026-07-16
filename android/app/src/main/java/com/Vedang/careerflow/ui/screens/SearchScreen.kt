package com.Vedang.careerflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.Vedang.careerflow.ui.components.JobCard
import com.Vedang.careerflow.ui.components.BackNavigationButton
import com.Vedang.careerflow.viewmodel.SearchUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onKeywordChanged: (String) -> Unit,
    onLocationChanged: (String) -> Unit,
    onFindJobs: () -> Unit,
    onBack: () -> Unit,
    onJobClick: (Int) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Find jobs", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { BackNavigationButton(onClick = onBack) }
            )
        }
    ) { innerPadding ->
        SearchContent(
            uiState = uiState,
            contentPadding = innerPadding,
            focusManager = focusManager,
            onKeywordChanged = onKeywordChanged,
            onLocationChanged = onLocationChanged,
            onFindJobs = onFindJobs,
            onJobClick = onJobClick
        )
    }
}

@Composable
private fun SearchContent(
    uiState: SearchUiState,
    contentPadding: PaddingValues,
    focusManager: FocusManager,
    onKeywordChanged: (String) -> Unit,
    onLocationChanged: (String) -> Unit,
    onFindJobs: () -> Unit,
    onJobClick: (Int) -> Unit
) {
    val submitSearch = {
        focusManager.clearFocus()
        onFindJobs()
    }
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
            Text("Find your next role", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Tell us what you’re looking for and we’ll bring fresh roles to you.",
                modifier = Modifier.padding(top = 6.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        item {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Your search", style = MaterialTheme.typography.titleLarge)
                    AssistChip(
                        onClick = {},
                        label = { Text("LinkedIn results") },
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    OutlinedTextField(
                        value = uiState.keyword,
                        onValueChange = onKeywordChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp),
                        label = { Text("Role or skill") },
                        placeholder = { Text("Android developer") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    OutlinedTextField(
                        value = uiState.location,
                        onValueChange = onLocationChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        label = { Text("Location") },
                        placeholder = { Text("Pune") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { submitSearch() })
                    )
                    Button(
                        onClick = submitSearch,
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(if (uiState.isLoading) "Searching…" else "Search opportunities")
                    }
                    if (uiState.isLoading) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 14.dp),
                            color = MaterialTheme.colorScheme.secondary
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
        uiState.message?.let { message ->
            item {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        if (!uiState.isLoading && uiState.message != null && uiState.jobs.isEmpty()) {
            item {
                Text(
                    text = "No matching roles yet. Try a broader title or a nearby location.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (uiState.jobs.isNotEmpty()) {
            item {
                Text(
                    text = "${uiState.searchedKeyword ?: "Search"} roles",
                    style = MaterialTheme.typography.headlineMedium
                )
                uiState.searchedLocation?.let { location ->
                    Text(
                        text = "in $location",
                        modifier = Modifier.padding(top = 4.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        items(uiState.jobs, key = { it.id }) { job ->
            JobCard(job = job, onClick = { onJobClick(job.id) })
        }
    }
}
