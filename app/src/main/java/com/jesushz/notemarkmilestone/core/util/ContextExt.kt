package com.jesushz.notemarkmilestone.core.util

import android.content.Context
import android.content.res.Configuration
import com.jesushz.notemarkmilestone.R

fun Context.isTablet(): Boolean {
    return resources.getBoolean(R.bool.is_tablet)
}

fun Context.isLandscape(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}
