package pro.yakuraion.treecomposenavigation.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.treecomposenavigation.core.DestinationScreen
import pro.yakuraion.treecomposenavigation.ksp.screendeclaration.ScreenDeclarationFactory
import pro.yakuraion.treecomposenavigation.ksp.specs.ComposableFunCreator
import pro.yakuraion.treecomposenavigation.ksp.specs.NavigateFunCreator
import pro.yakuraion.treecomposenavigation.kspcore.parameters.ParameterConverter
import pro.yakuraion.treecomposenavigation.kspcore.parameters.lambda.LambdaParameterConverter
import pro.yakuraion.treecomposenavigation.kspcore.parameters.parcelable.ParcelableParameterConverter
import pro.yakuraion.treecomposenavigation.kspcore.parameters.primitive.PrimitiveParameterConverter
import pro.yakuraion.treecomposenavigation.kspcore.parameters.serializable.SerializableParameterConverter

class DestinationsProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val viewModelKoinConverterClass: Class<*>? = try {
        Class.forName("pro.yakuraion.treecomposenavigation.kspviewmodelkoin.parameters.ViewModelParameterConverter")
    } catch (e: ClassNotFoundException) {
        null
    }

    private val screenDeclarationFactory = ScreenDeclarationFactory(
        parametersConverters = listOfNotNull(
            LambdaParameterConverter(),
            PrimitiveParameterConverter(),
            ParcelableParameterConverter(),
            SerializableParameterConverter(),
            viewModelKoinConverterClass?.getConstructor()?.newInstance() as ParameterConverter<*>?,
        )
    )

    private val navigationCreator = NavigationCreator(
        codeGenerator = environment.codeGenerator,
        funCreators = listOf(
            ComposableFunCreator(),
            NavigateFunCreator(NavigateFunCreator.Type.NAVIGATE_TO),
            NavigateFunCreator(NavigateFunCreator.Type.GET_START_DESTINATION),
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
