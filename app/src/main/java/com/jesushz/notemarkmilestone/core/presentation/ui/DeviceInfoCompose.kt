package com.jesushz.notemarkmilestone.core.presentation.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.booleanResource
import com.jesushz.notemarkmilestone.R

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun rememberDeviceInfo(): DeviceInfo {
    val activity = LocalActivity.current
    val configuration = LocalConfiguration.current

    val isTablet = booleanResource(R.bool.is_tablet)
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isTablet) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    return remember(isTablet, isLandscape) {
        DeviceInfo(isTablet, isLandscape)
    }
}
