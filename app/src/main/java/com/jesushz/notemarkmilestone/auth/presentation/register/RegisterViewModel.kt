package com.jesushz.notemarkmilestone.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.notemarkmilestone.auth.domain.UserDataValidator
import com.jesushz.notemarkmilestone.auth.domain.repository.AuthRepository
import com.jesushz.notemarkmilestone.core.domain.networking.Result
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
                val isValidEmail = userDataValidator.isValidEmail(action.email)
                val isValidPassword = userDataValidator.validatePassword(action.password).isValidPassword
                val isValidConfirmPassword = userDataValidator.validatePassword(action.confirmPassword).isValidPassword
                val isValidUsername = userDataValidator.validateUsername(action.username)
                val passwordMatch = action.password == action.confirmPassword
                _state.update {
                    it.copy(
                        registerIsEnable = isValidEmail and isValidPassword and isValidConfirmPassword and isValidUsername and passwordMatch
                    )
                }
            }
            else -> Unit
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
                    _eventUi.send(RegisterEvent.OnError(result.error.asUiText()))
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