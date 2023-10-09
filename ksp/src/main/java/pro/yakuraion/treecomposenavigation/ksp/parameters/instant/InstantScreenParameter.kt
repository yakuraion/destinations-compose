package pro.yakuraion.treecomposenavigation.ksp.parameters.instant

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ParameterSpec
import pro.yakuraion.treecomposenavigation.ksp.parameters.ScreenParameter

class InstantScreenParameter(parameter: KSValueParameter) : ScreenParameter(parameter) {

    fun getComposableParameterSpec(): ParameterSpec {
        return ParameterSpec
            .builder(name, typeName)
            .build()
    }
}
