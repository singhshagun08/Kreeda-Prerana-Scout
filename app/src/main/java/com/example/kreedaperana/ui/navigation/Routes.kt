package com.example.kreedaperana.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Splash : Route
    @Serializable
    data object Login : Route
    @Serializable
    data object SignUp : Route
    @Serializable
    data object ForgotPassword : Route
    @Serializable
    data object Home : Route
    @Serializable
    data object AddAthlete : Route
    @Serializable
    data object AthleteList : Route
    @Serializable
    data class AthleteDetail(val athleteId: Int) : Route
    @Serializable
    data object Timer : Route
    @Serializable
    data object Leaderboard : Route
    @Serializable
    data object Reports : Route
    @Serializable
    data object Settings : Route
    @Serializable
    data class TrialLogger(val athleteId: Int) : Route
    @Serializable
    data class BadgeCollection(val athleteId: Int) : Route
    @Serializable
    data object Analytics : Route
    @Serializable
    data class ProgressChart(val athleteId: Int) : Route
    @Serializable
    data object AiInsights : Route
}
