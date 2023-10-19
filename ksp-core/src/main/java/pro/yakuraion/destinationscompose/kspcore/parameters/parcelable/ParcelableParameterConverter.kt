package pro.yakuraion.destinationscompose.kspcore.parameters.parcelable

import com.google.devtools.ksp.symbol.KSValueParameter
import pro.yakuraion.destinationscompose.kspcore.parameters.ParameterConverter

class ParcelableParameterConverter : ParameterConverter<ParcelableParameter> {

    override fun convert(ksParameter: KSValueParameter): ParcelableParameter? {
        return ParcelableParameter.getParcelableKsClassDeclaration(ksParameter)
            ?.let { ParcelableParameter(ksParameter) }
    }
}
