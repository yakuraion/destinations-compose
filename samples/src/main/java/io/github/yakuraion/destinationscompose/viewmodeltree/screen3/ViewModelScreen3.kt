package io.github.yakuraion.destinationscompose.viewmodeltree.screen3

import androidx.compose.runtime.Composable
import io.github.yakuraion.destinationscompose.base.BaseChildScreen
import io.github.yakuraion.destinationscompose.core.DestinationScreen
import org.koin.androidx.compose.koinViewModel

@DestinationScreen
@Composable
fun ViewModelScreen3(
    viewModel: ViewModel3 = koinViewModel(),
) {
    BaseChildScreen(
        name = "ViewModelScreen3",
        params = listOf()
    )
}
