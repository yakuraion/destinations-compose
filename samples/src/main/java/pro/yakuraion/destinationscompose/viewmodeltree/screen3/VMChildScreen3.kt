package pro.yakuraion.destinationscompose.viewmodeltree.screen3

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import pro.yakuraion.destinationscompose.core.DestinationScreen

@DestinationScreen
@Composable
fun ViewModelScreen3(
    viewModel: VMChildScreen3ViewModel = koinViewModel(),
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("ViewModelScreen3")
    }
}
