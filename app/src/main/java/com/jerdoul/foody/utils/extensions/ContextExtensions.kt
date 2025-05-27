package com.jerdoul.foody.utils.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.Window
import androidx.core.view.WindowCompat

fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
    }
    startActivity(intent)
}

fun Window.updateLightStatusBarAppearance(isLight: Boolean) {
    WindowCompat.getInsetsController(this, decorView)
        .isAppearanceLightStatusBars = isLight
}

