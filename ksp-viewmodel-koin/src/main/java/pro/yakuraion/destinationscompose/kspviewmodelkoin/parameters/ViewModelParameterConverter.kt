package pro.yakuraion.destinationscompose.kspviewmodelkoin.parameters

import com.google.devtools.ksp.symbol.KSValueParameter
import pro.yakuraion.destinationscompose.kspcore.parameters.ParameterConverter

class ViewModelParameterConverter : ParameterConverter<ViewModelParameter> {

    override fun convert(ksParameter: KSValueParameter): ViewModelParameter? {
        return ViewModelParameter.getViewModelKsClassDeclaration(ksParameter)?.let { ViewModelParameter(ksParameter) }
    }
}
