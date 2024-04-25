package io.github.yakuraion.destinationscompose.viewmodeltree.screen2

import androidx.compose.runtime.Composable
import io.github.yakuraion.destinationscompose.base.BaseChildScreen
import io.github.yakuraion.destinationscompose.core.DestinationScreen
import org.koin.androidx.compose.koinViewModel

@DestinationScreen
@Composable
fun ViewModelScreen2(
    onCallback: () -> Unit,
    viewModel: ViewModel2 = koinViewModel(),
) {
    BaseChildScreen(
        name = "ViewModelScreen2",
        params = listOf(
            "arg1" to viewModel.arg1,
            "arg2" to viewModel.arg2,
            "arg3" to viewModel.arg3,
            "arg4" to viewModel.arg4,
            "url" to viewModel.url,
        )
    )
}
