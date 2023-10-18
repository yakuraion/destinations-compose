package pro.yakuraion.treecomposenavigation.viewmodeltree

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import pro.yakuraion.treecomposenavigation.core.DestinationScreen
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen1.getViewModelScreen1StartDestination
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen1.viewModelScreen1Composable
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen2.ViewModelParcelableParameter
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen2.ViewModelSerializableParameter
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen2.navigateToViewModelScreen2
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen2.viewModelScreen2Composable
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen3.navigateToViewModelScreen3
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen3.viewModelScreen3Composable

@DestinationScreen
@Composable
fun ViewModelTreeScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = getViewModelScreen1StartDestination(),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        viewModelScreen1Composable(
            onGoToViewModelScreen2Click = {
                navController.navigateToViewModelScreen2(
                    arg1 = 12L,
                    arg2 = null,
                    arg4 = ViewModelSerializableParameter(11, true),
                    arg5 = ViewModelParcelableParameter(3.14f, null),
                )
            }
        )
        viewModelScreen2Composable(
            onGoToViewModelScreen3Click = {
                navController.navigateToViewModelScreen3()
            }
        )
        viewModelScreen3Composable()
    }
}
