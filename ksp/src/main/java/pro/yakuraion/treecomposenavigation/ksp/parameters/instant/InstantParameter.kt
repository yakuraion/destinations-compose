package pro.yakuraion.treecomposenavigation.ksp.parameters.instant

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ParameterSpec
import pro.yakuraion.treecomposenavigation.ksp.parameters.Parameter

class InstantParameter(parameter: KSValueParameter) : Parameter(parameter) {

    fun getParameterSpec(): ParameterSpec {
        return ParameterSpec
            .builder(name, typeName)
            .build()
    }
}
