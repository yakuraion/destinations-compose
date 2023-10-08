package pro.yakuraion.treecomposenavigation.ksp.args.lambda

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import pro.yakuraion.treecomposenavigation.ksp.args.Argument

class LambdaArgument(parameter: KSValueParameter) : Argument(parameter) {

    override fun modifyRoute(route: String): String = route

    override fun getComposableParameters(): List<ParameterSpec> {
        val spec = ParameterSpec
            .builder(name, parameter.type.toTypeName())
            .build()
        return listOf(spec)
    }

    override fun getNavigateToParameters(): List<ParameterSpec> = emptyList()
}
