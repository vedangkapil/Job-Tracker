package com.Vedang.careerflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.Vedang.careerflow.ui.theme.CareerFlowTheme
import com.Vedang.careerflow.ui.navigation.CareerFlowApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CareerFlowTheme {
                CareerFlowApp()
            }
        }
    }
}
