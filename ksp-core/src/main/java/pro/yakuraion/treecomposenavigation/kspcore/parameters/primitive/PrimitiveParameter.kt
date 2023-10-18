package pro.yakuraion.treecomposenavigation.kspcore.parameters.primitive

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSClassifierReference
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import pro.yakuraion.treecomposenavigation.core.DestinationOptionalParameter
import pro.yakuraion.treecomposenavigation.kspcore.BACK_STACK_ENTRY_NAME
import pro.yakuraion.treecomposenavigation.kspcore.parameters.Parameter

class PrimitiveParameter(
    ksParameter: KSValueParameter,
) : Parameter(ksParameter) {

    private val type: Type = getPrimitiveType(ksParameter)
        ?: error("ksParameter is not primitive")

    private val isNullable: Boolean = ksParameter.type.resolve().isMarkedNullable

    @OptIn(KspExperimental::class)
    private val defaultValueLiteral: String? = ksParameter
        .getAnnotationsByType(DestinationOptionalParameter::class)
        .firstOrNull()
        ?.defaultValue

    override fun FunSpec.Builder.addComposableParameters(): FunSpec.Builder = this

    override fun getComposableRouteArguments(): List<ComposableRouteArgument> {
        return listOf(ComposableRouteArgument(name))
    }

    override fun getComposableCreateScreenParameterPropertiesCode(): String {
        var fromStringMethod = type.fromStringMethod
        if (!isNullable) {
            fromStringMethod = "$fromStringMethod!!"
        }
        return """
            val $name = $BACK_STACK_ENTRY_NAME.arguments?.getString("$name")${fromStringMethod}
        """.trimIndent()
    }

    override fun FunSpec.Builder.addNavigateParameters(): FunSpec.Builder {
        val parameterSpec = ParameterSpec.builder(name, kpTypeName)
            .run { defaultValueLiteral?.let { defaultValue(defaultValueLiteral) } ?: this }
            .build()
        return addParameter(parameterSpec)
    }

    override fun getNavigateRouteArguments(): List<NavigateRouteArgument> {
        return listOf(NavigateRouteArgument(name, "_$name"))
    }

    override fun getNavigateCreateRouteArgumentPropertiesCode(): String {
        var toStringMethod = ".toString()"
        if (isNullable) {
            toStringMethod = "?$toStringMethod"
        }
        return """
            val _${name} = $name$toStringMethod
        """.trimIndent()
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
