package com.jesushz.notemarkmilestone.auth.presentation.login

sealed interface LoginAction {
    data object OnTogglePasswordVisibility: LoginAction
    data object OnLogInClick: LoginAction
    data object OnRegisterClick: LoginAction
    data class OnValidateCredentials(val email: String, val password: String): LoginAction
}
