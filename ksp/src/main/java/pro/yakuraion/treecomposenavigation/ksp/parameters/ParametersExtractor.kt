package pro.yakuraion.treecomposenavigation.ksp.parameters

import com.google.devtools.ksp.symbol.KSFunctionDeclaration

interface ParametersExtractor<T : Parameter> {

    fun extract(screenFun: KSFunctionDeclaration): List<T>
}
