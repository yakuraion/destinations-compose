package pro.yakuraion.treecomposenavigation.viewmodeltree.screen2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import pro.yakuraion.treecomposenavigation.core.DestinationScreen

@DestinationScreen
@Composable
fun ViewModelScreen2(
    onGoToViewModelScreen3Click: () -> Unit,
    viewModel: VMChildScreen2ViewModel = koinViewModel(),
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = """
            ViewModelScreen2:
            arg1 = ${viewModel.arg1},
            arg2 = ${viewModel.arg2},
            arg3 = ${viewModel.arg3},
            arg4 = ${viewModel.arg4},
            arg5 = ${viewModel.arg5},
        """.trimIndent(),
        )
        Button(onClick = onGoToViewModelScreen3Click) {
            Text(text = "Go to ViewModelScreen3")
        }
    }
}
