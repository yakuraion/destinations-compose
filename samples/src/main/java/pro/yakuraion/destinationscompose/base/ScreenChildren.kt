package pro.yakuraion.destinationscompose.base

import androidx.compose.runtime.Stable

@Stable
data class ScreenChildren(val values: List<ScreenChild>)

data class ScreenChild(val name: String)
