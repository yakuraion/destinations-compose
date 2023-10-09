package pro.yakuraion.treecomposenavigation.ksp.parameters.instant

import com.google.devtools.ksp.symbol.KSCallableReference
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.ksp.parameters.ScreenParametersExtractor

class LambdaParametersExtractor : ScreenParametersExtractor<InstantScreenParameter> {

    override fun extract(screenFun: KSFunctionDeclaration): List<InstantScreenParameter> {
        return screenFun.parameters
            .filter { it.type.element is KSCallableReference }
            .map { InstantScreenParameter(it) }
    }
}
