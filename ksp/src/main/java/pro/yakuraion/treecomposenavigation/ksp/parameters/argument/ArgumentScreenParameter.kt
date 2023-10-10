package pro.yakuraion.treecomposenavigation.ksp.parameters.argument

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ParameterSpec
import pro.yakuraion.treecomposenavigation.ksp.parameters.ScreenParameter

abstract class ArgumentScreenParameter(parameter: KSValueParameter) : ScreenParameter(parameter) {

    abstract fun getPatternQueryArguments(): List<PatternQueryArgument>

    abstract fun getPropertiesForScreenCallCode(backStackEntryName: String, propertyName: String): String

    abstract fun getPropertiesForValueQueryArgumentsCode(): String

    abstract fun getValueQueryArguments(): List<ValueQueryArgument>

    abstract fun getArgumentsAsParametersSpecs(): List<ParameterSpec>
}

class PatternQueryArgument(val name: String)

class ValueQueryArgument(val name: String, val valuePropertyName: String)
