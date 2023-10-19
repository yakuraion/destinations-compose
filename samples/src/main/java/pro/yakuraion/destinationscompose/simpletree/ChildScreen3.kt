package pro.yakuraion.destinationscompose.simpletree

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pro.yakuraion.destinationscompose.core.DestinationScreen

@DestinationScreen
@Composable
fun ChildScreen3() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("ChildScreen3")
    }
}
