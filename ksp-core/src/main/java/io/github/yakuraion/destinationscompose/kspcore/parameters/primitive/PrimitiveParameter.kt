package io.github.yakuraion.destinationscompose.kspcore.parameters.primitive

import com.google.devtools.ksp.symbol.KSClassifierReference
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.FunSpec
import io.github.yakuraion.destinationscompose.kspcore.parameters.NavArgParameter

class PrimitiveParameter(
    ksParameter: KSValueParameter,
) : NavArgParameter(ksParameter) {

    private val type: Type = getPrimitiveType(ksParameter) ?: error("ksParameter is not primitive")

    private val isNullable: Boolean = ksParameter.type.resolve().isMarkedNullable

    private val composableNavArg = ComposableNavArg(name, getComposableNavArgDefaultValue())

    private val navigateParameter = NavigateParameter(name, kpTypeName, defaultValueLiteral)

    override fun getComposableNavArgs(): List<ComposableNavArg> = listOf(composableNavArg)

    private fun getComposableNavArgDefaultValue(): ComposableNavArg.DefaultValue? {
        return defaultValueLiteral?.let { defaultValueLiteral ->
            if (defaultValueLiteral == "null") {
                ComposableNavArg.DefaultValue(null)
            } else {
                ComposableNavArg.DefaultValue(defaultValueLiteral.replace("\"", ""))
            }
        }
    }

    override fun FunSpec.Builder.createParameterValFromBackStack(backStackName: String): FunSpec.Builder {
        val fromStringMethod = if (isNullable) type.fromStringMethod else "${type.fromStringMethod}!!"
        return addStatement("val $parameterValFromBackStackName = $backStackName.arguments?.getString(\"${composableNavArg.name}\")${fromStringMethod}")
    }

    override fun getNavigateParameters(): List<NavigateParameter> = listOf(navigateParameter)

    override fun FunSpec.Builder.createNavArgsValsFromNavigateParameters(): FunSpec.Builder {
        val toStringMethod = if (isNullable) "?.toString()" else ".toString()"
        return addStatement("val ${composableNavArg.valInsideNavigateFunName} = ${navigateParameter.name}$toStringMethod")
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

        fun getPrimitiveType(ksParameter: KSValueParameter): Type? {
            val element = ksParameter.type.element as? KSClassifierReference ?: return null
            return Type.values().firstOrNull { it.referencedName == element.referencedName() }
        }
    }
}
