package pro.yakuraion.destinationscompose.viewmodeltree.screen2

import androidx.lifecycle.ViewModel
import pro.yakuraion.destinationscompose.parameters.ParcelableParameter
import pro.yakuraion.destinationscompose.parameters.SerializableParameter

class ViewModel2(
    val arg1: ParcelableParameter,
    val arg2: ParcelableParameter?,
    val arg3: SerializableParameter,
    val arg4: SerializableParameter?,
) : ViewModel()
