package pro.yakuraion.treecomposenavigation.ksp.screendeclaration

import com.google.devtools.ksp.symbol.KSFile
import pro.yakuraion.treecomposenavigation.ksp.parameters.argument.ArgumentScreenParameter
import pro.yakuraion.treecomposenavigation.ksp.parameters.instant.InstantScreenParameter
import pro.yakuraion.treecomposenavigation.ksp.parameters.ScreenParameter

class ScreenDeclaration(
    val packageName: String,
    val name: String,
    val containingFile: KSFile,
    val parameters: List<ScreenParameter>,
) {

    val decapitalizedName: String = name.replaceFirstChar { it.lowercaseChar() }

    val instantParameters = parameters.filterIsInstance(InstantScreenParameter::class.java)

    val argumentParameters = parameters.filterIsInstance(ArgumentScreenParameter::class.java)
}
