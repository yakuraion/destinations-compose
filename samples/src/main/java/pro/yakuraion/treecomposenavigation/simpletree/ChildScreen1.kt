package pro.yakuraion.treecomposenavigation.simpletree

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pro.yakuraion.treecomposenavigation.core.DestinationScreen

@DestinationScreen
@Composable
fun ChildScreen1(
    onGoToChildScreen2Click: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("ChildScreen1")
        Button(onClick = onGoToChildScreen2Click) {
            Text(text = "Go to ChildScreen2")
        }
    }
}
