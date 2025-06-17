package com.jesushz.notemarkmilestone.auth.presentation.register

sealed interface RegisterAction {
    data object OnTogglePasswordVisibility: RegisterAction
    data object OnToggleConfirmPasswordVisibility: RegisterAction
    data object OnLoginClick: RegisterAction
    data object OnRegisterClick: RegisterAction
    data class OnValidateCredentials(
        val username: String,
        val email: String,
        val password: String,
        val confirmPassword: String
    ): RegisterAction
}