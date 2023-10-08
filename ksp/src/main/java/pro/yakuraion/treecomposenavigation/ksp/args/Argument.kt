package pro.yakuraion.treecomposenavigation.ksp.args

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ParameterSpec

abstract class Argument(protected val parameter: KSValueParameter) {

    val name = parameter.name!!.asString()

    open fun getScreenCallValueName(): String = name

    abstract fun modifyRoute(route: String): String

    abstract fun getComposableParameters(): List<ParameterSpec>

    abstract fun getNavigateToParameters(): List<ParameterSpec>

}
