package io.github.yakuraion.destinationscompose.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import io.github.yakuraion.destinationscompose.ksp.screendeclaration.ScreenDeclaration
import io.github.yakuraion.destinationscompose.ksp.specs.FunCreator
import io.github.yakuraion.destinationscompose.kspcore.addImports

class NavigationCreator(
    private val codeGenerator: CodeGenerator,
    private val funCreators: List<FunCreator>,
) {

    fun create(screen: ScreenDeclaration) {
        val fileSpec = getFileSpec(screen)
        val sourceFiles = listOf(screen.ksContainingFile)
        fileSpec.writeTo(codeGenerator, false, sourceFiles)
    }

    private fun getFileSpec(
        screen: ScreenDeclaration,
    ): FileSpec {
        val fileName = "${screen.name}Navigation"

        val funCreatorImports = funCreators.flatMap { it.getImports() }
        val parametersImports = screen.parameters.flatMap { it.getImports() }
        val imports = (funCreatorImports + parametersImports).distinct()

        return FileSpec.builder(screen.packageName, fileName)
            .indent("    ")
            .addImports(imports)
            .run {
                funCreators.fold(this) { builder, funSpecCreator ->
                    funSpecCreator.createKpFunSpec(screen)?.let { builder.addFunction(it) } ?: builder
                }
            }
            .build()
    }
}
