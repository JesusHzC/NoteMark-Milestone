package com.jesushz.notemarkmilestone.app.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationRoute {

    // Graphs
    @Serializable
    data object AuthGraph: NavigationRoute

    // Screens Auth
    @Serializable
    data object IntroScreen: NavigationRoute
    @Serializable
    data object LoginScreen: NavigationRoute
    @Serializable
    data object RegisterScreen: NavigationRoute

}
