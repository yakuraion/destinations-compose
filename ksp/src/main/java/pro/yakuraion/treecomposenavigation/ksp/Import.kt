package pro.yakuraion.treecomposenavigation.ksp

import com.squareup.kotlinpoet.FileSpec

class Import(val packageName: String, val name: String)

fun FileSpec.Builder.addImports(list: List<Import>): FileSpec.Builder {
    return list.fold(this) { builder, import ->
        builder.addImport(packageName = import.packageName, names = listOf(import.name))
    }
}
