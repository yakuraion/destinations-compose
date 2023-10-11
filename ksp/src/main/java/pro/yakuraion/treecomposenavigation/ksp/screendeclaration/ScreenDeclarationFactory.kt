package pro.yakuraion.treecomposenavigation.ksp.screendeclaration

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.ksp.parameters.ParametersExtractor

class ScreenDeclarationFactory(
    private val parametersExtractors: List<ParametersExtractor<*>>,
) {

    fun create(screenFunction: KSFunctionDeclaration): ScreenDeclaration {
        return ScreenDeclaration(
            packageName = screenFunction.packageName.asString(),
            name = screenFunction.simpleName.getShortName(),
            containingFile = screenFunction.containingFile!!,
            parameters = parametersExtractors.map { it.extract(screenFunction) }.flatten()
        )
    }
}
