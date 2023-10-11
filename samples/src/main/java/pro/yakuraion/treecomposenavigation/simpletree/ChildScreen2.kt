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
fun ChildScreen2(
    arg1: Int,
    arg2: Float,
    arg3: Char,
    arg4: String,
    nArg1: Int?,
    nArg2: Float?,
    nArg3: Char?,
    nArg4: String?,
    onGoToChildScreen3Click: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = """
            ChildScreen2:
            arg1 = $arg1,
            arg2 = $arg2,
            arg3 = $arg3,
            arg4 = $arg4,
            nArg1 = $nArg1,
            nArg2 = $nArg2,
            nArg3 = $nArg3,
            nArg4 = $nArg4,
        """.trimIndent(),
        )
        Button(onClick = onGoToChildScreen3Click) {
            Text(text = "Go to ChildScreen3")
        }
    }
}
