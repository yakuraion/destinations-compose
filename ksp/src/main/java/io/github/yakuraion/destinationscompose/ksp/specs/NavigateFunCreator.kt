package io.github.yakuraion.destinationscompose.ksp.specs

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.asTypeName
import io.github.yakuraion.destinationscompose.ksp.screendeclaration.ScreenDeclaration
import io.github.yakuraion.destinationscompose.kspcore.Import

class NavigateFunCreator : FunCreator {

    override fun getImports(): List<Import> {
        return emptyList()
    }

    override fun createKpFunSpec(screen: ScreenDeclaration): FunSpec {
        val funName = "navigateTo${screen.name}"

        return FunSpec.builder(funName)
            .receiver(navControllerClass)
            .addNavigateParameters(screen)
            .addParameter(getRouteParameterSpec(screen))
            .addParameter(getNavOptionsBuilderReceiverParameterSpec())
            .addNavArgValsStatement(screen)
            .addFinalRouteStatement(screen)
            .addNavigateCallStatement()
            .build()
    }

    private fun FunSpec.Builder.addNavigateParameters(screen: ScreenDeclaration): FunSpec.Builder {
        return screen.navArgParameters.flatMap { it.getNavigateParameters() }.fold(this) { builder, param ->
            val paramSpec = ParameterSpec.builder(param.name, param.type)
                .run { param.defaultValueLiteral?.let { defaultValue(it) } ?: this }
                .build()
            builder.addParameter(paramSpec)
        }
    }

    private fun getRouteParameterSpec(screen: ScreenDeclaration): ParameterSpec {
        return ParameterSpec.builder(ROUTE_PARAMETER_NAME, String::class)
            .defaultValue("%S", screen.defaultRouteName)
            .build()
    }

    private fun getNavOptionsBuilderReceiverParameterSpec(): ParameterSpec {
        val type = LambdaTypeName.get(receiver = navOptionsBuilderClass, returnType = Unit::class.asTypeName())
        return ParameterSpec.builder("builder", type)
            .defaultValue(CodeBlock.of("{}"))
            .build()
    }

    private fun FunSpec.Builder.addNavArgValsStatement(screen: ScreenDeclaration): FunSpec.Builder {
        return screen.navArgParameters.fold(this) { builder, parameter ->
            with(parameter) {
                builder.createNavArgsValsFromNavigateParameters()
            }
        }
    }

    private fun FunSpec.Builder.addFinalRouteStatement(screen: ScreenDeclaration): FunSpec.Builder {
        val composableNavArgs = screen.navArgParameters.flatMap { it.getComposableNavArgs() }
        return this
            .addStatement("val __navArgsNamesToValues = listOf<Pair<String, String?>>(")
            .run {
                composableNavArgs.fold(this) { builder, navArg ->
                    builder.addStatement("    \"${navArg.name}\" to ${navArg.valInsideNavigateFunName},")
                }
            }
            .addStatement(")")
            .addStatement("val __notEmptyNavArgsNamesToValues = __navArgsNamesToValues.filter { it.second != null }")
            .addStatement("val __routeArgumentsString = __notEmptyNavArgsNamesToValues.joinToString(separator = \"&\") { \"\${it.first}=\${it.second}\" }")
            .addStatement("val $FINAL_ROUTE_VAL_NAME = if (__routeArgumentsString.isEmpty()) $ROUTE_PARAMETER_NAME else \"\$$ROUTE_PARAMETER_NAME?\$__routeArgumentsString\"")
    }

    private fun FunSpec.Builder.addNavigateCallStatement(): FunSpec.Builder {
        return addStatement(
            """
                navigate(
                    route = %L,
                    builder = builder,
                )
            """.trimIndent(),
            FINAL_ROUTE_VAL_NAME
        )
    }

    companion object {

        private const val ROUTE_PARAMETER_NAME = "route"
        private const val FINAL_ROUTE_VAL_NAME = "__finalRoute"

        private val navControllerClass = ClassName("androidx.navigation", "NavController")
        private val navOptionsBuilderClass = ClassName("androidx.navigation", "NavOptionsBuilder")
    }
}
