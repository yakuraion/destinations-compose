package pro.yakuraion.destinationscompose.kspcore.parameters.lambda

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.FunSpec
import pro.yakuraion.destinationscompose.kspcore.parameters.Parameter

class LambdaParameter(ksParameter: KSValueParameter) : Parameter(ksParameter) {

    override fun FunSpec.Builder.addComposableParameters(): FunSpec.Builder {
        return addParameter(name, kpTypeName)
    }

    override fun getComposableRouteArguments(): List<ComposableRouteArgument> = emptyList()

    override fun getComposableCreateScreenParameterPropertiesCode(): String = ""

    override fun FunSpec.Builder.addNavigateParameters(): FunSpec.Builder = this

    override fun getNavigateRouteArguments(): List<NavigateRouteArgument> = emptyList()

    override fun getNavigateCreateRouteArgumentPropertiesCode(): String = ""
}
