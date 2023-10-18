package pro.yakuraion.treecomposenavigation.simpletree

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import pro.yakuraion.treecomposenavigation.core.DestinationScreen

@DestinationScreen
@Composable
fun SimpleTreeScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = getChildScreen1StartDestination(),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        childScreen1Composable(
            onGoToChildScreen2Click = {
                navController.navigateToChildScreen2(
                    arg1 = 1,
                    arg2 = 2f,
                    arg3 = '3',
                    arg4 = "4",
                    arg5 = SerializableParameter(first = 42, second = "Sense of life"),
                    arg6 = ParcelableParameter(first = 16, second = "Potatoes"),
                    nArg1 = null,
                    nArg2 = 13f,
                    nArg3 = null,
                    nArg4 = "foo",
                    nArg5 = null,
                    nArg6 = null,
                )
            }
        )
        childScreen2Composable(
            onGoToChildScreen3Click = {
                navController.navigateToChildScreen3()
            }
        )
        childScreen3Composable()
    }
}
