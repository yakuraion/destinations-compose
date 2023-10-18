package pro.yakuraion.treecomposenavigation.kspcore.parameters.parcelable

import com.google.devtools.ksp.symbol.KSValueParameter
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ParameterConverter

class ParcelableParameterConverter : ParameterConverter<ParcelableParameter> {

    override fun convert(ksParameter: KSValueParameter): ParcelableParameter? {
        return ParcelableParameter.getParcelableKsClassDeclaration(ksParameter)
            ?.let { ParcelableParameter(ksParameter) }
    }
}
