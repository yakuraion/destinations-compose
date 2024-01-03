package io.github.yakuraion.destinationscompose.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import io.github.yakuraion.destinationscompose.core.DestinationScreen
import io.github.yakuraion.destinationscompose.ksp.screendeclaration.ScreenDeclarationFactory
import io.github.yakuraion.destinationscompose.ksp.specs.ComposableFunCreator
import io.github.yakuraion.destinationscompose.ksp.specs.GetRouteSchemeFunCreator
import io.github.yakuraion.destinationscompose.ksp.specs.NavigateFunCreator
import io.github.yakuraion.destinationscompose.kspcore.parameters.ParameterConverter
import io.github.yakuraion.destinationscompose.kspcore.parameters.lambda.LambdaParameterConverter
import io.github.yakuraion.destinationscompose.kspcore.parameters.parcelable.ParcelableParameterConverter
import io.github.yakuraion.destinationscompose.kspcore.parameters.primitive.PrimitiveParameterConverter
import io.github.yakuraion.destinationscompose.kspcore.parameters.serializable.SerializableParameterConverter

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
            GetRouteSchemeFunCreator(),
            ComposableFunCreator(),
            NavigateFunCreator(),
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
            "io.github.yakuraion.destinationscompose.kspviewmodelkoin.parameters.ViewModelParameterConverter"
    }
}
