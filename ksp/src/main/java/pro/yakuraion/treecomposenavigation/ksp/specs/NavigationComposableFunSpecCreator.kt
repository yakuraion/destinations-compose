package pro.yakuraion.treecomposenavigation.ksp.specs

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toTypeName
import pro.yakuraion.treecomposenavigation.ksp.ScreenDeclaration

private val function1Class = ClassName("kotlin", "Function1")

private val navGraphBuilderClass = ClassName("androidx.navigation", "NavGraphBuilder")

private val animatedContentTransitionScopeClass = ClassName(
    "androidx.compose.animation",
    "AnimatedContentTransitionScope"
)

private val enterTransitionClass = ClassName("androidx.compose.animation", "EnterTransition")
private val exitTransitionClass = ClassName("androidx.compose.animation", "ExitTransition")

private val navBackStackEntryClass = ClassName("androidx.navigation", "NavBackStackEntry")

private val composableMember = MemberName("androidx.navigation.compose", "composable")

fun createNavigationComposableFunSpec(
    screen: ScreenDeclaration,
): FunSpec {
    val funName = "${screen.decapitalizedName}Composable"

    return FunSpec.builder(funName)
        .receiver(navGraphBuilderClass)
        .addParameters(getLambdaParametersSpecs(screen))
        .addParameter(getRouteParameterSpec(screen))
        .addParameter(getTransitionParameterSpec("enterTransition", enterTransitionClass, "null"))
        .addParameter(getTransitionParameterSpec("exitTransition", exitTransitionClass, "null"))
        .addParameter(getTransitionParameterSpec("popEnterTransition", enterTransitionClass, "enterTransition"))
        .addParameter(getTransitionParameterSpec("popExitTransition", exitTransitionClass, "exitTransition"))
        .addStatement(
            """
                %M(
                    route = route,
                    arguments = listOf(
                        
                    ),
                    enterTransition = enterTransition,
                    exitTransition = exitTransition,
                    popEnterTransition = popEnterTransition,
                    popExitTransition = popExitTransition,
                ) { backStackEntry ->
            """.trimIndent(),
            composableMember,
        )
        .addScreenCallStatement(screen)
        .addStatement("}")
        .build()
}

private fun getLambdaParametersSpecs(screen: ScreenDeclaration): List<ParameterSpec> {
    return screen.lambdaParameters.map { lambdaParameter ->
        ParameterSpec.builder(lambdaParameter.name!!.asString(), lambdaParameter.type.toTypeName())
            .build()
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

private fun FunSpec.Builder.addScreenCallStatement(screen: ScreenDeclaration): FunSpec.Builder {
    val screenMember = MemberName(screen.packageName, screen.name)
    val lambdaParametersString = screen.lambdaParameters.joinToString(separator = ",\n") { lambdaParameter ->
        val paramName = lambdaParameter.name!!.asString()
        "$paramName = $paramName"
    }
    return addStatement(
        """
            %M(
            %L
            )
        """.trimIndent(),
        screenMember,
        lambdaParametersString
    )
}
