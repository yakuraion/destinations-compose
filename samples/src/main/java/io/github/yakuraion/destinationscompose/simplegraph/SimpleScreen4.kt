package io.github.yakuraion.destinationscompose.simplegraph

import android.util.Base64
import androidx.compose.runtime.Composable
import io.github.yakuraion.destinationscompose.base.BaseChildScreen
import io.github.yakuraion.destinationscompose.core.DestinationScreen

@DestinationScreen
@Composable
fun SimpleScreen4(
    url: String,
) {
    BaseChildScreen(
        name = "SimpleScreen4",
        params = listOf(
            "url" to url,
        )
    )
}
