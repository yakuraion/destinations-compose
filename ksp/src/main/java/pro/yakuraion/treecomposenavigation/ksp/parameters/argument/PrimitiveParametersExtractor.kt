package pro.yakuraion.treecomposenavigation.ksp.parameters.argument

import com.google.devtools.ksp.symbol.KSClassifierReference
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.ksp.parameters.ParametersExtractor

class PrimitiveParametersExtractor : ParametersExtractor<PrimitiveParameter> {

    override fun extract(screenFun: KSFunctionDeclaration): List<PrimitiveParameter> {

        return screenFun.parameters.mapNotNull { parameter ->
            val element = parameter.type.element as? KSClassifierReference
            val type = PrimitiveParameter.Type.values()
                .firstOrNull { element?.referencedName() == it.referencedName }
            val isNullable = parameter.type.resolve().isMarkedNullable
            type?.let { PrimitiveParameter(parameter, type, isNullable) }
        }
    }
}
