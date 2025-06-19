package com.jesushz.notemarkmilestone.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.notemarkmilestone.R
import com.jesushz.notemarkmilestone.auth.domain.UserDataValidator
import com.jesushz.notemarkmilestone.auth.domain.repository.AuthRepository
import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.presentation.ui.UiText
import com.jesushz.notemarkmilestone.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val _eventUi = Channel<RegisterEvent>()
    val eventUi = _eventUi.receiveAsFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            RegisterAction.OnRegisterClick -> {
                register()
            }
            RegisterAction.OnToggleConfirmPasswordVisibility -> {
                _state.update {
                    it.copy(
                        showConfirmPassword = !it.showConfirmPassword
                    )
                }
            }
            RegisterAction.OnTogglePasswordVisibility -> {
                _state.update {
                    it.copy(
                        showPassword = !it.showPassword
                    )
                }
            }
            is RegisterAction.OnValidateCredentials -> {
                validateFields(
                    username = action.username,
                    email = action.email,
                    password = action.password,
                    confirmPassword = action.confirmPassword
                )
            }
            else -> Unit
        }
    }

    private fun validateFields(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        val isValidUsername = userDataValidator.validateUsername(username)
        val isValidEmail = userDataValidator.isValidEmail(email)
        val passwordValidationState = userDataValidator.validatePassword(password)
        val passwordMatch = password == confirmPassword

        val errorEmail = if (email.isBlank()) {
            null
        } else if (!isValidEmail) {
            UiText.StringResource(R.string.error_invalid_email)
        } else null

        val errorUsername = if (username.isBlank()) {
            null
        } else if (!isValidUsername) {
            UiText.StringResource(R.string.error_invalid_username)
        } else null

        val errorPassword = if (password.isBlank()) {
            null
        } else if (!passwordValidationState.isValidPassword) {
            when {
                !passwordValidationState.hasMinLength -> UiText.StringResource(R.string.error_password_too_short)
                !passwordValidationState.hasNumber -> UiText.StringResource(R.string.error_password_no_number)
                !passwordValidationState.hasLowerCaseCharacter -> UiText.StringResource(R.string.error_password_no_lowercase)
                !passwordValidationState.hasUpperCaseCharacter -> UiText.StringResource(R.string.error_password_no_uppercase)
                else -> null
            }
        } else null

        val errorConfirmPassword = if (confirmPassword.isBlank()) {
            null
        } else if (!passwordMatch) {
            UiText.StringResource(R.string.error_password_mismatch)
        } else null

        val isFormValid = email.isNotBlank() &&
                username.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                isValidEmail &&
                isValidUsername &&
                passwordValidationState.isValidPassword &&
                passwordMatch

        _state.update {
            it.copy(
                errorEmail = errorEmail,
                errorUsername = errorUsername,
                errorPassword = errorPassword,
                errorConfirmPassword = errorConfirmPassword,
                registerIsEnable = isFormValid
            )
        }
    }



    private fun register() {
        viewModelScope.launch {
            val username = state.value.username.text.toString().trim()
            val email = state.value.email.text.toString().trim()
            val password = state.value.password.text.toString()

            setIsLoading(true)
            val result = authRepository.register(username, email, password)
            when (result) {
                is Result.Error -> {
                    setIsLoading(false)
                    if (result.error == DataError.Network.CONFLICT) {
                        _eventUi.send(
                            RegisterEvent.OnError(
                                UiText.StringResource(R.string.error_email_or_username_exists)
                            )
                        )
                    } else {
                        _eventUi.send(RegisterEvent.OnError(result.error.asUiText()))
                    }
                }
                is Result.Success -> {
                    setIsLoading(false)
                    _eventUi.send(RegisterEvent.RegisterSuccess)
                }
            }
        }
    }

    private fun setIsLoading(isLoading: Boolean) {
        _state.update {
            it.copy(
                isLoading = isLoading
            )
        }
    }

}