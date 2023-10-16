package pro.yakuraion.treecomposenavigation.kspcore.parameters

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ParameterSpec

abstract class ArgumentsScreenParameter(ksParameter: KSValueParameter) : ScreenParameter(ksParameter) {

    abstract fun getPatternQueryArguments(): List<PatternQueryArgument>

    abstract fun getPropertiesForScreenCallStatement(backStackEntryName: String): String

    abstract fun getPropertiesForValueQueryArgumentsStatement(): String

    abstract fun getValueQueryArguments(): List<ValueQueryArgument>

    abstract fun getArgumentsNavigationParametersSpecs(): List<ParameterSpec>
}

class PatternQueryArgument(val name: String)

class ValueQueryArgument(val name: String, val valuePropertyName: String)
