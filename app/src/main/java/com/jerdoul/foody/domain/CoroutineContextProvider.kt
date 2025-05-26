package com.jerdoul.foody.domain

import kotlin.coroutines.CoroutineContext

interface CoroutineContextProvider {
    val main: CoroutineContext
    val io: CoroutineContext
    val default: CoroutineContext
}