package pro.yakuraion.treecomposenavigation.kspviewmodelkoin.parameters

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ScreenParametersExtractor

class ViewModelScreenParametersExtractor : ScreenParametersExtractor<ViewModelScreenParameter> {

    override fun extract(ksScreenFun: KSFunctionDeclaration): List<ViewModelScreenParameter> {
        return ksScreenFun.parameters.mapNotNull { ViewModelScreenParameter.create(it) }
    }
}
