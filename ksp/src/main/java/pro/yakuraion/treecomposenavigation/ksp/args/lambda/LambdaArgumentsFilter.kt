package pro.yakuraion.treecomposenavigation.ksp.args.lambda

import com.google.devtools.ksp.symbol.KSCallableReference
import com.google.devtools.ksp.symbol.KSValueParameter
import pro.yakuraion.treecomposenavigation.ksp.args.ArgumentsFilter

class LambdaArgumentsFilter : ArgumentsFilter<LambdaArgument> {

    override fun filter(parameters: List<KSValueParameter>): List<LambdaArgument> {
        val lambdaParameters = parameters.filter { it.type.element is KSCallableReference }
        return lambdaParameters.map { LambdaArgument(it) }
    }
}
