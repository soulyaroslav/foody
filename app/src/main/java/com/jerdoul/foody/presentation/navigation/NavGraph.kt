package com.jerdoul.foody.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jerdoul.foody.presentation.auth.AuthorizationScreen
import com.jerdoul.foody.presentation.auth.AuthorizationViewModel
import com.jerdoul.foody.presentation.cart.CartScreen
import com.jerdoul.foody.presentation.cart.CartViewModel
import com.jerdoul.foody.presentation.dashboard.DashboardScreen
import com.jerdoul.foody.presentation.dashboard.DashboardViewModel
import com.jerdoul.foody.presentation.details.DetailsScreen
import com.jerdoul.foody.presentation.details.DetailsViewModel
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.presentation.search.SearchScreen
import com.jerdoul.foody.presentation.search.SearchViewModel
import com.jerdoul.foody.presentation.splash.SplashScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavGraph(
    modifier: Modifier,
    navController: NavHostController,
    navigator: Navigator
) {
    SharedTransitionLayout {
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
                    animatedVisibilityScope = this,
                    onAction = viewModel::onAction
                )
            }
            composable<Destination.SearchScreen> {
                val viewModel = hiltViewModel<SearchViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                SearchScreen(
                    navigator = navigator,
                    state = state,
                    animatedVisibilityScope = this,
                    onAction = viewModel::onAction
                )
            }
            composable<Destination.DetailsScreen> {
                val viewModel = hiltViewModel<DetailsViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                DetailsScreen(
                    navigator = navigator,
                    state = state,
                    animatedVisibilityScope = this,
                    onAction = viewModel::onAction
                )
            }
            composable<Destination.CartScreen> {
                val viewModel = hiltViewModel<CartViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                CartScreen(
                    navigator = navigator,
                    state = state,
                    onAction = viewModel::onAction
                )
            }
            composable<Destination.PaymentScreen> {

            }
        }
    }
}


