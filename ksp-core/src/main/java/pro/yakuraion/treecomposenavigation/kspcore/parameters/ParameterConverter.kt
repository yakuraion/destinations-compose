package pro.yakuraion.treecomposenavigation.kspcore.parameters

import com.google.devtools.ksp.symbol.KSValueParameter

interface ParameterConverter<T : Parameter> {

    fun convert(ksParameter: KSValueParameter): T?
}
