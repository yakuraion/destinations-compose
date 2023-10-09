package pro.yakuraion.treecomposenavigation.ksp.specs

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.asTypeName
import pro.yakuraion.treecomposenavigation.ksp.Import
import pro.yakuraion.treecomposenavigation.ksp.screendeclaration.ScreenDeclaration

class NavigationNavigateToFunSpecCreator : FunSpecCreator {

    override fun getImports(): List<Import> {
        return emptyList()
    }

    override fun createSpec(screen: ScreenDeclaration): FunSpec {
        val funName = "navigateTo${screen.name}"

        return FunSpec.builder(funName)
            .receiver(navControllerClass)
            .addParameters(getArgumentsParameters(screen))
            .addParameter(getRouteParameterSpec(screen))
            .addParameter(getNavOptionsBuilderReceiverParameterSpec())
            .addStatement(getInFunctionCode(screen))
            .addStatement(
                """
                val queryArgs = %L
                val routeWithQuery = if (queryArgs.isNotEmpty()) route + "?" + queryArgs else route
                navigate(
                    route = routeWithQuery,
                    builder = builder,
                )
                """.trimIndent(),
                getRouteQueryCode(screen)
            )
            .build()
    }

    private fun getArgumentsParameters(screen: ScreenDeclaration): List<ParameterSpec> {
        return screen.argumentParameters.flatMap { it.getNavigateToParametersSpecs() }
    }

    private fun getRouteParameterSpec(screen: ScreenDeclaration): ParameterSpec {
        return ParameterSpec.builder("route", String::class)
            .defaultValue("%S", screen.decapitalizedName)
            .build()
    }

    private fun getNavOptionsBuilderReceiverParameterSpec(): ParameterSpec {
        val type = LambdaTypeName.get(receiver = navOptionsBuilderClass, returnType = Unit::class.asTypeName())
        return ParameterSpec.builder("builder", type)
            .defaultValue(CodeBlock.of("{}"))
            .build()
    }

    private fun getInFunctionCode(screen: ScreenDeclaration): String {
        return screen.argumentParameters.joinToString(separator = "\n") { it.getNavigateToInFunctionCode() }
    }

    private fun getRouteQueryCode(screen: ScreenDeclaration): String {
        val code = screen.argumentParameters.joinToString(separator = "&") { it.getNavigateToQueryCode() }
        return "\"$code\".trim(\'&\')"
    }

    companion object {

        private val navControllerClass = ClassName("androidx.navigation", "NavController")
        private val navOptionsBuilderClass = ClassName("androidx.navigation", "NavOptionsBuilder")
    }
}
