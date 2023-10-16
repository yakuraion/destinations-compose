package pro.yakuraion.treecomposenavigation.kspcore.parameters.lambda

import com.google.devtools.ksp.symbol.KSCallableReference
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.kspcore.parameters.InstantScreenParameter
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ScreenParametersExtractor

class LambdaScreenParametersExtractor : ScreenParametersExtractor<InstantScreenParameter> {

    override fun extract(ksScreenFun: KSFunctionDeclaration): List<InstantScreenParameter> {
        return ksScreenFun.parameters
            .filter { it.type.element is KSCallableReference }
            .map { InstantScreenParameter(it) }
    }
}
