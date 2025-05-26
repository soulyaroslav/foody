package com.jerdoul.foody.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.presentation.splash.SplashScreen

enum class KeyboardState {
    SHOW, HIDE
}

@Composable
fun NavGraph(
    modifier: Modifier,
    navController: NavHostController,
    navigator: Navigator
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = navigator.startDestination
    ) {
        composable<Destination.Splash> {
            SplashScreen(navigator = navigator)
        }
//        composable<Destination.Camera> {
//            val viewModel = hiltViewModel<CameraViewModel>()
//            val state by viewModel.state.collectAsStateWithLifecycle()
//            CameraScreen(
//                state = state,
//                onEvent = viewModel::onEvent
//            )
//        }
//        composable<Destination.FriendsNearby> {
//            FriendNearbyScreen(navigator = navigator)
//        }
    }
}


