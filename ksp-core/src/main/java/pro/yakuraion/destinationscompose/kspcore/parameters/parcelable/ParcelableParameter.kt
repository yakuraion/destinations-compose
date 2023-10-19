package pro.yakuraion.destinationscompose.kspcore.parameters.parcelable

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.FunSpec
import pro.yakuraion.destinationscompose.kspcore.BACK_STACK_ENTRY_NAME
import pro.yakuraion.destinationscompose.kspcore.Import
import pro.yakuraion.destinationscompose.kspcore.parameters.Parameter

class ParcelableParameter(ksParameter: KSValueParameter) : Parameter(ksParameter) {

    private val ksClassDeclaration: KSClassDeclaration = getParcelableKsClassDeclaration(ksParameter)
        ?: error("ksParameter is not Parcelable")

    private val isNullable: Boolean = ksParameter.type.resolve().isMarkedNullable

    override fun getImports(): List<Import> {
        return listOf(
            Import("android.os", "Parcel"),
            Import("android.util", "Base64"),
        )
    }

    override fun FunSpec.Builder.addComposableParameters(): FunSpec.Builder = this

    override fun getComposableRouteArguments(): List<ComposableRouteArgument> {
        return listOf(ComposableRouteArgument(name))
    }

    override fun getComposableCreateScreenParameterPropertiesCode(): String {
        var className = ksClassDeclaration.qualifiedName?.asString().orEmpty()
        if (isNullable) {
            className = "$className?"
        }
        return """
            val ${name}Parcel = Parcel.obtain()
            val ${name}String = $BACK_STACK_ENTRY_NAME.arguments?.getString("$name")
            val ${name}ByteArray = ${name}String?.let { Base64.decode(it, Base64.NO_WRAP) } ?: ByteArray(0)
            ${name}Parcel.unmarshall(${name}ByteArray, 0, ${name}ByteArray.size)
            ${name}Parcel.setDataPosition(0)
            val $name = ${name}Parcel.readValue(${ksClassDeclaration.qualifiedName?.asString()}::class.java.classLoader) as $className
            ${name}Parcel.recycle()
        """.trimIndent()
    }

    override fun FunSpec.Builder.addNavigateParameters(): FunSpec.Builder {
        return addParameter(name, kpTypeName)
    }

    override fun getNavigateRouteArguments(): List<NavigateRouteArgument> {
        return listOf(NavigateRouteArgument(name, "_$name"))
    }

    override fun getNavigateCreateRouteArgumentPropertiesCode(): String {
        return """
            val ${name}Parcel = Parcel.obtain()
            ${name}Parcel.writeValue($name)
            val ${name}ByteArray = ${name}Parcel.marshall()
            val _${name} = Base64.encodeToString(${name}ByteArray, Base64.NO_WRAP)
            ${name}Parcel.recycle()
        """.trimIndent()
    }

    companion object {

        private const val PARCELABLE_FULL_NAME = "android.os.Parcelable"

        fun getParcelableKsClassDeclaration(ksParameter: KSValueParameter): KSClassDeclaration? {
            val classDeclaration = ksParameter.type.resolve().declaration as? KSClassDeclaration ?: return null
            val isInheritedFromParcelable = classDeclaration.getAllSuperTypes().any { supertype ->
                supertype.declaration.qualifiedName?.asString() == PARCELABLE_FULL_NAME
            }
            return if (isInheritedFromParcelable) classDeclaration else null
        }
    }
}
