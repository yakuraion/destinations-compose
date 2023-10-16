package pro.yakuraion.treecomposenavigation.kspcore.parameters

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ParameterSpec

class InstantScreenParameter(ksParameter: KSValueParameter) : ScreenParameter(ksParameter) {

    fun getNavigationParameterSpec(): ParameterSpec {
        return ParameterSpec
            .builder(name, kpTypeName)
            .build()
    }
}
