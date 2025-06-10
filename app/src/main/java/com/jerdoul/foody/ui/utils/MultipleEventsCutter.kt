package com.jerdoul.foody.ui.utils

import androidx.compose.runtime.Stable

internal interface MultipleEventsCutter {
    fun processEvent(event: () -> Unit)

    companion object
}

internal fun MultipleEventsCutter.Companion.get(): MultipleEventsCutter =
    MultipleEventsCutterImpl()

@Stable
private class MultipleEventsCutterImpl : MultipleEventsCutter {
    private val now: Long
        get() = System.currentTimeMillis()

    private var lastEventTimeMs: Long = 0

    override fun processEvent(event: () -> Unit) {
        val currentTime = now
        if (currentTime - lastEventTimeMs >= 500L) {
            lastEventTimeMs = currentTime
            event.invoke()
        }
    }
}