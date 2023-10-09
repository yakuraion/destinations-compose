package pro.yakuraion.treecomposenavigation.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.core.DestinationScreen
import pro.yakuraion.treecomposenavigation.ksp.parameters.instant.LambdaParametersExtractor
import pro.yakuraion.treecomposenavigation.ksp.parameters.argument.PrimitiveParametersExtractor
import pro.yakuraion.treecomposenavigation.ksp.screendeclaration.ScreenDeclarationFactory
import pro.yakuraion.treecomposenavigation.ksp.specs.NavigationComposableFunFunSpecCreator
import pro.yakuraion.treecomposenavigation.ksp.specs.NavigationNavigateToFunSpecCreator

class DestinationsProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val screenDeclarationFactory = ScreenDeclarationFactory(
        screenParametersExtractors = listOf(
            LambdaParametersExtractor(),
            PrimitiveParametersExtractor(),
        )
    )

    private val navigationCreator = NavigationCreator(
        codeGenerator = environment.codeGenerator,
        funSpecCreators = listOf(
            NavigationComposableFunFunSpecCreator(),
            NavigationNavigateToFunSpecCreator(),
        )
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver
            .getSymbolsWithAnnotation(DestinationScreen::class.java.name)
            .filterIsInstance(KSFunctionDeclaration::class.java)
            .forEach(::processDestination)

        return emptyList()
    }

    private fun processDestination(funcDeclaration: KSFunctionDeclaration) {
        val screen = screenDeclarationFactory.create(funcDeclaration)
        navigationCreator.create(screen)
    }
}
