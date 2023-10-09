package pro.yakuraion.treecomposenavigation.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import pro.yakuraion.treecomposenavigation.ksp.specs.FunSpecCreator

class NavigationCreator(
    private val codeGenerator: CodeGenerator,
    private val funSpecCreators: List<FunSpecCreator>,
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
                funSpecCreators.fold(this) { builder, funSpecCreator ->
                    builder
                        .addImports(funSpecCreator.getImports())
                        .addFunction(funSpecCreator.createSpec(screen))
                }
            }
            .build()
    }
}
