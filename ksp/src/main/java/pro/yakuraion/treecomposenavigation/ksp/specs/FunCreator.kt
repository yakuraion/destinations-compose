package pro.yakuraion.treecomposenavigation.ksp.specs

import com.squareup.kotlinpoet.FunSpec
import pro.yakuraion.treecomposenavigation.kspcore.Import
import pro.yakuraion.treecomposenavigation.ksp.screendeclaration.ScreenDeclaration

interface FunCreator {

    fun getImports(): List<Import> = emptyList()

    fun createKpFunSpec(screen: ScreenDeclaration): FunSpec
}
