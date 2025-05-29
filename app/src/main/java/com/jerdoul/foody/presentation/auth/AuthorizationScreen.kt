package com.jerdoul.foody.presentation.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jerdoul.foody.presentation.auth.login.LoginContent
import com.jerdoul.foody.presentation.auth.login.RegistrationContent
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.utils.extensions.clickableSingle
import com.jerdoul.foody.utils.extensions.fadeInAnimation
import com.jerdoul.foody.utils.extensions.verticalSlideInAnimation

@Composable
fun AuthorizationScreen(
    state: AuthorizationState,
    onAction: (AuthorizationAction) -> Unit,
    navigator: Navigator
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        val (tabBarRef, contentRef) = createRefs()
        val guideline = createGuidelineFromTop(.4f)
        AuthTabBar(
            modifier = Modifier
                .constrainAs(tabBarRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(contentRef.top)
                },
            tabs = listOf(
                NavItem.LOGIN,
                NavItem.SIGN_UP
            ),
            onTabChanged = {
                onAction(AuthorizationAction.ChangeNavItem(it))
            }
        )
        Box(
            modifier = Modifier
                .constrainAs(contentRef) {
                    top.linkTo(guideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            AnimatedContent(
                targetState = state.currentNavItem,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                },
                label = "TabTransition"
            ) { targetTab ->
                when (targetTab) {
                    NavItem.LOGIN -> {
                        LoginContent(
                            state = state.loginState,
                            onAction = onAction
                        )
                    }

                    NavItem.SIGN_UP -> {
                        RegistrationContent(
                            state = state.registrationState,
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AuthTabBar(modifier: Modifier, tabs: List<NavItem>, onTabChanged: (NavItem) -> Unit) {
    val indicatorOffset = remember { Animatable(0f) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    LaunchedEffect(selectedTabIndex) {
        onTabChanged(tabs[selectedTabIndex])
        val targetOffset = with(density) {
            val tabWidth = configuration.screenWidthDp.dp / tabs.size
            val offset = tabWidth - 25.dp - (tabWidth / 2)
            (selectedTabIndex * tabWidth.toPx()) + offset.toPx()
        }
        indicatorOffset.animateTo(
            targetValue = targetOffset,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    Column(
        modifier = modifier
            .verticalSlideInAnimation(
                initialOffsetY = -200f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = 100f
                )
            )
            .fadeInAnimation()
    ) {
        Row {
            tabs.forEach { tab ->
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickableSingle {
                            selectedTabIndex = tabs.indexOf(tab)
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(indicatorOffset.value.toInt(), 0) }
                    .width(width = 50.dp)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    )
            )
        }
    }
}