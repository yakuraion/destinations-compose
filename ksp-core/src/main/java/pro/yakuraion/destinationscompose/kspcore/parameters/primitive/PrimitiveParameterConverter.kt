package pro.yakuraion.destinationscompose.kspcore.parameters.primitive

import com.google.devtools.ksp.symbol.KSValueParameter
import pro.yakuraion.destinationscompose.kspcore.parameters.ParameterConverter

class PrimitiveParameterConverter : ParameterConverter<PrimitiveParameter> {

    override fun convert(ksParameter: KSValueParameter): PrimitiveParameter? {
        return PrimitiveParameter.getPrimitiveType(ksParameter)?.let { PrimitiveParameter(ksParameter) }
    }
}
