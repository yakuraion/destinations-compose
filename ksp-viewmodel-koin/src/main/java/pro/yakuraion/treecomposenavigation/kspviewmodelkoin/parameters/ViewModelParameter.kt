package pro.yakuraion.treecomposenavigation.kspviewmodelkoin.parameters

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.FunSpec
import pro.yakuraion.treecomposenavigation.kspcore.Import
import pro.yakuraion.treecomposenavigation.kspcore.parameters.Parameter
import pro.yakuraion.treecomposenavigation.kspcore.parameters.primitive.PrimitiveParameter
import pro.yakuraion.treecomposenavigation.kspcore.parameters.primitive.PrimitiveParameterConverter

class ViewModelParameter(ksParameter: KSValueParameter) : Parameter(ksParameter) {

    private val ksClassDeclaration: KSClassDeclaration = getViewModelKsClassDeclaration(ksParameter)
        ?: error("ksParameter is not ViewModel")

    private val viewModelClassName: String = ksClassDeclaration.qualifiedName?.asString().orEmpty()

    private val innerPrimitiveParameters: List<PrimitiveParameter>

    init {
        val primitiveParameterConverter = PrimitiveParameterConverter()
        innerPrimitiveParameters = ksClassDeclaration
            .primaryConstructor
            ?.parameters
            ?.mapNotNull { primitiveParameterConverter.convert(it) }
            .orEmpty()
    }

    override fun getImports(): List<Import> {
        return listOf(
            Import("org.koin.androidx.compose", "koinViewModel"),
            Import("org.koin.core.parameter", "parametersOf"),
        )
    }

    override fun FunSpec.Builder.addComposableParameters(): FunSpec.Builder = this

    override fun getComposableRouteArguments(): List<ComposableRouteArgument> {
        return innerPrimitiveParameters.flatMap { it.getComposableRouteArguments() }
    }

    override fun getComposableCreateScreenParameterPropertiesCode(): String {
        val primitiveParametersCode = innerPrimitiveParameters.joinToString(separator = "\n") { primitiveParameter ->
            primitiveParameter.getComposableCreateScreenParameterPropertiesCode()
        }
        return """
            $primitiveParametersCode
            val $name = koinViewModel<$viewModelClassName> {
                parametersOf(
                    ${innerPrimitiveParameters.joinToString(separator = ", ") { it.name }}
                )
            }
        """.trimIndent()
    }

    override fun FunSpec.Builder.addNavigateParameters(): FunSpec.Builder {
        return innerPrimitiveParameters.fold(this) { builder, primitiveParameter ->
            with(primitiveParameter) {
                builder.addNavigateParameters()
            }
        }
    }

    override fun getNavigateRouteArguments(): List<NavigateRouteArgument> {
        return innerPrimitiveParameters.flatMap { it.getNavigateRouteArguments() }
    }

    override fun getNavigateCreateRouteArgumentPropertiesCode(): String {
        return innerPrimitiveParameters.joinToString(separator = "\n") { it.getNavigateCreateRouteArgumentPropertiesCode() }
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
