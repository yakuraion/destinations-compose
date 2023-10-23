package pro.yakuraion.destinationscompose.viewmodeltree.screen1

import androidx.lifecycle.ViewModel
import pro.yakuraion.destinationscompose.core.DestinationOptionalParameter
import pro.yakuraion.destinationscompose.core.NotDestinationParameter
import pro.yakuraion.destinationscompose.viewmodeltree.model.SomeUseCase

class ViewModel1(
    @DestinationOptionalParameter("1") val arg1: Int,
    @DestinationOptionalParameter("2f") val arg2: Float?,
    @NotDestinationParameter val diString: String,
    val someUseCase: SomeUseCase,
) : ViewModel()
