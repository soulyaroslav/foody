package com.jerdoul.foody.ui.utils

import android.content.Context
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SnackbarAction(
    val label: String,
    val action: () -> Unit
)

sealed interface SnackbarEvent {
    data class Message(
        val message: String,
        val action: SnackbarAction? = null
    ): SnackbarEvent

    data class UiTextMessage(
        val message: UiText,
        val action: SnackbarAction? = null
    ): SnackbarEvent
}

fun SnackbarEvent.asMessage(context: Context): String {
    return when (this) {
        is SnackbarEvent.Message -> message
        is SnackbarEvent.UiTextMessage -> message.asString(context)
    }
}

fun SnackbarEvent.asAction(): SnackbarAction? {
    return when (this) {
        is SnackbarEvent.Message -> action
        is SnackbarEvent.UiTextMessage -> action
    }
}

object SnackbarController {

    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun showEvent(event: SnackbarEvent) {
        _events.send(event)
    }
}