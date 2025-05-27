package com.jerdoul.foody

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.jerdoul.foody.data.network.rememberConnectivityState
import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.type.NetworkError
import com.jerdoul.foody.domain.network.NetworkConnectionState
import com.jerdoul.foody.domain.network.NetworkManager
import com.jerdoul.foody.domain.usecase.SignInUseCase
import com.jerdoul.foody.presentation.asErrorUiText
import com.jerdoul.foody.presentation.asUiText
import com.jerdoul.foody.presentation.navigation.NavGraph
import com.jerdoul.foody.presentation.navigation.NavigationAction
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.ui.theme.FoodyTheme
import com.jerdoul.foody.ui.utils.SnackbarController
import com.jerdoul.foody.ui.utils.SnackbarEvent
import com.jerdoul.foody.ui.utils.rememberWindowSizeDetails
import com.jerdoul.foody.utils.extensions.updateLightStatusBarAppearance
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var networkManager: NetworkManager

    @Inject
    lateinit var signInUseCase: SignInUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.updateLightStatusBarAppearance(true)
        setContent {
            val window = rememberWindowSizeDetails()
            FoodyTheme(window) {
                val context = LocalContext.current
                val navController = rememberNavController()
                val keyboardController = LocalSoftwareKeyboardController.current
                val networkState by rememberConnectivityState(networkManager)
                val snackbarHosState = remember {
                    SnackbarHostState()
                }

                val scope = rememberCoroutineScope()

                LaunchedEffect(key1 = networkState) {
                    val params = SignInUseCase.Params.toParams("", "password")
                    when(val result = signInUseCase(params)) {
                        is Result.Error -> {
                            val error = result.asErrorUiText()
                            Log.d("TTTT", "result: ${error.asString(context)}")
                        }
                        is Result.Success -> {
                            Log.d("TTTT", "result: ${result.data}")
                        }
                    }
                    if (networkState != NetworkConnectionState.Available) {
                        val noConnectionUiText = NetworkError.Http.NoNetworkConnection.asUiText()
                        val event = SnackbarEvent(noConnectionUiText.asString(context))
                        SnackbarController.showEvent(event)
                    }
                }

                ObserveAsEvents(flow = SnackbarController.events, snackbarHosState) { event ->
                    scope.launch {
                        snackbarHosState.currentSnackbarData?.dismiss()
                        val result = snackbarHosState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action?.label,
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            event.action?.action?.invoke()
                        }
                    }
                }

                ObserveAsEvents(flow = navigator.navigationActions) { action ->
                    when (action) {
                        is NavigationAction.Navigate -> {
                            navController.navigate(action.destination) {
                                action.navOptions(this)
                            }
                        }

                        is NavigationAction.NavigateUp -> {
                            navController.navigateUp()
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0.dp),
                    containerColor = MaterialTheme.colorScheme.background,
                    snackbarHost = { SnackbarHost(hostState = snackbarHosState) }
                ) { innerPadding ->
                    NavGraph(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        navigator = navigator,
//                        onKeyboardStateChanged = { state ->
//                            when (state) {
//                                KeyboardState.SHOW -> keyboardController?.show()
//                                KeyboardState.HIDE -> keyboardController?.hide()
//                            }
//                        }
                    )
                }
            }
        }
    }
}

@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2, flow) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect { onEvent(it) }
            }
        }
    }
}