package io.github.yakuraion.destinationscompose.ksp.specs

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import io.github.yakuraion.destinationscompose.ksp.screendeclaration.ScreenDeclaration

class GetStartDestinationFunCreator : FunCreator {

    override fun createKpFunSpec(screen: ScreenDeclaration): FunSpec? {
        if (!getIsAllNavArgsHaveDefaultValue(screen)) {
            return null
        }

        val funName = "get${screen.name}StartDestination"

        return FunSpec.builder(funName)
            .returns(String::class)
            .addParameter(getRouteParameterSpec(screen))
            .addFinalRouteStatement(screen)
            .addStatement("return $FINAL_ROUTE_VAL_NAME")
            .build()
    }

    private fun getIsAllNavArgsHaveDefaultValue(screen: ScreenDeclaration): Boolean {
        val isNavArgWithoutDefaultValueExists = screen.navArgParameters
            .flatMap { it.getComposableNavArgs() }
            .any { it.defaultValue == null }

        return !isNavArgWithoutDefaultValueExists
    }

    private fun getRouteParameterSpec(screen: ScreenDeclaration): ParameterSpec {
        return ParameterSpec.builder(ROUTE_PARAMETER_NAME, String::class)
            .defaultValue("%S", screen.decapitalizedName)
            .build()
    }

    private fun FunSpec.Builder.addFinalRouteStatement(screen: ScreenDeclaration): FunSpec.Builder {
        val composableNavArgs = screen.navArgParameters.flatMap { it.getComposableNavArgs() }
        return this
            .addStatement("val __routeArgumentsString = listOf<String>(")
            .run {
                composableNavArgs.fold(this) { builder, composableNavArg ->
                    builder.addStatement("    \"${composableNavArg.name}={${composableNavArg.name}}\",")
                }
            }
            .addStatement(")")
            .addStatement("val $FINAL_ROUTE_VAL_NAME = if (__routeArgumentsString.isEmpty()) $ROUTE_PARAMETER_NAME else \"\$$ROUTE_PARAMETER_NAME?\$__routeArgumentsString\"")
    }

    companion object {

        private const val ROUTE_PARAMETER_NAME = "route"
        private const val FINAL_ROUTE_VAL_NAME = "__finalRoute"
    }
}
