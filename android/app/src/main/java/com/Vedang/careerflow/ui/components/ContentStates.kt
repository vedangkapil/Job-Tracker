package com.Vedang.careerflow.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "loading shimmer")
    val alpha = transition.animateFloat(
        initialValue = 0.38f,
        targetValue = 0.82f,
        animationSpec = infiniteRepeatable(
            animation = tween(950, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer alpha"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Finding opportunities", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "Bringing your jobs together…",
            modifier = Modifier.padding(top = 6.dp, bottom = 24.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        repeat(3) {
            JobCardSkeleton(alpha = alpha.value)
        }
    }
}

@Composable
private fun JobCardSkeleton(alpha: Float) {
    val shape = RoundedCornerShape(24.dp)
    val shade = MaterialTheme.colorScheme.surfaceVariant
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerLow, shape)
            .padding(18.dp)
            .alpha(alpha)
    ) {
        Box(
            modifier = Modifier
                .height(22.dp)
                .fillMaxWidth(0.72f)
                .background(shade, RoundedCornerShape(8.dp))
        )
        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .height(16.dp)
                .width(132.dp)
                .background(shade, RoundedCornerShape(8.dp))
        )
        Box(
            modifier = Modifier
                .padding(top = 24.dp)
                .height(14.dp)
                .fillMaxWidth(0.9f)
                .background(shade, RoundedCornerShape(8.dp))
        )
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "We lost the connection", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = message,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Button(onClick = onRetry, modifier = Modifier.padding(top = 20.dp)) {
            Text("Try again")
        }
    }
}

@Composable
fun EmptyContent(
    title: String,
    description: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "✦", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.primary)
        Text(
            text = title,
            modifier = Modifier.padding(top = 12.dp),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = description,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextButton(onClick = onAction, modifier = Modifier.padding(top = 12.dp)) {
            Text(actionLabel)
        }
    }
}
