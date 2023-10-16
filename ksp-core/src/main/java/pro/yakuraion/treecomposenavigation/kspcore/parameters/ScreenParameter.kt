package pro.yakuraion.treecomposenavigation.kspcore.parameters

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toTypeName
import pro.yakuraion.treecomposenavigation.kspcore.Import

abstract class ScreenParameter(val ksParameter: KSValueParameter) {

    val name: String = ksParameter.name!!.asString()

    val kpTypeName: TypeName = ksParameter.type.toTypeName()

    open fun getImports(): List<Import> = emptyList()
}
