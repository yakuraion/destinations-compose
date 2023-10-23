package pro.yakuraion.destinationscompose.simplegraph

import androidx.compose.runtime.Composable
import pro.yakuraion.destinationscompose.base.BaseChildScreen
import pro.yakuraion.destinationscompose.core.DestinationScreen

@DestinationScreen
@Composable
fun SimpleScreen2(
    arg1: Int,
    arg2: Int?,
    arg3: ULong,
    arg4: String,
    arg5: Char?,
) {
    BaseChildScreen(
        name = "SimpleScreen2",
        params = listOf(
            "arg1" to arg1,
            "arg2" to arg2,
            "arg3" to arg3,
            "arg4" to arg4,
            "arg5" to arg5,
        )
    )
}
