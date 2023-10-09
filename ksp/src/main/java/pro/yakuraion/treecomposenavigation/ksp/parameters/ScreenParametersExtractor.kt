package pro.yakuraion.treecomposenavigation.ksp.parameters

import com.google.devtools.ksp.symbol.KSFunctionDeclaration

interface ScreenParametersExtractor<T : ScreenParameter> {

    fun extract(screenFun: KSFunctionDeclaration): List<T>
}
