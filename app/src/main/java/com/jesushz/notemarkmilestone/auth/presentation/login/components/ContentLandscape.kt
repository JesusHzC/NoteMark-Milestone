package com.jesushz.notemarkmilestone.auth.presentation.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jesushz.notemarkmilestone.R
import com.jesushz.notemarkmilestone.auth.presentation.login.LoginAction
import com.jesushz.notemarkmilestone.auth.presentation.login.LoginState
import com.jesushz.notemarkmilestone.core.presentation.designsystem.components.NoteMarkButton
import com.jesushz.notemarkmilestone.core.presentation.designsystem.components.NoteMarkPasswordTextField
import com.jesushz.notemarkmilestone.core.presentation.designsystem.components.NoteMarkTextField

@Composable
internal fun ContentLandscape(
    modifier: Modifier = Modifier,
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    Row(
        modifier = modifier

    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = stringResource(R.string.log_in),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.log_in_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            NoteMarkTextField(
                state = state.email,
                hint = stringResource(R.string.email_hint),
                title = stringResource(R.string.email),
                startIcon = null,
                endIcon = null,
                error = null,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            NoteMarkPasswordTextField(
                state = state.password,
                hint = stringResource(R.string.password),
                title = stringResource(R.string.password),
                isPasswordVisible = state.showPassword,
                onTogglePasswordVisibility = {
                    onAction(LoginAction.OnTogglePasswordVisibility)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            NoteMarkButton(
                modifier = Modifier
                    .fillMaxWidth(),
                isEnable = state.loginIsEnable,
                onButtonClick = {
                    onAction(LoginAction.OnLogInClick)
                }
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    else -> {
                        Text(
                            text = stringResource(R.string.log_in),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                onClick = {
                    onAction(LoginAction.OnRegisterClick)
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(R.string.dont_have_an_account),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}