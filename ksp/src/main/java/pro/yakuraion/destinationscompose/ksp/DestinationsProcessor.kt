package pro.yakuraion.destinationscompose.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import pro.yakuraion.destinationscompose.core.DestinationScreen
import pro.yakuraion.destinationscompose.ksp.screendeclaration.ScreenDeclarationFactory
import pro.yakuraion.destinationscompose.ksp.specs.ComposableFunCreator
import pro.yakuraion.destinationscompose.ksp.specs.NavigateFunCreator
import pro.yakuraion.destinationscompose.kspcore.parameters.ParameterConverter
import pro.yakuraion.destinationscompose.kspcore.parameters.lambda.LambdaParameterConverter
import pro.yakuraion.destinationscompose.kspcore.parameters.parcelable.ParcelableParameterConverter
import pro.yakuraion.destinationscompose.kspcore.parameters.primitive.PrimitiveParameterConverter
import pro.yakuraion.destinationscompose.kspcore.parameters.serializable.SerializableParameterConverter

class DestinationsProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val viewModelKoinConverterClass: Class<*>? = try {
        Class.forName(VIEW_MODEL_KOIN_CONVERTER_CLASS)
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

    companion object {
        private const val VIEW_MODEL_KOIN_CONVERTER_CLASS =
            "pro.yakuraion.destinationscompose.kspviewmodelkoin.parameters.ViewModelParameterConverter"
    }
}
