package com.example.simple_note_test.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.simple_note_test.ui.screens.auth.LoginScreen
import com.example.simple_note_test.ui.screens.auth.RegisterScreen
import com.example.simple_note_test.ui.screens.notes.HomeScreen
import com.example.simple_note_test.ui.screens.editor.NoteEditScreen
import com.example.simple_note_test.ui.screens.welcome.OnboardingScreen
import com.example.simple_note_test.ui.screens.settings.SettingsScreen
import com.example.simple_note_test.ui.screens.ChangePasswordScreen
import com.example.simple_note_test.ui.screens.LogoutConfirmScreen

@Composable
fun AppNavigator(navHost: NavHostController) {
    NavHost(
        navController = navHost,
        startDestination = ScreenRoutes.Onboarding.route
    ) {
        composable(ScreenRoutes.Onboarding.route) {
            OnboardingScreen(onGetStarted = {
                navHost.navigate(ScreenRoutes.Login.route) {
                    popUpTo(ScreenRoutes.Onboarding.route) { inclusive = true }
                }
            })
        }

        // Authentication screens
        composable(ScreenRoutes.Login.route) { LoginScreen(navHost) }
        composable(ScreenRoutes.Register.route) { RegisterScreen(navHost) }
        composable(ScreenRoutes.Forgot.route) { ForgotScreen(navHost) }

        // Main flows
        composable(ScreenRoutes.Home.route) { HomeScreen(navHost) }

        composable(ScreenRoutes.NoteDetail.route) { entry ->
            val noteId = entry.arguments?.getString("id") ?: ""
            NoteDetailScreen(navHost, noteId)
        }

        composable(ScreenRoutes.NoteEdit.route) {
            NoteEditScreen(navHost, null)
        }

        composable(
            route = ScreenRoutes.NoteEdit.editWithId,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val editId = entry.arguments?.getString("id")
            NoteEditScreen(navHost, editId)
        }

        composable(ScreenRoutes.Settings.route) { SettingsScreen(navHost) }
        composable(ScreenRoutes.ChangePassword.route) { ChangePasswordScreen(navHost) }
        composable(ScreenRoutes.LogoutConfirm.route) { LogoutConfirmScreen(navHost) }
    }
}
