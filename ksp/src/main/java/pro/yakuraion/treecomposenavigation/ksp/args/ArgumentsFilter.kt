package pro.yakuraion.treecomposenavigation.ksp.args

import com.google.devtools.ksp.symbol.KSValueParameter

interface ArgumentsFilter<T : Argument> {

    fun filter(parameters: List<KSValueParameter>): List<T>
}
