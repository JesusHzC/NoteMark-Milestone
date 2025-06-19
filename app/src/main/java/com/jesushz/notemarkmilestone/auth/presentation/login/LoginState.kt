package com.jesushz.notemarkmilestone.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import com.jesushz.notemarkmilestone.core.presentation.ui.UiText

data class LoginState(
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val loginIsEnable: Boolean = false,
    val showPassword: Boolean = false,
    val isLoading: Boolean = false,
    val errorEmail: UiText? = null,
)
