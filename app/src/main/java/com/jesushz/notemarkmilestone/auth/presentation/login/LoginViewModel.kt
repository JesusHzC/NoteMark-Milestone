package com.jesushz.notemarkmilestone.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.notemarkmilestone.auth.domain.UserDataValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val userDataValidator: UserDataValidator
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(LoginState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginState()
        )

    private val _eventUi = Channel<LoginEvent>()
    val eventUi = _eventUi.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLogInClick -> {
            }
            LoginAction.OnTogglePasswordVisibility -> {
                _state.update {
                    it.copy(
                        showPassword = !it.showPassword
                    )
                }
            }
            is LoginAction.OnValidateCredentials -> {
                _state.update {
                    it.copy(
                        loginIsEnable = userDataValidator.isValidEmail(action.email) &&
                                action.password.isNotEmpty()
                    )
                }
            }
            else -> Unit
        }
    }

}