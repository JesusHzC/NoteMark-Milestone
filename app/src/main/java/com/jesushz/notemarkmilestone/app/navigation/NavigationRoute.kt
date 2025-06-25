package com.jesushz.notemarkmilestone.app.navigation

import com.jesushz.notemarkmilestone.core.domain.note.NoteNavigation
import kotlinx.serialization.Serializable

sealed interface NavigationRoute {

    // Graphs
    @Serializable
    data object AuthGraph: NavigationRoute
    @Serializable
    data object NoteGraph: NavigationRoute

    // Screens Auth
    @Serializable
    data object IntroScreen: NavigationRoute
    @Serializable
    data object LoginScreen: NavigationRoute
    @Serializable
    data object RegisterScreen: NavigationRoute

    // Screens Note
    @Serializable
    data object NoteList: NavigationRoute
    @Serializable
    data class UpsertNote(val note: NoteNavigation?): NavigationRoute

}
