package pro.yakuraion.destinationscompose.viewmodeltree.screen1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import pro.yakuraion.destinationscompose.core.DestinationScreen

@DestinationScreen
@Composable
fun ViewModelScreen1(
    onGoToViewModelScreen2Click: () -> Unit,
    viewModel: VMChildScreen1ViewModel = koinViewModel(),
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("ViewModelScreen1")
        Button(onClick = onGoToViewModelScreen2Click) {
            Text(text = "Go to ViewModelScreen2")
        }
    }
}
