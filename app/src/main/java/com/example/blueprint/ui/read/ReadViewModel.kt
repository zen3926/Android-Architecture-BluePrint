package com.example.blueprint.ui.read

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.text.inSpans
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReadViewModel : ViewModel() {
    private val _message = MutableLiveData<CharSequence>()
    val message: LiveData<CharSequence>
        get() = _message

    init {
        viewModelScope.launch {
            val info = withContext(Dispatchers.IO) {
                buildReadMeInfo()
            }
            _message.value = info
        }
    }

    private fun buildReadMeInfo(): CharSequence {
        return SpannableStringBuilder().apply {
            color(Color.BLUE) {
                bold { append("Android Architecture BluePrint\n") }
            }
            append("ZeNan Gao | gzn3926@gmail.com\n")
            append("https://github.com/zen3926/Android-Architecture-BluePrint.git\n\n")


            append("This serves as a demo for various Android components, features and MVVM & Repository patterns:\n")
            buildContent()
            append("\n")

            append("Other 3rd-party dependent libraries used:\n")
            buildAdditionalLibrariesUsed()
            append("\n")

            append("Other note:\n")
            buildOtherNotes()

            append("\n")
            append("References:\n")
            buildReferences()
        }
    }

    private fun SpannableStringBuilder.buildContent() {
        val contents = listOf(
            "Android App Components: [Activity, Services, Broadcast Receivers]",
            "Android Navigation Component: [Navigation Graph, Controller]",
            "Android Architecture Components: [View Binding, Data Binding, LiveData, Room, Work Manager, ViewModel, SaveStateHandle]",
            "View Components: [RecyclerView, CardView, ConstraintLayout, TextInputLayout, and many other Material View Components]",
            "Additional Features: [App Widget, App Shortcut, Notification, Security Storage, Biometric Authentication]",
            "Many more"
        )

        buildBullets(contents)
    }

    private fun SpannableStringBuilder.buildAdditionalLibrariesUsed() {
        val contents = listOf(
            "Network: Retrofit 2",
            "Json parsing: Moshi",
            "Logging: Timber",
            "Image Loading: Glide",
            "Unit Testing: JUnit, Espresso, Truth, Mockk"
        )
        buildBullets(contents)
    }

    private fun SpannableStringBuilder.buildOtherNotes() {
        val contents = listOf(
            "Kotlin and Coroutines",
            "MVVM, Repository, and other patterns"
        )
        buildBullets(contents)
    }

    private fun SpannableStringBuilder.buildReferences() {
        val contents = listOf(
            "Guide to app architecture: https://developer.android.com/jetpack/docs/guide",
            "Android Architecture Components: https://developer.android.com/topic/libraries/architecture",
            "Navigation: https://developer.android.com/guide/navigation"
        )
        buildBullets(contents)
    }

    private fun SpannableStringBuilder.buildBullets(contents: List<String>) {
        contents.forEach {
            inSpans(BulletSpan(30, Color.BLUE)) { append(it) }
            append("\n")
        }
    }
}
