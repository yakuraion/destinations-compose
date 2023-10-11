package pro.yakuraion.treecomposenavigation.ksp.screendeclaration

import com.google.devtools.ksp.symbol.KSFile
import pro.yakuraion.treecomposenavigation.ksp.parameters.argument.ArgumentParameter
import pro.yakuraion.treecomposenavigation.ksp.parameters.instant.InstantParameter
import pro.yakuraion.treecomposenavigation.ksp.parameters.Parameter

class ScreenDeclaration(
    val packageName: String,
    val name: String,
    val containingFile: KSFile,
    val parameters: List<Parameter>,
) {

    val decapitalizedName: String = name.replaceFirstChar { it.lowercaseChar() }

    val instantParameters = parameters.filterIsInstance(InstantParameter::class.java)

    val argumentParameters = parameters.filterIsInstance(ArgumentParameter::class.java)
}
