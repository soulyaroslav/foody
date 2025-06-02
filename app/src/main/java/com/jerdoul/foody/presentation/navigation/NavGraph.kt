package com.jerdoul.foody.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jerdoul.foody.presentation.auth.AuthorizationScreen
import com.jerdoul.foody.presentation.auth.AuthorizationViewModel
import com.jerdoul.foody.presentation.dashboard.DashboardScreen
import com.jerdoul.foody.presentation.dashboard.DashboardViewModel
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.presentation.search.SearchScreen
import com.jerdoul.foody.presentation.search.SearchViewModel
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
        composable<Destination.AuthorizationScreen> {
            val viewModel = hiltViewModel<AuthorizationViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            AuthorizationScreen(
                navigator = navigator,
                state = state,
                onAction = viewModel::onAction
            )
        }
        composable<Destination.DashboardScreen> {
            val viewModel = hiltViewModel<DashboardViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            DashboardScreen(
                navigator = navigator,
                state = state,
                onAction = viewModel::onAction
            )
        }
        composable<Destination.SearchScreen> {
            val viewModel = hiltViewModel<SearchViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            SearchScreen(
                navigator = navigator,
                state = state,
                onAction = viewModel::onAction
            )
        }
    }
}


