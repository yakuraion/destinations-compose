package io.github.yakuraion.destinationscompose.ksp.specs

import com.squareup.kotlinpoet.FunSpec
import io.github.yakuraion.destinationscompose.ksp.screendeclaration.ScreenDeclaration

class GetDefaultRouteFunCreator : FunCreator {

    override fun createKpFunSpec(screen: ScreenDeclaration): FunSpec {
        val funName = "get${screen.name}DefaultRoute"

        return FunSpec.builder(funName)
            .returns(String::class)
            .addStatement("return %S", screen.defaultRouteName)
            .build()
    }
}
