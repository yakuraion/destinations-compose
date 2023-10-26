package io.github.yakuraion.destinationscompose.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BaseChildScreen(name: String, params: List<Pair<Any, Any?>>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = name)
        Spacer(modifier = Modifier.height(32.dp))
        for (param in params) {
            Text(
                text = "${param.first} = ${param.second}",
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 2.dp,
                )
            )
        }
    }
}
