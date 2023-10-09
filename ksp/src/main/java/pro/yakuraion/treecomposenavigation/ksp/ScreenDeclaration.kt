package pro.yakuraion.treecomposenavigation.ksp

import com.google.devtools.ksp.symbol.KSFile
import pro.yakuraion.treecomposenavigation.ksp.parameters.ArgumentScreenParameter
import pro.yakuraion.treecomposenavigation.ksp.parameters.InstantScreenParameter
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
