package com.jesushz.notemarkmilestone.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.AuthGraph,
    ) {
        authGraph(navController)
    }
}

private fun NavGraphBuilder.authGraph(
    navController: NavHostController
) {
    navigation<NavigationRoute.AuthGraph>(
        startDestination = NavigationRoute.IntroScreen
    ) {
        composable<NavigationRoute.IntroScreen> {

        }
    }
}
