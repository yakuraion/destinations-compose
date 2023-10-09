package pro.yakuraion.treecomposenavigation.ksp.specs

import com.squareup.kotlinpoet.FunSpec
import pro.yakuraion.treecomposenavigation.ksp.Import
import pro.yakuraion.treecomposenavigation.ksp.screendeclaration.ScreenDeclaration

interface FunSpecCreator {

    fun getImports(): List<Import> = emptyList()

    fun createSpec(screen: ScreenDeclaration): FunSpec
}
