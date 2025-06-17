package com.jesushz.notemarkmilestone.auth.presentation.login

import com.jesushz.notemarkmilestone.core.presentation.ui.UiText

sealed interface LoginEvent {
    data class OnError(val error: UiText): LoginEvent
    data object LoginSuccess: LoginEvent
}
