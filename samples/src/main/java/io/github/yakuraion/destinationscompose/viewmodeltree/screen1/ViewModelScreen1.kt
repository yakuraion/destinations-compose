package io.github.yakuraion.destinationscompose.viewmodeltree.screen1

import androidx.compose.runtime.Composable
import io.github.yakuraion.destinationscompose.base.BaseChildScreen
import io.github.yakuraion.destinationscompose.core.DestinationScreen
import org.koin.androidx.compose.koinViewModel

@DestinationScreen
@Composable
fun ViewModelScreen1(
    onCallback: () -> Unit,
    viewModel: ViewModel1 = koinViewModel(),
) {
    BaseChildScreen(
        name = "ViewModelScreen1",
        params = listOf(
            "arg1" to viewModel.arg1,
            "arg2" to viewModel.arg2,
            "diString" to viewModel.diString,
        )
    )
}
