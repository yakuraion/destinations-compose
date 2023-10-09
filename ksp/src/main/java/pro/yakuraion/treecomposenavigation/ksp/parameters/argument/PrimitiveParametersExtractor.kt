package pro.yakuraion.treecomposenavigation.ksp.parameters.argument

import com.google.devtools.ksp.symbol.KSClassifierReference
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.ksp.parameters.ScreenParametersExtractor

class PrimitiveParametersExtractor : ScreenParametersExtractor<PrimitiveScreenParameter> {

    override fun extract(screenFun: KSFunctionDeclaration): List<PrimitiveScreenParameter> {

        return screenFun.parameters.mapNotNull { parameter ->
            val element = parameter.type.element as? KSClassifierReference
            val type = PrimitiveScreenParameter.Type.values()
                .firstOrNull { element?.referencedName() == it.referencedName }
            val isNullable = parameter.type.resolve().isMarkedNullable
            type?.let { PrimitiveScreenParameter(parameter, type, isNullable) }
        }
    }
}
