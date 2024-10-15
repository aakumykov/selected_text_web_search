package com.github.aakumykov.kotlin_playground

import android.content.Intent
import android.net.Uri
import android.widget.Toast

class ExactGoogleSearchActivity : GoogleSearchActivity() {

    private fun processInputIntent(intent: Intent?) {
        intent?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()?.also { text ->
            searchTheWeb(text)
        } ?: run {
            Toast.makeText(this, R.string.where_is_no_text_to_search, Toast.LENGTH_SHORT).show()
        }
    }

    override fun text2searchUri(text: String): Uri {
        val quotedText = "\"$text\""
        return Uri.parse(
            GOOGLE_SEARCH_BASE_URL
                + quotedText
                + GOOGLE_NO_MODIFY_FLAG
        )
    }

    companion object {
        val TAG: String = GoogleSearchActivity::class.java.simpleName
        const val GOOGLE_NO_MODIFY_FLAG = "&nfpr=1"
    }
}