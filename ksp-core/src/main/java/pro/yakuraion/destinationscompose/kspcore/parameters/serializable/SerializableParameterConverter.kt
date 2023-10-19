package pro.yakuraion.destinationscompose.kspcore.parameters.serializable

import com.google.devtools.ksp.symbol.KSValueParameter
import pro.yakuraion.destinationscompose.kspcore.parameters.ParameterConverter

class SerializableParameterConverter : ParameterConverter<SerializableParameter> {

    override fun convert(ksParameter: KSValueParameter): SerializableParameter? {
        return SerializableParameter.getSerializableKsClassDeclaration(ksParameter)
            ?.let { SerializableParameter(ksParameter) }
    }
}
