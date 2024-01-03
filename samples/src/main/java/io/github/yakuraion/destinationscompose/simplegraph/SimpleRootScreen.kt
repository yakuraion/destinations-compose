package io.github.yakuraion.destinationscompose.simplegraph

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

private val CHILDREN = ScreenChildren(
    values = listOf(
        ScreenChild("SimpleScreen1"),
        ScreenChild("SimpleScreen2"),
        ScreenChild("SimpleScreen3"),
        ScreenChild("SimpleScreen4"),
    )
)

@DestinationScreen
@Composable
fun SimpleRootScreen() {
    val navController = rememberNavController()
    BaseRootScreen(
        children = CHILDREN,
        onChildClick = { child ->
            when (child.name) {
                "SimpleScreen1" -> {
                    navController.navigateToSimpleScreen1(
                        arg1 = 1,
                        arg2 = 2f,
                        arg3 = "arg3String",
                        arg4 = "arg4String",
                    )
                }

                "SimpleScreen2" -> {
                    navController.navigateToSimpleScreen2(
                        arg1 = 1,
                        arg2 = null,
                        arg3 = 3.toULong(),
                        arg4 = "4",
                        arg5 = '5',
                    )

                }

                "SimpleScreen3" -> {
                    navController.navigateToSimpleScreen3(
                        arg1 = SerializableParameter(1, "2"),
                        arg2 = SerializableParameter(1, "2"),
                        arg3 = null,
                        arg4 = ParcelableParameter(1, "2"),
                        arg5 = ParcelableParameter(1, "2"),
                        arg6 = null,
                    )

                }

                "SimpleScreen4" -> {
                    navController.navigateToSimpleScreen4()

                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = getSimpleScreen1RouteScheme(),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            simpleScreen1Composable(
                onCallback1 = {},
                onCallback2 = { _, _ -> true },
            )
            simpleScreen2Composable()
            simpleScreen3Composable()
            simpleScreen4Composable()
        }
    }
}
