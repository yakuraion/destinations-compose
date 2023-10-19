package pro.yakuraion.destinationscompose.ksp.screendeclaration

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.destinationscompose.kspcore.parameters.ParameterConverter

class ScreenDeclarationFactory(
    private val parametersConverters: List<ParameterConverter<*>>,
) {

    fun create(ksScreenFunction: KSFunctionDeclaration): ScreenDeclaration {
        val parameters = ksScreenFunction.parameters.mapNotNull { ksParameter ->
            parametersConverters.firstNotNullOfOrNull { it.convert(ksParameter) }
        }

        return ScreenDeclaration(
            packageName = ksScreenFunction.packageName.asString(),
            name = ksScreenFunction.simpleName.getShortName(),
            ksContainingFile = ksScreenFunction.containingFile!!,
            parameters = parameters,
        )
    }
}
