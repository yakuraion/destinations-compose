package pro.yakuraion.destinationscompose.viewmodeltree.screen2

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import pro.yakuraion.destinationscompose.base.BaseChildScreen
import pro.yakuraion.destinationscompose.core.DestinationScreen

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
        )
    )
}
