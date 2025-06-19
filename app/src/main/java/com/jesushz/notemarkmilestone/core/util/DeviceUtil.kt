package com.jesushz.notemarkmilestone.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun isTablet(): Boolean {
    val context = LocalContext.current
    return context.isTablet()
}

@Composable
fun isLandscape(): Boolean {
    val context = LocalContext.current
    return context.isLandscape()
}
