package com.jerdoul.foody.domain.network

sealed interface NetworkConnectionState {
    data object Available : NetworkConnectionState
    data object Lost : NetworkConnectionState
    data object Unavailable : NetworkConnectionState
}