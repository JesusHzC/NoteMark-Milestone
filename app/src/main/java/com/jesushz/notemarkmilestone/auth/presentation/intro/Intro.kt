package com.jesushz.notemarkmilestone.auth.presentation.intro

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jesushz.notemarkmilestone.auth.presentation.intro.components.IntroContentPhone
import com.jesushz.notemarkmilestone.auth.presentation.intro.components.IntroContentTablet
import com.jesushz.notemarkmilestone.core.presentation.designsystem.theme.NoteMarkMilestoneTheme
import com.jesushz.notemarkmilestone.core.presentation.ui.NoteMarkPreview
import com.jesushz.notemarkmilestone.core.util.isTablet

@Composable
fun IntroScreenRoot(
    navigateToRegister: () -> Unit,
    navigateToLogin: () -> Unit
) {
    IntroScreen(
        onAction = { action ->
            when (action) {
                IntroAction.OnGetStarted -> navigateToRegister()
                IntroAction.OnLogIn -> navigateToLogin()
            }
        }
    )
}

@Composable
private fun IntroScreen(
    onAction: (IntroAction) -> Unit,
) {
    when {
        isTablet() -> {
            IntroContentTablet(
                modifier = Modifier
                    .fillMaxSize(),
                onGetStartedClick = {
                    onAction(IntroAction.OnGetStarted)
                },
                onLoginClick = {
                    onAction(IntroAction.OnLogIn)
                }
            )
        }
        else -> {
            IntroContentPhone(
                modifier = Modifier
                    .fillMaxSize(),
                onGetStartedClick = {
                    onAction(IntroAction.OnGetStarted)
                },
                onLoginClick = {
                    onAction(IntroAction.OnLogIn)
                }
            )
        }
    }
}

@NoteMarkPreview
@Composable
private fun IntroPreview() {
    NoteMarkMilestoneTheme {
        IntroScreen(
            onAction = {}
        )
    }
}
