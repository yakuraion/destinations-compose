package pro.yakuraion.destinationscompose.kspviewmodelkoin.parameters

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.FunSpec
import pro.yakuraion.destinationscompose.core.NotDestinationParameter
import pro.yakuraion.destinationscompose.kspcore.Import
import pro.yakuraion.destinationscompose.kspcore.parameters.NavArgParameter
import pro.yakuraion.destinationscompose.kspcore.parameters.parcelable.ParcelableParameterConverter
import pro.yakuraion.destinationscompose.kspcore.parameters.primitive.PrimitiveParameterConverter
import pro.yakuraion.destinationscompose.kspcore.parameters.serializable.SerializableParameterConverter

@OptIn(KspExperimental::class)
class ViewModelParameter(ksParameter: KSValueParameter) : NavArgParameter(ksParameter) {

    private val ksClassDeclaration: KSClassDeclaration = getViewModelKsClassDeclaration(ksParameter) ?: error("ksParameter is not ViewModel")

    private val viewModelClassName: String = ksClassDeclaration.qualifiedName?.asString().orEmpty()

    private val innerParametersExtractors = listOf(
            PrimitiveParameterConverter(),
            ParcelableParameterConverter(),
            SerializableParameterConverter(),
    )

    private val innerParameters: List<NavArgParameter> = extractInnerParameters()

    private val composableNavArgs = innerParameters.flatMap { it.getComposableNavArgs() }

    private val navigateParameters = innerParameters.flatMap { it.getNavigateParameters() }

    private fun extractInnerParameters(): List<NavArgParameter> {
        return ksClassDeclaration
                .primaryConstructor
                ?.parameters
                .orEmpty()
                .filter { it.getAnnotationsByType(NotDestinationParameter::class).none() }
                .mapNotNull { ksInnerParameter ->
                    innerParametersExtractors.firstNotNullOfOrNull { it.convert(ksInnerParameter) }
                }
    }

    override fun getImports(): List<Import> {
        val innerParametersImports = innerParameters.flatMap { it.getImports() }
        val viewModelParameterImports = listOf(
                Import("org.koin.androidx.compose", "koinViewModel"),
                Import("org.koin.core.parameter", "parametersOf"),
        )
        return (innerParametersImports + viewModelParameterImports).distinct()
    }

    override fun getComposableNavArgs(): List<ComposableNavArg> = composableNavArgs

    override fun FunSpec.Builder.createParameterValFromBackStack(backStackName: String): FunSpec.Builder {
        val innerParametersVals = innerParameters.map { it.parameterValFromBackStackName }
        return innerParameters.fold(this) { builder, innerParameter ->
            with(innerParameter) {
                builder.createParameterValFromBackStack(backStackName)
            }
        }
                .beginControlFlow("val $parameterValFromBackStackName = koinViewModel<$viewModelClassName>")
                .addStatement("parametersOf(${innerParametersVals.joinToString(separator = ", ")})")
                .endControlFlow()
    }

    override fun getNavigateParameters(): List<NavigateParameter> = navigateParameters

    override fun FunSpec.Builder.createNavArgsValsFromNavigateParameters(): FunSpec.Builder {
        return innerParameters.fold(this) { builder, innerParameter ->
            with(innerParameter) {
                builder.createNavArgsValsFromNavigateParameters()
            }
        }
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
