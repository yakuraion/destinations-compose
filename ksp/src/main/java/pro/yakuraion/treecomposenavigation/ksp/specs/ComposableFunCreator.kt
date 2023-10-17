package pro.yakuraion.treecomposenavigation.ksp.specs

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import pro.yakuraion.treecomposenavigation.ksp.screendeclaration.ScreenDeclaration
import pro.yakuraion.treecomposenavigation.kspcore.BACK_STACK_ENTRY_NAME
import pro.yakuraion.treecomposenavigation.kspcore.Import

class ComposableFunCreator : FunCreator {

    override fun getImports(): List<Import> {
        return listOf(
            Import("androidx.navigation", "navArgument"),
            Import("androidx.navigation", "NavType"),
        )
    }

    override fun createKpFunSpec(screen: ScreenDeclaration): FunSpec {
        val funName = "${screen.decapitalizedName}Composable"

        return FunSpec.builder(funName)
            .receiver(navGraphBuilderClass)
            .addComposableParameters(screen)
            .addParameter(getRouteParameterSpec(screen))
            .addParameter(getTransitionParameterSpec("enterTransition", enterTransitionClass, "null"))
            .addParameter(getTransitionParameterSpec("exitTransition", exitTransitionClass, "null"))
            .addParameter(getTransitionParameterSpec("popEnterTransition", enterTransitionClass, "enterTransition"))
            .addParameter(getTransitionParameterSpec("popExitTransition", exitTransitionClass, "exitTransition"))
            .addComposableCallStatement(screen) {
                addScreenCallStatement(screen)
            }
            .build()
    }

    private fun FunSpec.Builder.addComposableParameters(screen: ScreenDeclaration): FunSpec.Builder {
        return screen.parameters.fold(this) { builder, parameter ->
            with(parameter) {
                builder.addComposableParameters()
            }
        }
    }

    private fun getRouteParameterSpec(screen: ScreenDeclaration): ParameterSpec {
        return ParameterSpec.builder("route", String::class)
            .defaultValue("%S", screen.decapitalizedName)
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

    private fun FunSpec.Builder.addComposableCallStatement(
        screen: ScreenDeclaration,
        insideCode: FunSpec.Builder.() -> FunSpec.Builder,
    ): FunSpec.Builder {
        return this
            .addStatement(
                """
                %M(
                    route = route + %S,
                    arguments = listOf(
                        %L
                    ),
                    enterTransition = enterTransition,
                    exitTransition = exitTransition,
                    popEnterTransition = popEnterTransition,
                    popExitTransition = popExitTransition,
                ) { $BACK_STACK_ENTRY_NAME ->
            """.trimIndent(),
                composableMember,
                getQueryArgumentsString(screen),
                getNavigationArgumentsString(screen),
            )
            .insideCode()
            .addStatement("}")
    }

    /**
     * Example:
     * "?arg1={arg1}&arg2={arg2}
     */
    private fun getQueryArgumentsString(screen: ScreenDeclaration): String {
        var result = ""
        val routeQueryArgs = screen.parameters.map { it.getComposableRouteArguments() }.flatten()
        if (routeQueryArgs.isNotEmpty()) {
            result = "?" + routeQueryArgs.joinToString(separator = "&") { "${it.name}={${it.name}}" }
        }
        return result
    }

    /**
     * Example:
     * navArgument("arg1"} { type = NavType.String; nullable = true },
     * navArgument("arg2"} { type = NavType.String; nullable = true },
     */
    private fun getNavigationArgumentsString(screen: ScreenDeclaration): String {
        return screen.parameters.flatMap { it.getComposableRouteArguments() }
            .joinToString(separator = ",\n") { routeArgument ->
                "navArgument(\"${routeArgument.name}\") { type = NavType.StringType; nullable = true }"
            }
    }

    private fun FunSpec.Builder.addScreenCallStatement(screen: ScreenDeclaration): FunSpec.Builder {
        val builder = screen.parameters.fold(this) { builder, parameter ->
            builder.addStatement(parameter.getComposableCreateScreenParameterPropertiesCode())
        }

        val screenMember = MemberName(screen.packageName, screen.name)

        val argsParametersString = screen.parameters
            .joinToString(separator = ",\n") { "${it.name} = ${it.name}" }

        return builder.addStatement(
            """
            %M(
            %L
            )
        """.trimIndent(),
            screenMember,
            argsParametersString
        )
    }

    companion object {

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
