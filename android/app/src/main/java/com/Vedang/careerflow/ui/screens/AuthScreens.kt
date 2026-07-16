package com.Vedang.careerflow.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.Vedang.careerflow.ui.components.BrandLockup

@Composable
fun SplashScreen() {
    val pulse by rememberInfiniteTransition(label = "splash pulse").animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(700, easing = LinearEasing), RepeatMode.Reverse),
        label = "brand scale"
    )
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(80.dp).scale(pulse).background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) { Text("↗", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onPrimary) }
            BrandLockup(Modifier.padding(top = 20.dp))
            Text("Your career, in motion.", modifier = Modifier.padding(top = 8.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun LoginScreen(onContinue: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp),
        verticalArrangement = Arrangement.Center
    ) {
        BrandLockup()
        Text("Welcome back", modifier = Modifier.padding(top = 28.dp), style = MaterialTheme.typography.headlineMedium)
        Text("Sign in to organize your job search.", modifier = Modifier.padding(top = 6.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
        OutlinedTextField(name, { name = it; error = null }, Modifier.fillMaxWidth().padding(top = 28.dp), label = { Text("Name") }, singleLine = true)
        OutlinedTextField(password, { password = it; error = null }, Modifier.fillMaxWidth().padding(top = 12.dp), label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), singleLine = true)
        error?.let { Text(it, modifier = Modifier.padding(top = 10.dp), color = MaterialTheme.colorScheme.error) }
        Button(
            onClick = { if (name.isBlank()) error = "Enter a name to continue." else onContinue(name.trim()) },
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
        ) { Text("Continue") }
        Text("Demo sign-in — no account or server required.", modifier = Modifier.padding(top = 12.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
