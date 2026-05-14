package com.example.kreedaperana.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.kreedaperana.viewmodel.AuthState
import com.example.kreedaperana.viewmodel.AuthViewModel
import com.example.kreedaperana.viewmodel.SportsViewModel
import com.example.kreedaperana.ui.screens.*
import com.example.kreedaperana.auth.*

@Composable
fun NavGraph(
    navController: NavHostController,
    sportsViewModel: SportsViewModel,
    authViewModel: AuthViewModel
) {
    val sharedOnNavigate: (String) -> Unit = { routeStr ->
        when (routeStr) {
            "home" -> navController.navigate(Route.Home) { popUpTo(Route.Home) { inclusive = true } }
            "addAthlete" -> navController.navigate(Route.AddAthlete)
            "athleteList" -> navController.navigate(Route.AthleteList)
            "timer" -> navController.navigate(Route.Timer)
            "leaderboard" -> navController.navigate(Route.Leaderboard)
            "reports" -> navController.navigate(Route.Reports)
            "settings" -> navController.navigate(Route.Settings)
            "analytics" -> navController.navigate(Route.Analytics)
            "aiInsights" -> navController.navigate(Route.AiInsights)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Route.Splash,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        }
    ) {
        composable<Route.Splash> {
            SplashScreen {
                val nextRoute = if (authViewModel.authState.value is AuthState.Authenticated) {
                    Route.Home
                } else {
                    Route.Login
                }
                navController.navigate(nextRoute) {
                    popUpTo(Route.Splash) { inclusive = true }
                }
            }
        }

        composable<Route.Login> {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.Login) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Route.SignUp)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Route.ForgotPassword)
                }
            )
        }

        composable<Route.ForgotPassword> {
            ForgotPasswordScreen(
                authViewModel = authViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.SignUp> {
            SignUpScreen(
                authViewModel = authViewModel,
                onSignUpSuccess = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.SignUp) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable<Route.Home> {
            HomeScreen(
                viewModel = sportsViewModel,
                onNavigate = sharedOnNavigate
            )
        }

        composable<Route.AddAthlete> {
            AddAthleteScreen(
                viewModel = sportsViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.AthleteList> {
            AthleteListScreen(
                viewModel = sportsViewModel,
                onAthleteClick = { id -> navController.navigate(Route.AthleteDetail(id)) },
                onAddAthleteClick = { navController.navigate(Route.AddAthlete) },
                onLeaderboardClick = { navController.navigate(Route.Leaderboard) },
                onNavigate = sharedOnNavigate
            )
        }

        composable<Route.AthleteDetail> { backStackEntry ->
            val route: Route.AthleteDetail = backStackEntry.toRoute()
            AthleteDetailScreen(
                athleteId = route.athleteId,
                viewModel = sportsViewModel,
                onBack = { navController.popBackStack() },
                onSeeAllAchievements = { id -> navController.navigate(Route.BadgeCollection(id)) },
                onViewProgress = { id -> navController.navigate(Route.ProgressChart(id)) }
            )
        }

        composable<Route.Timer> {
            TimerScreen(
                viewModel = sportsViewModel,
                onBack = { navController.popBackStack() },
                onNavigate = sharedOnNavigate
            )
        }

        composable<Route.Leaderboard> {
            LeaderboardScreen(
                viewModel = sportsViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.Reports> {
            ReportsScreen(
                viewModel = sportsViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.Settings> {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Route.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigate = sharedOnNavigate
            )
        }

        composable<Route.TrialLogger> { backStackEntry ->
            val route: Route.TrialLogger = backStackEntry.toRoute()
            TrialLoggerScreen(
                athleteId = route.athleteId,
                viewModel = sportsViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.BadgeCollection> { backStackEntry ->
            val route: Route.BadgeCollection = backStackEntry.toRoute()
            BadgeCollectionScreen(
                athleteId = route.athleteId,
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.Analytics> {
            AnalyticsScreen(
                viewModel = sportsViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.ProgressChart> { backStackEntry ->
            val route: Route.ProgressChart = backStackEntry.toRoute()
            ProgressChartScreen(
                athleteId = route.athleteId,
                viewModel = sportsViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.AiInsights> {
            AiInsightsScreen(
                viewModel = sportsViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
