package io.github.yakuraion.destinationscompose.ksp.specs

import com.squareup.kotlinpoet.FunSpec
import io.github.yakuraion.destinationscompose.ksp.screendeclaration.ScreenDeclaration
import io.github.yakuraion.destinationscompose.kspcore.Import

interface FunCreator {

    fun getImports(): List<Import> = emptyList()

    fun createKpFunSpec(screen: ScreenDeclaration): FunSpec?
}
