package com.github.aakumykov.kotlin_playground

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.github.aakumykov.kotlin_playground.databinding.ActivityMainBinding

class WebSearchActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        intent?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()?.also { text ->
            binding.selectedTextView.text = text
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        val TAG: String = WebSearchActivity::class.java.simpleName
    }
}