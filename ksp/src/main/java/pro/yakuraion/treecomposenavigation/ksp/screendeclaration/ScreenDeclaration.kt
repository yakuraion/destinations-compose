package pro.yakuraion.treecomposenavigation.ksp.screendeclaration

import com.google.devtools.ksp.symbol.KSFile
import pro.yakuraion.treecomposenavigation.kspcore.parameters.Parameter

class ScreenDeclaration(
    val packageName: String,
    val name: String,
    val ksContainingFile: KSFile,
    val parameters: List<Parameter>,
) {

    val decapitalizedName: String = name.replaceFirstChar { it.lowercaseChar() }
}
