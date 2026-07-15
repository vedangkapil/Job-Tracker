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
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.Vedang.careerflow.ui.components.JobCard
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
            CenterAlignedTopAppBar(
                title = { Text("Find jobs") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
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
            top = contentPadding.calculateTopPadding() + 12.dp,
            end = 20.dp,
            bottom = contentPadding.calculateBottomPadding() + 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text("Discover your next role", style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = "Searches currently use LinkedIn and save the results to your job list.",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                OutlinedTextField(
                    value = uiState.keyword,
                    onValueChange = onKeywordChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    label = { Text("Job title or skill") },
                    placeholder = { Text("e.g. Android developer") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                OutlinedTextField(
                    value = uiState.location,
                    onValueChange = onLocationChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    label = { Text("Location") },
                    placeholder = { Text("e.g. Pune") },
                    singleLine = true,
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
                    Text(if (uiState.isLoading) "Searching…" else "Find jobs")
                }
                if (uiState.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
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
                uiState.message?.let { message ->
                    Text(
                        text = message,
                        modifier = Modifier.padding(top = 12.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        if (!uiState.isLoading && uiState.message != null && uiState.jobs.isEmpty()) {
            item {
                Text(
                    text = "No matching roles were found. Try another search.",
                    modifier = Modifier.padding(vertical = 20.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        items(uiState.jobs, key = { it.id }) { job ->
            JobCard(job = job, onClick = { onJobClick(job.id) })
        }
    }
}
