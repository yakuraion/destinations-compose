package pro.yakuraion.treecomposenavigation.ksp.specs

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.asTypeName
import pro.yakuraion.treecomposenavigation.ksp.Import
import pro.yakuraion.treecomposenavigation.ksp.screendeclaration.ScreenDeclaration

class NavigateToFunCreator : FunCreator {

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
            .addPropertiesStatement(screen)
            .addNavigateStatement(screen)
            .build()
    }

    private fun getArgumentsParameters(screen: ScreenDeclaration): List<ParameterSpec> {
        return screen.argumentParameters.flatMap { it.getArgumentsAsParametersSpecs() }
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

    private fun FunSpec.Builder.addPropertiesStatement(screen: ScreenDeclaration): FunSpec.Builder {
        return screen.argumentParameters.fold(this) { builder, parameter ->
            builder.addStatement(parameter.getPropertiesForValueQueryArgumentsCode())
        }
    }

    private fun FunSpec.Builder.addNavigateStatement(screen: ScreenDeclaration): FunSpec.Builder {
        val queryArguments = screen.argumentParameters.flatMap { it.getValueQueryArguments() }
        return addStatement(
            """
                val queryArgs = listOf(%L).filter { it.second != null }.joinToString(separator = "&") { it.first + "=" + it.second } 
                val routeWithQuery = if (queryArgs.isEmpty()) route else route + "?" + queryArgs
                navigate(
                    route = routeWithQuery,
                    builder = builder,
                )
            """.trimIndent(),
            queryArguments.joinToString(separator = ", ") { "\"${it.name}\" to ${it.valuePropertyName}" },
        )
    }

    companion object {

        private val navControllerClass = ClassName("androidx.navigation", "NavController")
        private val navOptionsBuilderClass = ClassName("androidx.navigation", "NavOptionsBuilder")
    }
}
