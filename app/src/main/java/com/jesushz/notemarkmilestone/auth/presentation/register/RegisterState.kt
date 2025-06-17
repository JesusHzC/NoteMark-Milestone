package com.jesushz.notemarkmilestone.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState

data class RegisterState(
    val username: TextFieldState = TextFieldState(),
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val confirmPassword: TextFieldState = TextFieldState(),
    val registerIsEnable: Boolean = false,
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val isLoading: Boolean = false
)
