package io.github.yakuraion.destinationscompose.kspcore.parameters

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeName
import io.github.yakuraion.destinationscompose.core.DestinationOptionalParameter

abstract class NavArgParameter(ksParameter: KSValueParameter) : Parameter(ksParameter) {

    val parameterValFromBackStackName = name

    @OptIn(KspExperimental::class)
    val defaultValueLiteral: String? = ksParameter
        .getAnnotationsByType(DestinationOptionalParameter::class)
        .firstOrNull()
        ?.defaultValue

    abstract fun getComposableNavArgs(): List<ComposableNavArg>

    abstract fun FunSpec.Builder.createParameterValFromBackStack(backStackName: String): FunSpec.Builder

    abstract fun getNavigateParameters(): List<NavigateParameter>

    abstract fun FunSpec.Builder.createNavArgsValsFromNavigateParameters(): FunSpec.Builder

    data class ComposableNavArg(val name: String, val defaultValue: DefaultValue?) {

        val valInsideNavigateFunName = "_$name"

        data class DefaultValue(val value: String?)
    }

    data class NavigateParameter(val name: String, val type: TypeName, val defaultValueLiteral: String?)
}
