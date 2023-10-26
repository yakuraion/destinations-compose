package io.github.yakuraion.destinationscompose.kspcore.parameters.parcelable

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.FunSpec
import io.github.yakuraion.destinationscompose.kspcore.Import
import io.github.yakuraion.destinationscompose.kspcore.parameters.NavArgParameter

class ParcelableParameter(ksParameter: KSValueParameter) : NavArgParameter(ksParameter) {

    private val ksClassDeclaration: KSClassDeclaration = getParcelableKsClassDeclaration(ksParameter) ?: error("ksParameter is not Parcelable")

    private val isNullable: Boolean = ksParameter.type.resolve().isMarkedNullable

    private val composableNavArg = ComposableNavArg(name, null)

    private val navigateParameter = NavigateParameter(name, kpTypeName, null)

    override fun getImports(): List<Import> {
        return listOf(
            Import("android.os", "Parcel"),
            Import("android.util", "Base64"),
        )
    }

    override fun getComposableNavArgs(): List<ComposableNavArg> = listOf(composableNavArg)

    override fun FunSpec.Builder.createParameterValFromBackStack(backStackName: String): FunSpec.Builder {
        val rawClassName = ksClassDeclaration.qualifiedName?.asString().orEmpty()
        val className = if (isNullable) "$rawClassName?" else rawClassName
        val valName = parameterValFromBackStackName
        return this
            .addStatement("val ${valName}Parcel = Parcel.obtain()")
            .addStatement("val ${valName}String = $backStackName.arguments?.getString(\"${composableNavArg.name}\")")
            .addStatement("val ${valName}ByteArray = ${valName}String?.let { Base64.decode(it, Base64.NO_WRAP) } ?: ByteArray(0)")
            .addStatement("${valName}Parcel.unmarshall(${valName}ByteArray, 0, ${valName}ByteArray.size)")
            .addStatement("${valName}Parcel.setDataPosition(0)")
            .addStatement("val $valName = ${valName}Parcel.readValue(${ksClassDeclaration.qualifiedName?.asString()}::class.java.classLoader) as $className")
            .addStatement("${valName}Parcel.recycle()")
            .addStatement("")
    }

    override fun getNavigateParameters(): List<NavigateParameter> = listOf(navigateParameter)

    override fun FunSpec.Builder.createNavArgsValsFromNavigateParameters(): FunSpec.Builder {
        val valName = composableNavArg.valInsideNavigateFunName
        return this
            .addStatement("val ${valName}Parcel = Parcel.obtain()")
            .addStatement("${valName}Parcel.writeValue(${navigateParameter.name})")
            .addStatement("val ${valName}ByteArray = ${valName}Parcel.marshall()")
            .addStatement("val $valName = Base64.encodeToString(${valName}ByteArray, Base64.NO_WRAP)")
            .addStatement("${valName}Parcel.recycle()")
            .addStatement("")
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
