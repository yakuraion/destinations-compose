package pro.yakuraion.treecomposenavigation.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.core.DestinationScreen
import pro.yakuraion.treecomposenavigation.ksp.screendeclaration.ScreenDeclarationFactory
import pro.yakuraion.treecomposenavigation.ksp.specs.ComposableFunCreator
import pro.yakuraion.treecomposenavigation.ksp.specs.GetStartDestinationFunCreator
import pro.yakuraion.treecomposenavigation.ksp.specs.NavigateToFunCreator
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ScreenParametersExtractor
import pro.yakuraion.treecomposenavigation.kspcore.parameters.primitive.PrimitiveScreenParametersExtractor
import pro.yakuraion.treecomposenavigation.kspcore.parameters.lambda.LambdaScreenParametersExtractor

class DestinationsProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val viewModelKoinExtractorClass: Class<*>? = try {
        Class.forName("pro.yakuraion.treecomposenavigation.kspviewmodelkoin.parameters.ViewModelScreenParametersExtractor")
    } catch (e: ClassNotFoundException) {
        null
    }

    private val screenDeclarationFactory = ScreenDeclarationFactory(
        parametersExtractors = listOfNotNull(
            LambdaScreenParametersExtractor(),
            PrimitiveScreenParametersExtractor(),
            viewModelKoinExtractorClass?.getConstructor()?.newInstance() as ScreenParametersExtractor<*>?,
        )
    )

    private val navigationCreator = NavigationCreator(
        codeGenerator = environment.codeGenerator,
        funCreators = listOf(
            ComposableFunCreator(),
            NavigateToFunCreator(),
            GetStartDestinationFunCreator(),
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
