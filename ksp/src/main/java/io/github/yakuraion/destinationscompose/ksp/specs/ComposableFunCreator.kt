package io.github.yakuraion.destinationscompose.ksp.specs

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.github.yakuraion.destinationscompose.ksp.screendeclaration.ScreenDeclaration
import io.github.yakuraion.destinationscompose.kspcore.Import

class ComposableFunCreator : FunCreator {

    override fun getImports(): List<Import> {
        return listOf(
            Import("androidx.navigation", "NamedNavArgument"),
            Import("androidx.navigation", "NavType"),
            Import("androidx.navigation", "navArgument"),
        )
    }

    override fun createKpFunSpec(screen: ScreenDeclaration): FunSpec {
        val funName = "${screen.decapitalizedName}Composable"

        return FunSpec.builder(funName)
            .receiver(navGraphBuilderClass)
            .addDirectParameters(screen)
            .addParameter(getRouteParameterSpec(screen))
            .addParameter(getTransitionParameterSpec("enterTransition", enterTransitionClass, "null"))
            .addParameter(getTransitionParameterSpec("exitTransition", exitTransitionClass, "null"))
            .addParameter(getTransitionParameterSpec("popEnterTransition", enterTransitionClass, "enterTransition"))
            .addParameter(getTransitionParameterSpec("popExitTransition", exitTransitionClass, "exitTransition"))
            .addFinalRouteStatement(screen)
            .addStatement("")
            .addComposableArgumentsStatement(screen)
            .addStatement("")
            .addComposableCallStatement {
                addScreenCallStatement(screen)
            }
            .build()
    }

    private fun FunSpec.Builder.addDirectParameters(screen: ScreenDeclaration): FunSpec.Builder {
        return screen.directParameters.fold(this) { builder, parameter ->
            builder.addParameter(parameter.name, parameter.kpTypeName)
        }
    }

    private fun getRouteParameterSpec(screen: ScreenDeclaration): ParameterSpec {
        return ParameterSpec.builder(ROUTE_PARAMETER_NAME, String::class)
            .defaultValue("%S", screen.defaultRouteName)
            .build()
    }

    private fun getTransitionParameterSpec(
        funName: String,
        transitionClass: ClassName,
        defaultValue: String,
    ): ParameterSpec {
        val parameterType = function1Class.parameterizedBy(
            animatedContentTransitionScopeClass.parameterizedBy(
                navBackStackEntryClass
            ),
            transitionClass.copy(nullable = true),
        ).copy(nullable = true)

        return ParameterSpec.builder(funName, parameterType)
            .defaultValue(defaultValue)
            .build()
    }

    private fun FunSpec.Builder.addFinalRouteStatement(screen: ScreenDeclaration): FunSpec.Builder {
        return this
            .addStatement("val·$FINAL_ROUTE_VAL_NAME·= get${screen.name}RouteScheme(route·= $ROUTE_PARAMETER_NAME)")
    }

    private fun FunSpec.Builder.addComposableArgumentsStatement(screen: ScreenDeclaration): FunSpec.Builder {
        val navArguments = screen.navArgParameters.map { it.getComposableNavArgs() }.flatten()
        return this
            .addStatement("val·$COMPOSABLE_ARGUMENTS_VAL_NAME·= listOf<NamedNavArgument>(")
            .run {
                navArguments.fold(this) { builder, navArgument ->
                    val defaultValueEncoded = navArgument.defaultValue?.value
                        ?.let {"Base64.encodeToString(\"$it\".toByteArray(), Base64.NO_WRAP)"
                        } ?: "null"
                    builder
                        .addStatement("    navArgument(\"${navArgument.name}\")·{")
                        .addStatement("        type·= NavType.StringType;")
                        .addStatement("        nullable·= true;")
                        .addStatement("        defaultValue·= $defaultValueEncoded")
                        .addStatement("    },")
                }
            }
            .addStatement(")")
    }

    private fun FunSpec.Builder.addComposableCallStatement(
        insideCode: FunSpec.Builder.() -> FunSpec.Builder,
    ): FunSpec.Builder {
        return this
            .addStatement("%M(", composableMember)
            .addStatement("    route·= $FINAL_ROUTE_VAL_NAME,")
            .addStatement("    arguments·= $COMPOSABLE_ARGUMENTS_VAL_NAME,")
            .addStatement("    enterTransition·= enterTransition,")
            .addStatement("    exitTransition·= exitTransition,")
            .addStatement("    popEnterTransition·= popEnterTransition,")
            .addStatement("    popExitTransition·= popExitTransition,")
            .beginControlFlow(")")
            .addStatement("$BACK_STACK_NAME ->")
            .insideCode()
            .endControlFlow()
    }

    private fun FunSpec.Builder.addScreenCallStatement(screen: ScreenDeclaration): FunSpec.Builder {
        val builder = screen.navArgParameters.fold(this) { builder, parameter ->
            with(parameter) {
                builder.createParameterValFromBackStack(BACK_STACK_NAME)
            }
        }

        val directScreenParamsToVals = screen.directParameters.map { it.name to it.name }
        val navArgScreenParamsToVals = screen.navArgParameters.map { it.name to it.parameterValFromBackStackName }
        val paramsAssignments = (directScreenParamsToVals + navArgScreenParamsToVals)

        return builder
            .addStatement("")
            .addStatement("%M(", MemberName(screen.packageName, screen.name))
            .run {
                paramsAssignments.fold(this) { builder, paramsAssignment ->
                    builder.addStatement("    ${paramsAssignment.first} = ${paramsAssignment.second},")
                }
            }
            .addStatement(")")
    }

    companion object {

        private const val ROUTE_PARAMETER_NAME = "route"
        private const val FINAL_ROUTE_VAL_NAME = "__finalRoute"
        private const val COMPOSABLE_ARGUMENTS_VAL_NAME = "__composableArguments"
        private const val BACK_STACK_NAME = "backStackEntry"

        private val function1Class = ClassName("kotlin", "Function1")

        private val navGraphBuilderClass = ClassName("androidx.navigation", "NavGraphBuilder")
        private val navBackStackEntryClass = ClassName("androidx.navigation", "NavBackStackEntry")
        private val composableMember = MemberName("androidx.navigation.compose", "composable")

        private val animatedContentTransitionScopeClass =
            ClassName("androidx.compose.animation", "AnimatedContentTransitionScope")
        private val enterTransitionClass = ClassName("androidx.compose.animation", "EnterTransition")
        private val exitTransitionClass = ClassName("androidx.compose.animation", "ExitTransition")
    }
}
