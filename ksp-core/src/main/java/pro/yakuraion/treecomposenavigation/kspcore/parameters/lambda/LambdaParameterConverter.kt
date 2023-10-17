package pro.yakuraion.treecomposenavigation.kspcore.parameters.lambda

import com.google.devtools.ksp.symbol.KSCallableReference
import com.google.devtools.ksp.symbol.KSValueParameter
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ParameterConverter

class LambdaParameterConverter : ParameterConverter<LambdaParameter> {

    override fun convert(ksParameter: KSValueParameter): LambdaParameter? {
        return (ksParameter.type.element as? KSCallableReference)?.let { LambdaParameter(ksParameter) }
    }
}
