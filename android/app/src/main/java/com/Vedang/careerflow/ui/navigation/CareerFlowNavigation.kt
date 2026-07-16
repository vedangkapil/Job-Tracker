package com.Vedang.careerflow.ui.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Vedang.careerflow.ui.screens.HomeScreen
import com.Vedang.careerflow.ui.screens.ApplicationTrackerScreen
import com.Vedang.careerflow.ui.screens.JobDetailScreen
import com.Vedang.careerflow.ui.screens.LoginScreen
import com.Vedang.careerflow.ui.screens.ProfileScreen
import com.Vedang.careerflow.ui.screens.SearchScreen
import com.Vedang.careerflow.ui.screens.SavedJobsScreen
import com.Vedang.careerflow.ui.screens.SplashScreen
import com.Vedang.careerflow.viewmodel.ApplicationTrackerViewModel
import com.Vedang.careerflow.viewmodel.HomeViewModel
import com.Vedang.careerflow.viewmodel.JobDetailViewModel
import com.Vedang.careerflow.viewmodel.SearchViewModel
import com.Vedang.careerflow.viewmodel.SavedJobsViewModel

sealed class CareerFlowDestination(val route: String) {
    data object Home : CareerFlowDestination("home")
    data object Search : CareerFlowDestination("search")
    data object SavedJobs : CareerFlowDestination("saved_jobs")
    data object Applications : CareerFlowDestination("applications")
    data object Profile : CareerFlowDestination("profile")

    data object JobDetail : CareerFlowDestination("job/{jobId}") {
        fun createRoute(jobId: Int) = "job/$jobId"
    }
}

@Composable
fun CareerFlowApp() {
    var splashVisible by remember { mutableStateOf(true) }
    var profileName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(splashVisible) {
        if (splashVisible) {
            kotlinx.coroutines.delay(1200)
            splashVisible = false
        }
    }

    when {
        splashVisible -> SplashScreen()
        profileName == null -> LoginScreen(onContinue = { profileName = it })
        else -> AuthenticatedCareerFlowApp(
            profileName = profileName.orEmpty(),
            onSignOut = { profileName = null }
        )
    }
}

@Composable
private fun AuthenticatedCareerFlowApp(profileName: String, onSignOut: () -> Unit) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CareerFlowDestination.Home.route
    ) {
        composable(CareerFlowDestination.Home.route) {
            HomeRoute(
                onFindJobs = { navController.navigate(CareerFlowDestination.Search.route) },
                onSavedJobs = { navController.navigate(CareerFlowDestination.SavedJobs.route) },
                onApplications = { navController.navigate(CareerFlowDestination.Applications.route) },
                onProfile = { navController.navigate(CareerFlowDestination.Profile.route) },
                onJobClick = { jobId ->
                    navController.navigate(CareerFlowDestination.JobDetail.createRoute(jobId))
                }
            )
        }

        composable(CareerFlowDestination.Search.route) {
            SearchRoute(
                onBack = { navController.popBackStack() },
                onJobClick = { jobId ->
                    navController.navigate(CareerFlowDestination.JobDetail.createRoute(jobId))
                }
            )
        }

        composable(CareerFlowDestination.SavedJobs.route) {
            SavedJobsRoute(
                onBack = { navController.popBackStack() },
                onFindJobs = { navController.navigate(CareerFlowDestination.Search.route) },
                onJobClick = { jobId ->
                    navController.navigate(CareerFlowDestination.JobDetail.createRoute(jobId))
                }
            )
        }

        composable(CareerFlowDestination.Applications.route) {
            ApplicationsRoute(onBack = { navController.popBackStack() })
        }

        composable(CareerFlowDestination.Profile.route) {
            ProfileScreen(
                name = profileName,
                onBack = { navController.popBackStack() },
                onSignOut = onSignOut
            )
        }

        composable(
            route = CareerFlowDestination.JobDetail.route,
            arguments = listOf(navArgument("jobId") { type = NavType.IntType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getInt("jobId") ?: return@composable
            JobDetailRoute(
                jobId = jobId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun HomeRoute(
    onFindJobs: () -> Unit,
    onSavedJobs: () -> Unit,
    onApplications: () -> Unit,
    onProfile: () -> Unit,
    onJobClick: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadJobs()
    }

    HomeScreen(
        uiState = uiState.value,
        onRefresh = viewModel::loadJobs,
        onFindJobs = onFindJobs,
        onSavedJobs = onSavedJobs,
        onApplications = onApplications,
        onProfile = onProfile,
        onJobClick = onJobClick
    )
}

@Composable
private fun ApplicationsRoute(
    onBack: () -> Unit,
    viewModel: ApplicationTrackerViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.loadApplications() }
    ApplicationTrackerScreen(
        uiState = uiState.value,
        onRefresh = viewModel::loadApplications,
        onBack = onBack
    )
}

@Composable
private fun SearchRoute(
    onBack: () -> Unit,
    onJobClick: (Int) -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        uiState = uiState.value,
        onKeywordChanged = viewModel::updateKeyword,
        onLocationChanged = viewModel::updateLocation,
        onFindJobs = viewModel::findNewJobs,
        onBack = onBack,
        onJobClick = onJobClick
    )
}

@Composable
private fun SavedJobsRoute(
    onBack: () -> Unit,
    onFindJobs: () -> Unit,
    onJobClick: (Int) -> Unit,
    viewModel: SavedJobsViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadSavedJobs()
    }

    SavedJobsScreen(
        uiState = uiState.value,
        onRefresh = viewModel::loadSavedJobs,
        onFindJobs = onFindJobs,
        onBack = onBack,
        onJobClick = onJobClick
    )
}

@Composable
private fun JobDetailRoute(
    jobId: Int,
    onBack: () -> Unit,
    viewModel: JobDetailViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(jobId) {
        viewModel.loadJob(jobId)
    }

    JobDetailScreen(
        uiState = uiState.value,
        onRefresh = { viewModel.loadJob(jobId) },
        onBack = onBack,
        onUpdateTracking = viewModel::updateTrackedState,
        onApply = { url ->
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    )
}

@Composable
private fun PlaceholderScreen(
    title: String,
    description: String,
    primaryActionLabel: String,
    onPrimaryAction: () -> Unit,
    secondaryActionLabel: String? = null,
    onSecondaryAction: (() -> Unit)? = null
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
            Text(text = description, style = MaterialTheme.typography.bodyLarge)
            Button(onClick = onPrimaryAction) {
                Text(primaryActionLabel)
            }
            if (secondaryActionLabel != null && onSecondaryAction != null) {
                Button(onClick = onSecondaryAction) {
                    Text(secondaryActionLabel)
                }
            }
        }
    }
}
