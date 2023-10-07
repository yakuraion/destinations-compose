package pro.yakuraion.treecomposenavigation.ksp.specs

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import pro.yakuraion.treecomposenavigation.ksp.ScreenDeclaration

private val navControllerClass = ClassName("androidx.navigation", "NavController")

fun createNavigationNavigateToFunSpec(
    screen: ScreenDeclaration,
): FunSpec {
    val funName = "navigateTo${screen.name}"

    return FunSpec.builder(funName)
        .receiver(navControllerClass)
        .addParameter(getRouteParameterSpec(screen))
        .addStatement(
            """
                navigate(route)
            """.trimIndent()
        )
        .build()
}

private fun getRouteParameterSpec(screen: ScreenDeclaration): ParameterSpec {
    return ParameterSpec.builder("route", String::class)
        .defaultValue("%S", screen.decapitalizedName)
        .build()
}
