package com.jerdoul.foody.domain.network

import kotlinx.coroutines.flow.Flow

interface NetworkManager {
    val currentConnectivityState: NetworkConnectionState
    fun observeConnectivityAsFlow(): Flow<NetworkConnectionState>
}