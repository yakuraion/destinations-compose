package pro.yakuraion.treecomposenavigation.kspviewmodelkoin.parameters

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ParameterSpec
import pro.yakuraion.treecomposenavigation.kspcore.Import
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ArgumentsScreenParameter
import pro.yakuraion.treecomposenavigation.kspcore.parameters.PatternQueryArgument
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ValueQueryArgument
import pro.yakuraion.treecomposenavigation.kspcore.parameters.primitive.PrimitiveScreenParameter

class ViewModelScreenParameter private constructor(
    ksParameter: KSValueParameter,
    private val classDeclaration: KSClassDeclaration,
) : ArgumentsScreenParameter(ksParameter) {

    private val innerPrimitiveParameters: List<PrimitiveScreenParameter> = classDeclaration
        .primaryConstructor
        ?.parameters
        .orEmpty()
        .mapNotNull { PrimitiveScreenParameter.create(it) }

    override fun getImports(): List<Import> {
        return listOf(
            Import("org.koin.androidx.compose", "koinViewModel"),
            Import("org.koin.core.parameter", "parametersOf"),
        )
    }

    override fun getPatternQueryArguments(): List<PatternQueryArgument> {
        return innerPrimitiveParameters.flatMap { it.getPatternQueryArguments() }
    }

    override fun getPropertiesForScreenCallStatement(backStackEntryName: String): String {
        val innerPrimitiveParametersCode = innerPrimitiveParameters.joinToString(separator = "\n") { parameter ->
            parameter.getPropertiesForScreenCallStatement(backStackEntryName)
        }
        val viewModelCode = """
            val $name = koinViewModel<${classDeclaration.qualifiedName?.asString()}> { 
                parametersOf(
                ${innerPrimitiveParameters.joinToString(separator = ", ") { it.name }}
                )
            }
        """.trimIndent()
        return innerPrimitiveParametersCode + "\n" + viewModelCode
    }

    override fun getPropertiesForValueQueryArgumentsStatement(): String {
        return innerPrimitiveParameters.joinToString(separator = "\n") { it.getPropertiesForValueQueryArgumentsStatement() }
    }

    override fun getValueQueryArguments(): List<ValueQueryArgument> {
        return innerPrimitiveParameters.flatMap { it.getValueQueryArguments() }
    }

    override fun getArgumentsNavigationParametersSpecs(): List<ParameterSpec> {
        return innerPrimitiveParameters.flatMap { it.getArgumentsNavigationParametersSpecs() }
    }

    companion object {

        private const val VIEW_MODEL_FULL_NAME = "androidx.lifecycle.ViewModel"

        fun create(ksParameter: KSValueParameter): ViewModelScreenParameter? {
            val classDeclaration = ksParameter.type.resolve().declaration as? KSClassDeclaration ?: return null
            val isSuperTypesContainsViewModel = classDeclaration.getAllSuperTypes().any { superType ->
                superType.declaration.qualifiedName?.asString() == VIEW_MODEL_FULL_NAME
            }
            return if (isSuperTypesContainsViewModel) {
                ViewModelScreenParameter(ksParameter, classDeclaration)
            } else {
                null
            }
        }
    }
}
