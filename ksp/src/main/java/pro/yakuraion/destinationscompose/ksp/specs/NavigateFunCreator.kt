package pro.yakuraion.destinationscompose.ksp.specs

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.asTypeName
import pro.yakuraion.destinationscompose.ksp.screendeclaration.ScreenDeclaration
import pro.yakuraion.destinationscompose.kspcore.Import

class NavigateFunCreator(private val type: Type) : FunCreator {

    override fun getImports(): List<Import> {
        return emptyList()
    }

    override fun createKpFunSpec(screen: ScreenDeclaration): FunSpec {
        return when (type) {
            Type.NAVIGATE_TO -> createNavigateToFunSpec(screen)
            Type.GET_START_DESTINATION -> createGetStartDestinationFunSpec(screen)
        }
    }

    private fun createNavigateToFunSpec(screen: ScreenDeclaration): FunSpec {
        val funName = "navigateTo${screen.name}"

        return FunSpec.builder(funName)
            .receiver(navControllerClass)
            .addNavigateParameters(screen)
            .addParameter(getRouteParameterSpec(screen))
            .addParameter(getNavOptionsBuilderReceiverParameterSpec())
            .addPropertiesStatement(screen)
            .addRouteWithQueryStatement(screen)
            .addStatement("""
                navigate(
                    route = routeWithQuery,
                    builder = builder,
                )
            """.trimIndent())
            .build()
    }

    private fun createGetStartDestinationFunSpec(screen: ScreenDeclaration): FunSpec {
        val funName = "get${screen.name}StartDestination"

        return FunSpec.builder(funName)
            .returns(String::class)
            .addNavigateParameters(screen)
            .addParameter(getRouteParameterSpec(screen))
            .addPropertiesStatement(screen)
            .addRouteWithQueryStatement(screen)
            .addStatement("""
                return routeWithQuery
            """.trimIndent())
            .build()
    }

    private fun FunSpec.Builder.addNavigateParameters(screen: ScreenDeclaration): FunSpec.Builder {
        return screen.parameters.fold(this) { builder, parameter ->
            with(parameter) {
                builder.addNavigateParameters()
            }
        }
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
        return screen.parameters.fold(this) { builder, parameter ->
            builder.addStatement(parameter.getNavigateCreateRouteArgumentPropertiesCode())
        }
    }

    private fun FunSpec.Builder.addRouteWithQueryStatement(screen: ScreenDeclaration): FunSpec.Builder {
        val routeArguments = screen.parameters.flatMap { it.getNavigateRouteArguments() }
        return addStatement(
            """
                val queryArgs = listOf<Pair<String, String?>>(%L).filter { it.second != null }.joinToString(separator = "&") { it.first + "=" + it.second } 
                val routeWithQuery = if (queryArgs.isEmpty()) route else route + "?" + queryArgs
            """.trimIndent(),
            routeArguments.joinToString(separator = ", ") { "\"${it.name}\" to ${it.propertyName}" },
        )
    }

    enum class Type { NAVIGATE_TO, GET_START_DESTINATION }

    companion object {

        private val navControllerClass = ClassName("androidx.navigation", "NavController")
        private val navOptionsBuilderClass = ClassName("androidx.navigation", "NavOptionsBuilder")
    }
}
