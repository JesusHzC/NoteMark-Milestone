package com.jesushz.notemarkmilestone.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jesushz.notemarkmilestone.auth.presentation.intro.IntroScreenRoot
import com.jesushz.notemarkmilestone.auth.presentation.login.LoginScreenRoot
import com.jesushz.notemarkmilestone.auth.presentation.register.RegisterScreenRoot
import com.jesushz.notemarkmilestone.note.presentation.note_list.NoteListScreenRoot
import com.jesushz.notemarkmilestone.note.presentation.upsert_note.UpsertNoteScreenRoot

@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) NavigationRoute.NoteGraph else NavigationRoute.AuthGraph,
    ) {
        authGraph(navController)
        noteGraph(navController)
    }
}

private fun NavGraphBuilder.authGraph(
    navController: NavHostController
) {
    navigation<NavigationRoute.AuthGraph>(
        startDestination = NavigationRoute.IntroScreen
    ) {
        composable<NavigationRoute.IntroScreen> {
            IntroScreenRoot(
                navigateToRegister = {
                    navController.navigate(NavigationRoute.RegisterScreen)
                },
                navigateToLogin = {
                    navController.navigate(NavigationRoute.LoginScreen)
                }
            )
        }

        composable<NavigationRoute.LoginScreen> {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate(NavigationRoute.NoteGraph) {
                        popUpTo(NavigationRoute.AuthGraph) {
                            inclusive = true
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate(NavigationRoute.RegisterScreen) {
                        popUpTo(NavigationRoute.LoginScreen) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }

        composable<NavigationRoute.RegisterScreen> {
            RegisterScreenRoot(
                onSignInClick = {
                    navController.navigate(NavigationRoute.LoginScreen) {
                        popUpTo(NavigationRoute.RegisterScreen) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = {
                    navController.navigate(NavigationRoute.LoginScreen)
                },
            )
        }
    }
}

private fun NavGraphBuilder.noteGraph(
    navController: NavHostController
) {
    navigation<NavigationRoute.NoteGraph>(
        startDestination = NavigationRoute.NoteList
    ) {
        composable<NavigationRoute.NoteList> {
            NoteListScreenRoot(
                onCreateNewNote = {
                    navController.navigate(NavigationRoute.UpsertNote)
                }
            )
        }

        composable<NavigationRoute.UpsertNote> {
            UpsertNoteScreenRoot(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
