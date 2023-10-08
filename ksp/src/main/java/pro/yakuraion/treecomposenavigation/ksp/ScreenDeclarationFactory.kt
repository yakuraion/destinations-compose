package pro.yakuraion.treecomposenavigation.ksp

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.ksp.args.ArgumentsFilter

class ScreenDeclarationFactory(
    private val argumentsFilters: List<ArgumentsFilter<*>>,
) {

    fun create(screenFunction: KSFunctionDeclaration): ScreenDeclaration {
        return ScreenDeclaration(
            packageName = screenFunction.packageName.asString(),
            name = screenFunction.simpleName.getShortName(),
            containingFile = screenFunction.containingFile!!,
            arguments = argumentsFilters.map { it.filter(screenFunction.parameters) }.flatten()
        )
    }
}
