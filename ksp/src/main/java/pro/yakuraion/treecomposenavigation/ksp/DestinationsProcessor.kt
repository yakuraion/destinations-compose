package pro.yakuraion.treecomposenavigation.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.core.DestinationScreen
import pro.yakuraion.treecomposenavigation.ksp.args.lambda.LambdaArgumentsFilter

class DestinationsProcessor(
    private val environment: SymbolProcessorEnvironment,
) : SymbolProcessor {

    private val screenDeclarationFactory = ScreenDeclarationFactory(
        argumentsFilters = listOf(
            LambdaArgumentsFilter(),
        )
    )

    private val navigationCreator = NavigationCreator()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver
            .getSymbolsWithAnnotation(DestinationScreen::class.java.name)
            .filterIsInstance(KSFunctionDeclaration::class.java)
            .forEach(::processDestination)

        return emptyList()
    }

    private fun processDestination(funcDeclaration: KSFunctionDeclaration) {
        val screen = screenDeclarationFactory.create(funcDeclaration)
        navigationCreator.create(environment.codeGenerator, screen)
    }
}
