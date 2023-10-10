package pro.yakuraion.treecomposenavigation.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import pro.yakuraion.treecomposenavigation.ksp.screendeclaration.ScreenDeclaration
import pro.yakuraion.treecomposenavigation.ksp.specs.FunCreator

class NavigationCreator(
    private val codeGenerator: CodeGenerator,
    private val funCreators: List<FunCreator>,
) {

    fun create(screen: ScreenDeclaration) {
        val fileSpec = getFileSpec(screen)
        val sourceFiles = listOf(screen.containingFile)
        fileSpec.writeTo(codeGenerator, false, sourceFiles)
    }

    private fun getFileSpec(
        screen: ScreenDeclaration,
    ): FileSpec {
        val fileName = "${screen.name}Navigation"
        return FileSpec.builder(screen.packageName, fileName)
            .indent("    ")
            .run {
                funCreators.fold(this) { builder, funSpecCreator ->
                    builder
                        .addImports(funSpecCreator.getImports())
                        .addFunction(funSpecCreator.createSpec(screen))
                }
            }
            .build()
    }
}
