package pro.yakuraion.treecomposenavigation.ksp.parameters.instant

import com.google.devtools.ksp.symbol.KSCallableReference
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.ksp.parameters.ParametersExtractor

class LambdaParametersExtractor : ParametersExtractor<InstantParameter> {

    override fun extract(screenFun: KSFunctionDeclaration): List<InstantParameter> {
        return screenFun.parameters
            .filter { it.type.element is KSCallableReference }
            .map { InstantParameter(it) }
    }
}
