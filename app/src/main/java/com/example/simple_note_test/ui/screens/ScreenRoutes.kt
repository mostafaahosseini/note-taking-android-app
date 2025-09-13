package com.example.simple_note_test.ui.screens

// Centralized navigation routes used by the NavHost
sealed class ScreenRoutes(val route: String) {
    object Onboarding : ScreenRoutes("onboarding")
    object Login : ScreenRoutes("auth/login")
    object Register : ScreenRoutes("auth/register")
    object Forgot : ScreenRoutes("auth/forgot")
    object Home : ScreenRoutes("home")

    object NoteDetail : ScreenRoutes("note/detail/{id}") {
        fun createRoute(id: String) = "note/detail/$id"
    }

    object NoteEdit : ScreenRoutes("note/edit") {
        fun createRoute() = "note/edit"
        fun createRoute(id: String) = "note/edit/$id"
        val editWithId = "note/edit/{id}"
    }

    object Settings : ScreenRoutes("settings")
    object ChangePassword : ScreenRoutes("settings/change-password")
    object LogoutConfirm : ScreenRoutes("logout-confirm")
}
