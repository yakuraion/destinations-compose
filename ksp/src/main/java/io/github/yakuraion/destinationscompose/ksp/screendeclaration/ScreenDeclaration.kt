package io.github.yakuraion.destinationscompose.ksp.screendeclaration

import com.google.devtools.ksp.symbol.KSFile
import io.github.yakuraion.destinationscompose.kspcore.parameters.DirectParameter
import io.github.yakuraion.destinationscompose.kspcore.parameters.NavArgParameter
import io.github.yakuraion.destinationscompose.kspcore.parameters.Parameter

class ScreenDeclaration(
    val packageName: String,
    val name: String,
    val ksContainingFile: KSFile,
    val parameters: List<Parameter>,
) {

    val decapitalizedName: String = name.replaceFirstChar { it.lowercaseChar() }

    val defaultRouteName = decapitalizedName

    val directParameters: List<DirectParameter> = parameters.filterIsInstance(DirectParameter::class.java)

    val navArgParameters: List<NavArgParameter> = parameters.filterIsInstance(NavArgParameter::class.java)
}
