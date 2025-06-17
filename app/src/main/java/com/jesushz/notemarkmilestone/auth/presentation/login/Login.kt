package com.jesushz.notemarkmilestone.auth.presentation.login

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
import com.jesushz.notemarkmilestone.auth.presentation.login.components.ContentLandscape
import com.jesushz.notemarkmilestone.auth.presentation.login.components.ContentPortrait
import com.jesushz.notemarkmilestone.auth.presentation.login.components.ContentTablet
import com.jesushz.notemarkmilestone.core.presentation.designsystem.theme.NoteMarkMilestoneTheme
import com.jesushz.notemarkmilestone.core.presentation.ui.NoteMarkPreview
import com.jesushz.notemarkmilestone.core.presentation.ui.ObserveAsEvents
import com.jesushz.notemarkmilestone.core.presentation.ui.rememberDeviceInfo
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(
        flow = viewModel.eventUi
    ) { event ->
        when (event) {
            LoginEvent.LoginSuccess -> {
                Toast.makeText(
                    context,
                    R.string.youre_logged_in,
                    Toast.LENGTH_LONG
                ).show()
            }
            is LoginEvent.OnError -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    LoginScreen(
        state = state,
        onAction = { action ->
            when (action) {
                LoginAction.OnRegisterClick -> onNavigateToRegister()
                LoginAction.OnLogInClick -> keyboardController?.hide()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    val deviceInfo = rememberDeviceInfo()
    LaunchedEffect(state.email.text, state.password.text) {
        onAction(
            LoginAction.OnValidateCredentials(
                state.email.text.toString(),
                state.password.text.toString()
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
                deviceInfo.isTablet -> {
                    ContentTablet(
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
                deviceInfo.isLandscape -> {
                    ContentLandscape(
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
                    ContentPortrait(
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
private fun LoginPreview() {
    NoteMarkMilestoneTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}
