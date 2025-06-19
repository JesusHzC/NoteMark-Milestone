package com.jesushz.notemarkmilestone.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.notemarkmilestone.R
import com.jesushz.notemarkmilestone.auth.domain.UserDataValidator
import com.jesushz.notemarkmilestone.auth.domain.repository.AuthRepository
import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.presentation.ui.UiText
import com.jesushz.notemarkmilestone.core.presentation.ui.asUiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _eventUi = Channel<LoginEvent>()
    val eventUi = _eventUi.receiveAsFlow()

    private var loginJob: Job? = null

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLogInClick -> {
                login()
            }
            LoginAction.OnTogglePasswordVisibility -> {
                _state.update {
                    it.copy(
                        showPassword = !it.showPassword
                    )
                }
            }
            is LoginAction.OnValidateCredentials -> {
                validateFields(
                    action.email,
                    action.password
                )
            }
            else -> Unit
        }
    }

    private fun validateFields(email: String, password: String) {
        val isValidEmail = userDataValidator.isValidEmail(email)

        val errorEmail = if (email.isBlank()) {
            null
        } else if (!isValidEmail) {
            UiText.StringResource(R.string.error_invalid_email)
        } else null

        _state.update {
            it.copy(
                errorEmail = errorEmail,
                loginIsEnable = isValidEmail && password.isNotBlank()
            )
        }
    }

    private fun login() {
        if (loginJob?.isActive == true) return

        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            val email = state.value.email.text.toString().trim()
            val password = state.value.password.text.toString()

            setIsLoading(true)
            val result = authRepository.login(email, password)
            when (result) {
                is Result.Error -> {
                    setIsLoading(false)
                    if (result.error == DataError.Network.UNAUTHORIZED) {
                        _eventUi.send(
                            LoginEvent.OnError(
                                UiText.StringResource(R.string.error_email_password_incorrect)
                            )
                        )
                    } else {
                        _eventUi.send(
                            LoginEvent.OnError(
                                result.error.asUiText()
                            )
                        )
                    }
                }
                is Result.Success -> {
                    setIsLoading(false)
                    _eventUi.send(LoginEvent.LoginSuccess)
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