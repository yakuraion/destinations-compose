package pro.yakuraion.destinationscompose.simpletree

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.parcelize.Parcelize
import pro.yakuraion.destinationscompose.core.DestinationOptionalParameter
import pro.yakuraion.destinationscompose.core.DestinationScreen
import java.io.Serializable

@DestinationScreen
@Composable
fun ChildScreen2(
    arg1: Int,
    arg2: Float,
    @DestinationOptionalParameter(defaultValue = "\'M\'") arg3: Char,
    arg4: String,
    arg5: SerializableParameter,
    arg6: ParcelableParameter,
    @DestinationOptionalParameter(defaultValue = "10") nArg1: Int?,
    nArg2: Float?,
    nArg3: Char?,
    @DestinationOptionalParameter(defaultValue = "null") nArg4: String?,
    nArg5: SerializableParameter?,
    nArg6: ParcelableParameter?,
    onGoToChildScreen3Click: () -> Unit,
) {
    ParcelableParameter::class.java.classLoader
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = """
            ChildScreen2:
            arg1 = $arg1,
            arg2 = $arg2,
            arg3 = $arg3,
            arg4 = $arg4,
            arg5 = $arg5,
            arg6 = $arg6,
            nArg1 = $nArg1,
            nArg2 = $nArg2,
            nArg3 = $nArg3,
            nArg4 = $nArg4,
            nArg5 = $nArg5,
            nArg6 = $nArg6,
        """.trimIndent(),
        )
        Button(onClick = onGoToChildScreen3Click) {
            Text(text = "Go to ChildScreen3")
        }
    }
}

data class SerializableParameter(
    val first: Int,
    val second: String,
) : Serializable

@Parcelize
data class ParcelableParameter(
    val first: Int,
    val second: String,
) : Parcelable
