package pro.yakuraion.destinationscompose.kspviewmodelkoin.parameters

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.FunSpec
import pro.yakuraion.destinationscompose.core.NotDestinationParameter
import pro.yakuraion.destinationscompose.kspcore.Import
import pro.yakuraion.destinationscompose.kspcore.parameters.Parameter
import pro.yakuraion.destinationscompose.kspcore.parameters.parcelable.ParcelableParameterConverter
import pro.yakuraion.destinationscompose.kspcore.parameters.primitive.PrimitiveParameterConverter
import pro.yakuraion.destinationscompose.kspcore.parameters.serializable.SerializableParameterConverter

@OptIn(KspExperimental::class)
class ViewModelParameter(ksParameter: KSValueParameter) : Parameter(ksParameter) {

    private val ksClassDeclaration: KSClassDeclaration = getViewModelKsClassDeclaration(ksParameter)
        ?: error("ksParameter is not ViewModel")

    private val viewModelClassName: String = ksClassDeclaration.qualifiedName?.asString().orEmpty()

    private val innerParameters: List<Parameter>

    init {
        val innerParametersConverters = listOf(
            PrimitiveParameterConverter(),
            ParcelableParameterConverter(),
            SerializableParameterConverter(),
        )
        innerParameters = ksClassDeclaration
            .primaryConstructor
            ?.parameters
            .orEmpty()
            .filter { it.getAnnotationsByType(NotDestinationParameter::class).none() }
            .mapNotNull { ksInnerParameter ->
                innerParametersConverters.firstNotNullOfOrNull { it.convert(ksInnerParameter) }
            }
    }

    override fun getImports(): List<Import> {
        val innerParametersImports = innerParameters.flatMap { it.getImports() }
        val viewModelParameterImports = listOf(
            Import("org.koin.androidx.compose", "koinViewModel"),
            Import("org.koin.core.parameter", "parametersOf"),
        )
        return innerParametersImports + viewModelParameterImports
    }

    override fun FunSpec.Builder.addComposableParameters(): FunSpec.Builder = this

    override fun getComposableRouteArguments(): List<ComposableRouteArgument> {
        return innerParameters.flatMap { it.getComposableRouteArguments() }
    }

    override fun getComposableCreateScreenParameterPropertiesCode(): String {
        val primitiveParametersCode = innerParameters.joinToString(separator = "\n") { primitiveParameter ->
            primitiveParameter.getComposableCreateScreenParameterPropertiesCode()
        }
        return """
            $primitiveParametersCode
            val $name = koinViewModel<$viewModelClassName> {
                parametersOf(
                    ${innerParameters.joinToString(separator = ", ") { it.name }}
                )
            }
        """.trimIndent()
    }

    override fun FunSpec.Builder.addNavigateParameters(): FunSpec.Builder {
        return innerParameters.fold(this) { builder, primitiveParameter ->
            with(primitiveParameter) {
                builder.addNavigateParameters()
            }
        }
    }

    override fun getNavigateRouteArguments(): List<NavigateRouteArgument> {
        return innerParameters.flatMap { it.getNavigateRouteArguments() }
    }

    override fun getNavigateCreateRouteArgumentPropertiesCode(): String {
        return innerParameters.joinToString(separator = "\n") { it.getNavigateCreateRouteArgumentPropertiesCode() }
    }

    companion object {

        private const val VIEW_MODEL_FULL_NAME = "androidx.lifecycle.ViewModel"

        fun getViewModelKsClassDeclaration(ksParameter: KSValueParameter): KSClassDeclaration? {
            val classDeclaration = ksParameter.type.resolve().declaration as? KSClassDeclaration ?: return null
            val isSuperTypesContainsViewModel = classDeclaration.getAllSuperTypes().any { superType ->
                superType.declaration.qualifiedName?.asString() == VIEW_MODEL_FULL_NAME
            }
            return if (isSuperTypesContainsViewModel) classDeclaration else null
        }
    }
}
