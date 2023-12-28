package io.github.yakuraion.destinationscompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.yakuraion.destinationscompose.simplegraph.getSimpleRootScreenRoute
import io.github.yakuraion.destinationscompose.simplegraph.simpleRootScreenComposable
import io.github.yakuraion.destinationscompose.viewmodeltree.getViewModelTreeScreenRoute
import io.github.yakuraion.destinationscompose.viewmodeltree.viewModelTreeScreenComposable

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "picker",
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                ) {
                    composable(route = "picker") {
                        Picker(onClick = { example ->
                            val route = when (example) {
                                Example.SIMPLE -> getSimpleRootScreenRoute()
                                Example.VIEW_MODEL -> getViewModelTreeScreenRoute()
                            }
                            navController.navigate(route)
                        })
                    }
                    simpleRootScreenComposable()
                    viewModelTreeScreenComposable()
                }
            }
        }
    }

    @Composable
    private fun Picker(
        onClick: (Example) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(Example.values()) { example ->
                Button(onClick = { onClick(example) }) {
                    Text(text = example.name)
                }
            }
        }
    }

    enum class Example {
        SIMPLE,
        VIEW_MODEL,
    }
}
