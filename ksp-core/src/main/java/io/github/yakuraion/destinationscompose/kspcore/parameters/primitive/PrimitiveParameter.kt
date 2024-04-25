package io.github.yakuraion.destinationscompose.kspcore.parameters.primitive

import com.google.devtools.ksp.symbol.KSClassifierReference
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.FunSpec
import io.github.yakuraion.destinationscompose.kspcore.Import
import io.github.yakuraion.destinationscompose.kspcore.parameters.NavArgParameter

class PrimitiveParameter(
    ksParameter: KSValueParameter,
) : NavArgParameter(ksParameter) {

    private val type: Type = getPrimitiveType(ksParameter) ?: error("ksParameter is not primitive")

    private val isNullable: Boolean = ksParameter.type.resolve().isMarkedNullable

    private val composableNavArg = ComposableNavArg(name, getComposableNavArgDefaultValue())

    private val navigateParameter = NavigateParameter(name, kpTypeName, defaultValueLiteral)

    override fun getImports(): List<Import> {
        return listOf(
            Import("android.util", "Base64"),
        )
    }

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
        val encodedValueString = "$backStackName.arguments?.getString(\"${composableNavArg.name}\")"
        val decodedValueString = "$encodedValueString?.let·{ Base64.decode(it, Base64.NO_WRAP) }?.let { String(it) }"
        val value = "$decodedValueString${fromStringMethod}"
        return addStatement("val $parameterValFromBackStackName = $value")
    }

    override fun getNavigateParameters(): List<NavigateParameter> = listOf(navigateParameter)

    override fun FunSpec.Builder.createNavArgsValsFromNavigateParameters(): FunSpec.Builder {
        val toStringMethod = if (isNullable) "?.toString()" else ".toString()"
        val valueString = if (isNullable) {
            "${navigateParameter.name}$toStringMethod?.let·{ Base64.encodeToString(it.toByteArray(), Base64.NO_WRAP) }"
        } else {
            "Base64.encodeToString(${navigateParameter.name}$toStringMethod.toByteArray(), Base64.NO_WRAP)"
        }
        return addStatement("val·${composableNavArg.valInsideNavigateFunName}·= $valueString")
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
