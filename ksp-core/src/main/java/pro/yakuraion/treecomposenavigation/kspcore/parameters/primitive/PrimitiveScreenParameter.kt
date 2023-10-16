package pro.yakuraion.treecomposenavigation.kspcore.parameters.primitive

import com.google.devtools.ksp.symbol.KSClassifierReference
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ParameterSpec
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ArgumentsScreenParameter
import pro.yakuraion.treecomposenavigation.kspcore.parameters.PatternQueryArgument
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ValueQueryArgument

class PrimitiveScreenParameter private constructor(
    ksParameter: KSValueParameter,
    private val type: Type,
) : ArgumentsScreenParameter(ksParameter) {

    private val isNullable: Boolean = ksParameter.type.resolve().isMarkedNullable

    override fun getPatternQueryArguments(): List<PatternQueryArgument> {
        return listOf(PatternQueryArgument(name))
    }

    override fun getPropertiesForScreenCallStatement(backStackEntryName: String): String {
        val fromStringMethod = if (isNullable) type.fromStringMethod else "${type.fromStringMethod}!!"
        return """
            val $name = $backStackEntryName.arguments?.getString("$name")${fromStringMethod}
        """.trimIndent()
    }

    override fun getPropertiesForValueQueryArgumentsStatement(): String {
        return "val _${name} = ${name}${if (isNullable) "?" else ""}.toString()"
    }

    override fun getValueQueryArguments(): List<ValueQueryArgument> {
        return listOf(
            ValueQueryArgument(
                name = name,
                valuePropertyName = "_$name",
            )
        )
    }

    override fun getArgumentsNavigationParametersSpecs(): List<ParameterSpec> {
        val spec = ParameterSpec
            .builder(name, kpTypeName)
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

    companion object {

        fun create(ksParameter: KSValueParameter): PrimitiveScreenParameter? {
            val element = ksParameter.type.element as? KSClassifierReference
            val type = PrimitiveScreenParameter.Type.values().firstOrNull { element?.referencedName() == it.referencedName }
            return type?.let { PrimitiveScreenParameter(ksParameter, type) }
        }
    }
}
