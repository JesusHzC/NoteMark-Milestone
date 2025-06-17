package com.jesushz.notemarkmilestone.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState

data class LoginState(
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val loginIsEnable: Boolean = false,
    val showPassword: Boolean = false,
    val isLoading: Boolean = false
)
