package pro.yakuraion.treecomposenavigation.ksp.parameters

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ParameterSpec

class InstantScreenParameter(parameter: KSValueParameter) : ScreenParameter(parameter) {

    fun getComposableParameterSpec(): ParameterSpec {
        return ParameterSpec
            .builder(name, typeName)
            .build()
    }
}
