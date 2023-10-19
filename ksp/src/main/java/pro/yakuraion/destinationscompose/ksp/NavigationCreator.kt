package pro.yakuraion.destinationscompose.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import pro.yakuraion.destinationscompose.ksp.screendeclaration.ScreenDeclaration
import pro.yakuraion.destinationscompose.ksp.specs.FunCreator
import pro.yakuraion.destinationscompose.kspcore.addImports

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
        val imports = funCreators.flatMap { it.getImports() } + screen.parameters.flatMap { it.getImports() }
        return FileSpec.builder(screen.packageName, fileName)
            .indent("    ")
            .run {
                funCreators.fold(this) { builder, funSpecCreator ->
                    builder
                        .addImports(imports.distinct())
                        .addFunction(funSpecCreator.createKpFunSpec(screen))
                }
            }
            .build()
    }
}
