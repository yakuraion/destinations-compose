package pro.yakuraion.destinationscompose.simplegraph

import androidx.compose.runtime.Composable
import pro.yakuraion.destinationscompose.base.BaseChildScreen
import pro.yakuraion.destinationscompose.core.DestinationScreen
import pro.yakuraion.destinationscompose.parameters.ParcelableParameter
import pro.yakuraion.destinationscompose.parameters.SerializableParameter

@DestinationScreen
@Composable
fun SimpleScreen3(
    arg1: SerializableParameter,
    arg2: SerializableParameter?,
    arg3: SerializableParameter?,
    arg4: ParcelableParameter,
    arg5: ParcelableParameter?,
    arg6: ParcelableParameter?,
) {
    BaseChildScreen(
        name = "SimpleScreen3",
        params = listOf(
            "arg1" to arg1,
            "arg2" to arg2,
            "arg3" to arg3,
            "arg4" to arg4,
            "arg5" to arg5,
            "arg6" to arg6,
        )
    )
}
