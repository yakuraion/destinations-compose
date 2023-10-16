package pro.yakuraion.treecomposenavigation.kspcore.parameters.primitive

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ScreenParametersExtractor

class PrimitiveScreenParametersExtractor : ScreenParametersExtractor<PrimitiveScreenParameter> {

    override fun extract(ksScreenFun: KSFunctionDeclaration): List<PrimitiveScreenParameter> {
        return ksScreenFun.parameters.mapNotNull { PrimitiveScreenParameter.create(it) }
    }
}
