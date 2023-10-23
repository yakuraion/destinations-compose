package pro.yakuraion.destinationscompose.viewmodeltree.screen3

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import pro.yakuraion.destinationscompose.base.BaseChildScreen
import pro.yakuraion.destinationscompose.core.DestinationScreen

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
