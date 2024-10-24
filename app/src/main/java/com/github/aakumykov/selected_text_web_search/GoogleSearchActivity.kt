package com.github.aakumykov.selected_text_web_search

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.github.aakumykov.selected_text_web_search.git.R

const val GOOGLE_SEARCH_BASE_URL = "https://www.google.com/search?q="

abstract class GoogleSearchActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        processIntentAndFinish(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        processInputIntent(intent)
    }

    private fun processIntentAndFinish(intent: Intent?) {
        processInputIntent(intent)
        finish()
    }

    private fun processInputIntent(intent: Intent?) {
        when(intent?.action) {
            Intent.ACTION_PROCESS_TEXT -> processSelectedText(intent)
            Intent.ACTION_SEND -> processSendedText(intent)
            else -> reportNothingToSearch()
        }
    }

    private fun processSendedText(intent: Intent) {
        processTextFromIntent(intent, Intent.EXTRA_TEXT)
    }

    private fun processSelectedText(intent: Intent) {
        processTextFromIntent(intent, Intent.EXTRA_PROCESS_TEXT)
    }

    private fun processTextFromIntent(intent: Intent, extraKey: String) {
        intent.getCharSequenceExtra(extraKey)?.toString()?.also { text ->
            startSearchingTheWeb(text)
        } ?: run {
            reportNothingToSearch()
        }
    }

    private fun reportNothingToSearch() {
        Toast.makeText(this, R.string.where_is_no_text_to_search, Toast.LENGTH_SHORT).show()
    }

    protected fun startSearchingTheWeb(text: String) {
        Intent(Intent.ACTION_VIEW, text2searchUri(text)).also {
            startActivity(it)
        }
    }

    abstract fun text2searchUri(text: String): Uri
}