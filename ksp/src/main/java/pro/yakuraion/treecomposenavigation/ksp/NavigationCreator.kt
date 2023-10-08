package pro.yakuraion.treecomposenavigation.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import pro.yakuraion.treecomposenavigation.ksp.specs.createNavigationComposableFunSpec
import pro.yakuraion.treecomposenavigation.ksp.specs.createNavigationNavigateToFunSpec

class NavigationCreator {

    fun create(
        codeGenerator: CodeGenerator,
        screen: ScreenDeclaration,
    ) {
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
            .addFunction(createNavigationComposableFunSpec(screen))
            .addFunction(createNavigationNavigateToFunSpec(screen))
            .build()
    }
}
