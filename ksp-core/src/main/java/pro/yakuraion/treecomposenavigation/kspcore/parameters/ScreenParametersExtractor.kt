package pro.yakuraion.treecomposenavigation.kspcore.parameters

import com.google.devtools.ksp.symbol.KSFunctionDeclaration

interface ScreenParametersExtractor<T : ScreenParameter> {

    fun extract(ksScreenFun: KSFunctionDeclaration): List<T>
}
