package pro.yakuraion.treecomposenavigation.ksp.parameters

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toTypeName

abstract class ScreenParameter(parameter: KSValueParameter) {

    val name: String = parameter.name!!.asString()

    val typeName: TypeName = parameter.type.toTypeName()
}
