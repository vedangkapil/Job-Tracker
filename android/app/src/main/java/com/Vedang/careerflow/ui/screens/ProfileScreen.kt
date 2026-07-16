package com.Vedang.careerflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.Vedang.careerflow.ui.components.BackNavigationButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(name: String, onBack: () -> Unit, onSignOut: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { BackNavigationButton(onClick = onBack) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Box(
                        modifier = Modifier.size(56.dp).background(MaterialTheme.colorScheme.onPrimaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(name.firstOrNull()?.uppercaseChar()?.toString() ?: "?", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primaryContainer)
                    }
                    Text(name, modifier = Modifier.padding(top = 16.dp), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("Job seeker", modifier = Modifier.padding(top = 4.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            Text("About your profile", style = MaterialTheme.typography.titleLarge)
            Text(
                "This is a local demo profile. Account management and authentication will be added later.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedButton(onClick = onSignOut, modifier = Modifier.fillMaxWidth()) { Text("Sign out") }
        }
    }
}
