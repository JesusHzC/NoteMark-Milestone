package com.jesushz.notemarkmilestone.core.presentation.ui

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Phone", device = Devices.PIXEL_6A, showSystemUi = true)
@Preview(name = "Phone Landscape",
    device = "spec:parent=medium_phone,orientation=landscape", showSystemUi = true)
@Preview(name = "Tablet",
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait", showSystemUi = true)
//@Preview(name = "Tablet Landscape", device = Devices.TABLET, showSystemUi = true)
annotation class NoteMarkPreview