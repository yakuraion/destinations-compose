package pro.yakuraion.destinationscompose.kspcore.parameters.lambda

import com.google.devtools.ksp.symbol.KSCallableReference
import com.google.devtools.ksp.symbol.KSValueParameter
import pro.yakuraion.destinationscompose.kspcore.parameters.ParameterConverter

class LambdaParameterConverter : ParameterConverter<LambdaParameter> {

    override fun convert(ksParameter: KSValueParameter): LambdaParameter? {
        return (ksParameter.type.element as? KSCallableReference)?.let { LambdaParameter(ksParameter) }
    }
}
