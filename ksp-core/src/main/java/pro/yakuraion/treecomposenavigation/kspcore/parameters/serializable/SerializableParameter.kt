package pro.yakuraion.treecomposenavigation.kspcore.parameters.serializable

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.FunSpec
import pro.yakuraion.treecomposenavigation.kspcore.BACK_STACK_ENTRY_NAME
import pro.yakuraion.treecomposenavigation.kspcore.Import
import pro.yakuraion.treecomposenavigation.kspcore.parameters.Parameter
import java.io.Serializable

class SerializableParameter(ksParameter: KSValueParameter) : Parameter(ksParameter) {

    private val ksClassDeclaration: KSClassDeclaration = getSerializableKsClassDeclaration(ksParameter)
        ?: error("ksParameter is not Serializable")

    private val isNullable: Boolean = ksParameter.type.resolve().isMarkedNullable

    override fun getImports(): List<Import> {
        return listOf(
            Import("android.util", "Base64"),
            Import("java.io", "ByteArrayInputStream"),
            Import("java.io", "ByteArrayOutputStream"),
            Import("java.io", "ObjectInputStream"),
            Import("java.io", "ObjectOutputStream"),
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
            val ${name}Encoded = $BACK_STACK_ENTRY_NAME.arguments?.getString("$name")
            val ${name}Data = Base64.decode(${name}Encoded, Base64.NO_WRAP)
            val ${name}OIS = ObjectInputStream(ByteArrayInputStream(${name}Data))
            val $name = ${name}OIS.readObject() as $className
            ${name}OIS.close()
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
            val ${name}BAOS = ByteArrayOutputStream()
            val ${name}OOS = ObjectOutputStream(${name}BAOS)
            ${name}OOS.writeObject($name)
            ${name}OOS.close()
            val _${name} = Base64.encodeToString(${name}BAOS.toByteArray(), Base64.NO_WRAP)
        """.trimIndent()
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
