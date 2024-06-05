package com.github.aakumykov.kotlin_playground

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AppCompatActivity


fun Activity.openAccessibilitySettingsIfDisabled() {
    if (!isAccessibilityServiceEnabled())
        openAccessibilitySettings()
}

fun Activity.isAccessibilityServiceEnabled(): Boolean {
    return (getSystemService(AppCompatActivity.ACCESSIBILITY_SERVICE) as AccessibilityManager)
        .getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK)
        .any { accessibilityServiceInfo ->
            accessibilityServiceInfo.id.equals(getString(R.string.accessibilityservice_id))
        }
}

fun Activity.openAccessibilitySettings() {
    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
}

fun Activity.showAppProperties() {
    val uri = Uri.parse("package:$packageName")
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
    if (intent.resolveActivity(packageManager) != null) { startActivity(intent) }
}