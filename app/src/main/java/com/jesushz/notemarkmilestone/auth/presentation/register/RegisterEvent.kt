package com.jesushz.notemarkmilestone.auth.presentation.register

import com.jesushz.notemarkmilestone.core.presentation.ui.UiText

sealed interface RegisterEvent {
    data object RegisterSuccess: RegisterEvent
    data class OnError(val error: UiText): RegisterEvent
}
