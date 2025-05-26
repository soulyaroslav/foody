package com.jerdoul.foody.ui.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SnackbarAction(
    val label: String,
    val action: () -> Unit
)

data class SnackbarEvent(
    val message: String,
    val action: SnackbarAction? = null
)

object SnackbarController {

    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun showEvent(event: SnackbarEvent) {
        _events.send(event)
    }
}