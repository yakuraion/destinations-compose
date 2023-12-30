package io.github.yakuraion.destinationscompose.viewmodeltree

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.yakuraion.destinationscompose.base.BaseRootScreen
import io.github.yakuraion.destinationscompose.base.ScreenChild
import io.github.yakuraion.destinationscompose.base.ScreenChildren
import io.github.yakuraion.destinationscompose.core.DestinationScreen
import io.github.yakuraion.destinationscompose.parameters.ParcelableParameter
import io.github.yakuraion.destinationscompose.parameters.SerializableParameter
import io.github.yakuraion.destinationscompose.viewmodeltree.screen1.getViewModelScreen1StartRoute
import io.github.yakuraion.destinationscompose.viewmodeltree.screen1.navigateToViewModelScreen1
import io.github.yakuraion.destinationscompose.viewmodeltree.screen1.viewModelScreen1Composable
import io.github.yakuraion.destinationscompose.viewmodeltree.screen2.navigateToViewModelScreen2
import io.github.yakuraion.destinationscompose.viewmodeltree.screen2.viewModelScreen2Composable
import io.github.yakuraion.destinationscompose.viewmodeltree.screen3.navigateToViewModelScreen3
import io.github.yakuraion.destinationscompose.viewmodeltree.screen3.viewModelScreen3Composable

private val CHILDREN = ScreenChildren(
    values = listOf(
        ScreenChild("ViewModelScreen1"),
        ScreenChild("ViewModelScreen2"),
        ScreenChild("ViewModelScreen3"),
    )
)

@DestinationScreen
@Composable
fun ViewModelTreeScreen() {
    val navController = rememberNavController()
    BaseRootScreen(
        children = CHILDREN,
        onChildClick = { child ->
            when (child.name) {
                "ViewModelScreen1" -> {
                    navController.navigateToViewModelScreen1(
                        arg1 = 1,
                        arg2 = 2f,
                    )
                }

                "ViewModelScreen2" -> {
                    navController.navigateToViewModelScreen2(
                        arg1 = ParcelableParameter(1, "2"),
                        arg2 = null,
                        arg3 = SerializableParameter(1, "2"),
                        arg4 = null,
                    )
                }

                "ViewModelScreen3" -> {
                    navController.navigateToViewModelScreen3()
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = getViewModelScreen1StartRoute(),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            viewModelScreen1Composable(
                onCallback = {}
            )
            viewModelScreen2Composable(
                onCallback = {}
            )
            viewModelScreen3Composable()
        }
    }
}
