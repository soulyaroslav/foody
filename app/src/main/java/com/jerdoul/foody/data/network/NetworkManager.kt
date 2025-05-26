package com.jerdoul.foody.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.jerdoul.foody.domain.network.NetworkConnectionState
import com.jerdoul.foody.domain.network.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkManagerImpl @Inject constructor(context: Context) : NetworkManager {
    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override val currentConnectivityState: NetworkConnectionState
        get() = getCurrentNetworkState()

    override fun observeConnectivityAsFlow(): Flow<NetworkConnectionState> = callbackFlow {
        val callback = networkCallback { connectionState ->
            trySend(connectionState)
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, callback)
        val currentState = getCurrentNetworkState()
        trySend(currentState)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    private fun networkCallback(callback: (NetworkConnectionState) -> Unit) = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) = callback(NetworkConnectionState.Available)
        override fun onLost(network: Network) = callback(NetworkConnectionState.Lost)
        override fun onUnavailable() = callback(NetworkConnectionState.Unavailable)
    }

    private fun getCurrentNetworkState(): NetworkConnectionState {
        val network = connectivityManager.activeNetwork

        val isConnected = connectivityManager.getNetworkCapabilities(network)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false

        return if (isConnected) NetworkConnectionState.Available else NetworkConnectionState.Unavailable
    }
}

@Composable
fun rememberConnectivityState(manager: NetworkManager): State<NetworkConnectionState> {
    val context = LocalContext.current
    val connectivityState = remember { mutableStateOf(manager.currentConnectivityState) }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                manager.observeConnectivityAsFlow().collect { newState ->
                    connectivityState.value = newState
                }
            }
        }
    }

    return connectivityState
}