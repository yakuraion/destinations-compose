package io.github.yakuraion.destinationscompose.kspcore.parameters.serializable

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.FunSpec
import io.github.yakuraion.destinationscompose.kspcore.Import
import io.github.yakuraion.destinationscompose.kspcore.parameters.NavArgParameter
import java.io.Serializable

class SerializableParameter(ksParameter: KSValueParameter) : NavArgParameter(ksParameter) {

    private val ksClassDeclaration: KSClassDeclaration = getSerializableKsClassDeclaration(ksParameter) ?: error("ksParameter is not Serializable")

    private val isNullable: Boolean = ksParameter.type.resolve().isMarkedNullable

    private val composableNavArg = ComposableNavArg(name, null)

    private val navigateParameter = NavigateParameter(name, kpTypeName, null)

    override fun getImports(): List<Import> {
        return listOf(
            Import("android.util", "Base64"),
            Import("java.io", "ByteArrayInputStream"),
            Import("java.io", "ByteArrayOutputStream"),
            Import("java.io", "ObjectInputStream"),
            Import("java.io", "ObjectOutputStream"),
        )
    }

    override fun getComposableNavArgs(): List<ComposableNavArg> = listOf(composableNavArg)

    override fun FunSpec.Builder.createParameterValFromBackStack(backStackName: String): FunSpec.Builder {

        val rawClassName = ksClassDeclaration.qualifiedName?.asString().orEmpty()
        val className = if (isNullable) "$rawClassName?" else rawClassName
        val valName = parameterValFromBackStackName
        return this
            .addStatement("val ${valName}Encoded = $backStackName.arguments?.getString(\"${composableNavArg.name}\")")
            .addStatement("val ${valName}Data = Base64.decode(${valName}Encoded, Base64.NO_WRAP)")
            .addStatement("val ${valName}OIS = ObjectInputStream(ByteArrayInputStream(${valName}Data))")
            .addStatement("val $valName = ${valName}OIS.readObject() as $className")
            .addStatement("${valName}OIS.close()")
            .addStatement("")
    }

    override fun getNavigateParameters(): List<NavigateParameter> = listOf(navigateParameter)

    override fun FunSpec.Builder.createNavArgsValsFromNavigateParameters(): FunSpec.Builder {
        val valName = composableNavArg.valInsideNavigateFunName
        return this
            .addStatement("val ${valName}BAOS = ByteArrayOutputStream()")
            .addStatement("val ${valName}OOS = ObjectOutputStream(${valName}BAOS)")
            .addStatement("${valName}OOS.writeObject(${navigateParameter.name})")
            .addStatement("${valName}OOS.close()")
            .addStatement("val $valName = Base64.encodeToString(${valName}BAOS.toByteArray(), Base64.NO_WRAP)")
            .addStatement("")
    }

    companion object {

        fun getSerializableKsClassDeclaration(ksParameter: KSValueParameter): KSClassDeclaration? {
            val classDeclaration = ksParameter.type.resolve().declaration as? KSClassDeclaration ?: return null
            val isInheritedFromSerializable = classDeclaration.getAllSuperTypes().any { supertype ->
                supertype.declaration.qualifiedName?.asString() == Serializable::class.qualifiedName
            }
            return if (isInheritedFromSerializable) classDeclaration else null
        }
    }
}
