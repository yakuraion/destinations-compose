package pro.yakuraion.treecomposenavigation.ksp

import com.google.devtools.ksp.symbol.KSFile
import pro.yakuraion.treecomposenavigation.ksp.args.Argument

class ScreenDeclaration(
    val packageName: String,
    val name: String,
    val containingFile: KSFile,
    val arguments: List<Argument>,
) {

    val decapitalizedName: String = name.replaceFirstChar { it.lowercaseChar() }
}
