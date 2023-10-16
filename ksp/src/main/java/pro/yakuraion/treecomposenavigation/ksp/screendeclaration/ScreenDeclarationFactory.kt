package pro.yakuraion.treecomposenavigation.ksp.screendeclaration

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ScreenParametersExtractor

class ScreenDeclarationFactory(
    private val parametersExtractors: List<ScreenParametersExtractor<*>>,
) {

    fun create(ksScreenFunction: KSFunctionDeclaration): ScreenDeclaration {
        return ScreenDeclaration(
            packageName = ksScreenFunction.packageName.asString(),
            name = ksScreenFunction.simpleName.getShortName(),
            ksContainingFile = ksScreenFunction.containingFile!!,
            parameters = parametersExtractors.map { it.extract(ksScreenFunction) }.flatten()
        )
    }
}
