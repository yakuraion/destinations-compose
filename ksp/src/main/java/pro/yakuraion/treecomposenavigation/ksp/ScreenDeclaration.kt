package pro.yakuraion.treecomposenavigation.ksp

import com.google.devtools.ksp.symbol.KSCallableReference
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter

class ScreenDeclaration(
    val function: KSFunctionDeclaration,
) {

    val packageName: String = function.packageName.asString()

    val name: String = function.simpleName.getShortName()

    val decapitalizedName: String = name.replaceFirstChar { it.lowercaseChar() }

    val lambdaParameters: List<KSValueParameter> = function.parameters
        .filter { it.type.element is KSCallableReference }
}
