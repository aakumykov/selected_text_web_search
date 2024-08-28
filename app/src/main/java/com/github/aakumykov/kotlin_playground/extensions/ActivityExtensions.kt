package com.github.aakumykov.kotlin_playground.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat.startActivity

fun Activity.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Activity.showToast(@StringRes strRes: Int) {
    showToast(getString(strRes))
}

fun Activity.showAppProperties() {
    val uri = Uri.parse("package:$packageName")
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
    if (intent.resolveActivity(packageManager) != null) { startActivity(intent) }
}