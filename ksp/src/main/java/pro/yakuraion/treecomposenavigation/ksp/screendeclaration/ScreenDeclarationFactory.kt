package pro.yakuraion.treecomposenavigation.ksp.screendeclaration

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.ksp.parameters.ScreenParametersExtractor

class ScreenDeclarationFactory(
    private val screenParametersExtractors: List<ScreenParametersExtractor<*>>,
) {

    fun create(screenFunction: KSFunctionDeclaration): ScreenDeclaration {
        return ScreenDeclaration(
            packageName = screenFunction.packageName.asString(),
            name = screenFunction.simpleName.getShortName(),
            containingFile = screenFunction.containingFile!!,
            parameters = screenParametersExtractors.map { it.extract(screenFunction) }.flatten()
        )
    }
}
