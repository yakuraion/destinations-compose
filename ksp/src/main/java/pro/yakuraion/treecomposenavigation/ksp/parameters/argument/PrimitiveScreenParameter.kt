package pro.yakuraion.treecomposenavigation.ksp.parameters.argument

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ParameterSpec

class PrimitiveScreenParameter(
    parameter: KSValueParameter,
    private val type: Type,
    private val isNullable: Boolean,
) : ArgumentScreenParameter(parameter) {

    override fun getPatternQueryArguments(): List<PatternQueryArgument> {
        return listOf(PatternQueryArgument(name))
    }

    override fun getPropertiesForScreenCallCode(backStackEntryName: String, propertyName: String): String {
        val fromStringMethod = if (isNullable) type.fromStringMethod else "${type.fromStringMethod}!!"
        return """
            val $propertyName = $backStackEntryName.arguments?.getString("$name")${fromStringMethod}
        """.trimIndent()
    }

    override fun getPropertiesForValueQueryArgumentsCode(): String {
        return "val _${name} = ${name}${ if (isNullable) "?" else "" }.toString()"
    }

    override fun getValueQueryArguments(): List<ValueQueryArgument> {
        return listOf(
            ValueQueryArgument(
                name = name,
                valuePropertyName = "_$name",
            )
        )
    }

    override fun getArgumentsAsParametersSpecs(): List<ParameterSpec> {
        val spec = ParameterSpec
            .builder(name, typeName)
            .build()
        return listOf(spec)
    }

    enum class Type(val referencedName: String, val fromStringMethod: String) {
        BYTE("Byte", "?.toByte()"),
        U_BYTE("UByte", "?.toUByte()"),
        SHORT("Short", "?.toShort()"),
        U_SHORT("UShort", "?.toUShort()"),
        INT("Int", "?.toInt()"),
        U_INT("UInt", "?.toUInt()"),
        LONG("Long", "?.toLong()"),
        U_LONG("ULong", "?.toULong()"),

        FLOAT("Float", "?.toFloat()"),
        DOUBLE("Double", "?.toDouble()"),

        BOOLEAN("Boolean", "?.toBoolean()"),

        CHAR("Char", "?.first()"),

        STRING("String", ""),
    }
}
