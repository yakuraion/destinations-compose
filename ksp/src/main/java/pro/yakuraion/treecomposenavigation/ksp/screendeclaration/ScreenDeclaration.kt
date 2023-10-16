package pro.yakuraion.treecomposenavigation.ksp.screendeclaration

import com.google.devtools.ksp.symbol.KSFile
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ScreenParameter
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ArgumentsScreenParameter
import pro.yakuraion.treecomposenavigation.kspcore.parameters.InstantScreenParameter

class ScreenDeclaration(
    val packageName: String,
    val name: String,
    val ksContainingFile: KSFile,
    val parameters: List<ScreenParameter>,
) {

    val decapitalizedName: String = name.replaceFirstChar { it.lowercaseChar() }

    val instantParameters = parameters.filterIsInstance(InstantScreenParameter::class.java)

    val argumentParameters = parameters.filterIsInstance(ArgumentsScreenParameter::class.java)
}
