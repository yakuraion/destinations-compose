package pro.yakuraion.treecomposenavigation.ksp.specs

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import pro.yakuraion.treecomposenavigation.ksp.screendeclaration.ScreenDeclaration

class GetStartDestinationFunCreator : FunCreator {

    override fun createSpec(screen: ScreenDeclaration): FunSpec {
        val funName = "get${screen.name}StartDestination"

        return FunSpec.builder(funName)
            .returns(String::class)
            .addParameters(getArgumentsParameters(screen))
            .addParameter(getRouteParameterSpec(screen))
            .addPropertiesStatement(screen)
            .addReturnRouteStatement(screen)
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

    private fun FunSpec.Builder.addPropertiesStatement(screen: ScreenDeclaration): FunSpec.Builder {
        return screen.argumentParameters.fold(this) { builder, parameter ->
            builder.addStatement(parameter.getPropertiesForValueQueryArgumentsCode())
        }
    }

    private fun FunSpec.Builder.addReturnRouteStatement(screen: ScreenDeclaration): FunSpec.Builder {
        val queryArguments = screen.argumentParameters.flatMap { it.getValueQueryArguments() }
        return addStatement(
            """
                val queryArgs = listOf(%L).filter { it.second != null }.joinToString(separator = "&") { it.first + "=" + it.second } 
                return if (queryArgs.isEmpty()) route else route + "?" + queryArgs
            """.trimIndent(),
            queryArguments.joinToString(separator = ", ") { "\"${it.name}\" to ${it.valuePropertyName}" },
        )
    }
}
