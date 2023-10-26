package io.github.yakuraion.destinationscompose.simplegraph

import androidx.compose.runtime.Composable
import io.github.yakuraion.destinationscompose.base.BaseChildScreen
import io.github.yakuraion.destinationscompose.core.DestinationOptionalParameter
import io.github.yakuraion.destinationscompose.core.DestinationScreen

@DestinationScreen
@Composable
fun SimpleScreen1(
    @DestinationOptionalParameter(defaultValue = "10") arg1: Int,
    @DestinationOptionalParameter(defaultValue = "null") arg2: Float?,
    @DestinationOptionalParameter(defaultValue = "\"Hello\"") arg3: String?,
    @DestinationOptionalParameter(defaultValue = "null") arg4: String?,
    onCallback1: () -> Unit,
    onCallback2: (Int, String?) -> Boolean?,
) {
    BaseChildScreen(
        name = "SimpleScreen1",
        params = listOf(
            "arg1" to arg1,
            "arg2" to arg2,
            "arg3" to arg3,
            "arg4" to arg4,
        )
    )
}
