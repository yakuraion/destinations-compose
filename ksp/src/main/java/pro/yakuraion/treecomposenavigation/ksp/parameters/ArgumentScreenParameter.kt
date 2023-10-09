package pro.yakuraion.treecomposenavigation.ksp.parameters

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ParameterSpec

abstract class ArgumentScreenParameter(parameter: KSValueParameter) : ScreenParameter(parameter) {

    abstract fun getComposableQueryArguments(): List<QueryArgument>

    abstract fun getComposableCreateParameterValFromBackStackEntryCode(backStackEntryName: String): String

    abstract fun getNavigateToParametersSpecs(): List<ParameterSpec>

    abstract fun getNavigateToQueryArguments(): List<QueryValueArgument>

}

class QueryArgument(val name: String)

class QueryValueArgument(val name: String, val value: String)
