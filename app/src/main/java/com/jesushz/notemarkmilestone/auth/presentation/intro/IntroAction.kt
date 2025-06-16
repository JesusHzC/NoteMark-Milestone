package com.jesushz.notemarkmilestone.auth.presentation.intro

sealed interface IntroAction {
    data object OnGetStarted: IntroAction
    data object OnLogIn: IntroAction
}