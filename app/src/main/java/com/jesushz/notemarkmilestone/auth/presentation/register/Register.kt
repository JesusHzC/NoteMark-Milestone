package com.jesushz.notemarkmilestone.auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.notemarkmilestone.R
import com.jesushz.notemarkmilestone.auth.presentation.register.components.RegisterContentLandscape
import com.jesushz.notemarkmilestone.auth.presentation.register.components.RegisterContentPortrait
import com.jesushz.notemarkmilestone.auth.presentation.register.components.RegisterContentTablet
import com.jesushz.notemarkmilestone.core.presentation.designsystem.theme.NoteMarkMilestoneTheme
import com.jesushz.notemarkmilestone.core.presentation.ui.NoteMarkPreview
import com.jesushz.notemarkmilestone.core.presentation.ui.ObserveAsEvents
import com.jesushz.notemarkmilestone.core.util.isLandscape
import com.jesushz.notemarkmilestone.core.util.isTablet
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    viewModel: RegisterViewModel = koinViewModel(),
    navigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(
        flow = viewModel.eventUi
    ) { event ->
        when (event) {
            is RegisterEvent.OnError -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            RegisterEvent.RegisterSuccess -> {
                Toast.makeText(
                    context,
                    R.string.registration_successful,
                    Toast.LENGTH_LONG
                ).show()
                navigateToLogin()
            }
        }
    }

    RegisterScreen(
        state = state,
        onAction = { action ->
            when (action) {
                RegisterAction.OnLoginClick -> navigateToLogin()
                RegisterAction.OnRegisterClick -> keyboardController?.hide()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
) {
    LaunchedEffect(state.username.text, state.email.text, state.password.text, state.confirmPassword.text) {
        onAction(
            RegisterAction.OnValidateCredentials(
                state.username.text.toString(),
                state.email.text.toString(),
                state.password.text.toString(),
                state.confirmPassword.text.toString()
            )
        )
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp
            )
        ) {
            when {
                isTablet() -> {
                    RegisterContentTablet(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                vertical = 100.dp,
                                horizontal = 120.dp
                            )
                    )
                }
                isLandscape() -> {
                    RegisterContentLandscape(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = 60.dp,
                                top = 32.dp,
                                end = 32.dp,
                                bottom = 32.dp
                            )
                    )
                }
                else -> {
                    RegisterContentPortrait(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 32.dp
                            )
                    )
                }
            }
        }
    }
}

@NoteMarkPreview
@Composable
private fun RegisterPreview() {
    NoteMarkMilestoneTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}
