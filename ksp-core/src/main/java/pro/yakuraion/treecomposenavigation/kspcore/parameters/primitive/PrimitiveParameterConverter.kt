package pro.yakuraion.treecomposenavigation.kspcore.parameters.primitive

import com.google.devtools.ksp.symbol.KSValueParameter
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ParameterConverter

class PrimitiveParameterConverter : ParameterConverter<PrimitiveParameter> {

    override fun convert(ksParameter: KSValueParameter): PrimitiveParameter? {
        return PrimitiveParameter.getPrimitiveType(ksParameter)?.let { PrimitiveParameter(ksParameter) }
    }
}
