package pro.yakuraion.treecomposenavigation.ksp.specs

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import pro.yakuraion.treecomposenavigation.ksp.screendeclaration.ScreenDeclaration

class GetStartDestinationFunSpecCreator : FunSpecCreator {

    override fun createSpec(screen: ScreenDeclaration): FunSpec {
        val funName = "get${screen.name}StartDestination"

        return FunSpec.builder(funName)
            .returns(String::class)
            .addParameters(getArgumentsParameters(screen))
            .addParameter(getRouteParameterSpec(screen))
            .addStatement(getInFunctionCode(screen))
            .addStatement(
                """
                val queryArgs = %L
                return if (queryArgs.isNotEmpty()) route + "?" + queryArgs else route
                """.trimIndent(),
                getRouteQueryCode(screen)
            )
            .build()
    }

    private fun getRouteParameterSpec(screen: ScreenDeclaration): ParameterSpec {
        return ParameterSpec.builder("route", String::class)
            .defaultValue("%S", screen.decapitalizedName)
            .build()
    }

    private fun getArgumentsParameters(screen: ScreenDeclaration): List<ParameterSpec> {
        return screen.argumentParameters.flatMap { it.getNavigateToParametersSpecs() }
    }

    private fun getInFunctionCode(screen: ScreenDeclaration): String {
        return screen.argumentParameters.joinToString(separator = "\n") { it.getNavigateToInFunctionCode() }
    }

    private fun getRouteQueryCode(screen: ScreenDeclaration): String {
        val code = screen.argumentParameters.joinToString(separator = "&") { it.getNavigateToQueryCode() }
        return "\"$code\".trim(\'&\')"
    }
}
