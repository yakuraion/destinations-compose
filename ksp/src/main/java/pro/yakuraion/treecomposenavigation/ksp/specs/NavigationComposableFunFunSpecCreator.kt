package pro.yakuraion.treecomposenavigation.ksp.specs

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import pro.yakuraion.treecomposenavigation.ksp.Import
import pro.yakuraion.treecomposenavigation.ksp.ScreenDeclaration

class NavigationComposableFunFunSpecCreator : FunSpecCreator {

    override fun getImports(): List<Import> {
        return listOf(
            Import("androidx.navigation", "navArgument"),
            Import("androidx.navigation", "NavType"),
        )
    }

    override fun createSpec(screen: ScreenDeclaration): FunSpec {
        val funName = "${screen.decapitalizedName}Composable"

        return FunSpec.builder(funName)
            .receiver(navGraphBuilderClass)
            .addParameters(screen.instantParameters.map { it.getComposableParameterSpec() })
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
                        %L,
                    ),
                    enterTransition = enterTransition,
                    exitTransition = exitTransition,
                    popEnterTransition = popEnterTransition,
                    popExitTransition = popExitTransition,
                ) { backStackEntry ->
            """.trimIndent(),
                composableMember,
                getRouteQueryArgsString(screen),
                getNavigationArgumentsString(screen),
            )
            .insideCode()
            .addStatement("}")
    }

    /**
     * Example:
     * "?arg1={arg1}&arg2={arg2}
     */
    private fun getRouteQueryArgsString(screen: ScreenDeclaration): String {
        var result = ""
        val routeQueryArgs = screen.argumentParameters.map { it.getComposableQueryArguments() }.flatten()
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
        return screen.argumentParameters.joinToString(separator = ",\n") {
            "navArgument(\"${it.name}\") { type = NavType.StringType; nullable = true }"
        }
    }

    private fun FunSpec.Builder.addScreenCallStatement(screen: ScreenDeclaration): FunSpec.Builder {
        val createValsCode = screen.argumentParameters.joinToString(separator = "\n") { parameter ->
            parameter.getComposableCreateParameterValFromBackStackEntryCode("backStackEntry")
        }

        val screenMember = MemberName(screen.packageName, screen.name)

        val argsParametersString = screen.parameters
            .joinToString(separator = ",\n") { "${it.name} = ${it.name}" }

        return addStatement(
            """
            %L
            %M(
            %L
            )
        """.trimIndent(),
            createValsCode,
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
