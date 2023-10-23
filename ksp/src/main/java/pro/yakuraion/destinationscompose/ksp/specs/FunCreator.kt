package pro.yakuraion.destinationscompose.ksp.specs

import com.squareup.kotlinpoet.FunSpec
import pro.yakuraion.destinationscompose.kspcore.Import
import pro.yakuraion.destinationscompose.ksp.screendeclaration.ScreenDeclaration

interface FunCreator {

    fun getImports(): List<Import> = emptyList()

    fun createKpFunSpec(screen: ScreenDeclaration): FunSpec?
}
