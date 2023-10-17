package pro.yakuraion.treecomposenavigation.kspcore.parameters

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toTypeName
import pro.yakuraion.treecomposenavigation.kspcore.Import

abstract class Parameter(ksParameter: KSValueParameter) {

    val name: String = ksParameter.name!!.asString()

    val kpTypeName: TypeName = ksParameter.type.toTypeName()

    open fun getImports(): List<Import> = emptyList()

    abstract fun FunSpec.Builder.addComposableParameters(): FunSpec.Builder

    abstract fun getComposableRouteArguments(): List<ComposableRouteArgument>

    data class ComposableRouteArgument(val name: String)

    abstract fun getComposableCreateScreenParameterPropertiesCode(): String

    abstract fun FunSpec.Builder.addNavigateParameters(): FunSpec.Builder

    abstract fun getNavigateRouteArguments(): List<NavigateRouteArgument>

    data class NavigateRouteArgument(val name: String, val propertyName: String)

    abstract fun getNavigateCreateRouteArgumentPropertiesCode(): String
}
