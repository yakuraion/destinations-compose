What is destinations-compose?
=============================

Destinations-compose is a library to generate [navigation-compose](https://developer.android.com/jetpack/compose/navigation) code using [KSP](https://kotlinlang.org/docs/ksp-overview.html). The main feature is supporting arguments in easy way.

## Get started

## Generated methods

For every destination this library will generate the following functions:

1. To apply composable to the navigation graph:
```kotlin
fun NavGraphBuilder.{screenName}Composable(...)
```

2. To navigate to the destination:
```kotlin
fun NavController.navigateTo{ScreenName}(...)
```

3. To get a string to use as `startDestination` parameter in NavHost (will be generated if all of the parameters have default values):
```kotlin
fun get{ScreenName}StartDestination(): String
```

4. Might be useful in methods like `navController.popBackStack(destinationId = ...)` if you decide to keep using predefined route:
```kotlin
fun get(ScreenName}DefaultRoute(): String
```

## How to use

1. Create your destination composable.

   ```kotlin
   @Composable
   fun ChildScreen(
       arg1: Int,
       arg2: String?,
       onBackRequest: () -> Unit, 
   ) {
       // code
   }
   ```
2. Mark your composable with the `@DestinationScreen` annotation.

   ```kotlin
   @DestinationScreen
   @Composable
   fun ChildScreen(
       arg1: Int,
       arg2: String?,
       onBackRequest: () -> Unit, 
   ) {
       // code
   }
   ```
3. Build the project - the library will generate the next functions:
   ```kotlin
   fun NavGraphBuilder.childScreenComposable(onBackRequest: () -> Unit)
   ```
   and
   ```kotlin
   fun NavController.navigateToChildScreen(
       arg1: Int,
       arg2: String?,
   )
   ```
   If all arguments are callable (lambdas) or all of them have default values (see "Setting default values to arguments") it will also generate function:
   ```kotlin
   fun getChildScreenStartDestination(): String
   ```
4. Add the screen to a navigation graph

   ```kotlin
   @Composable
   fun ParentScreen() {
       val navController = rememberNavController()
       NavHost(
           navController = navController,
           startDestination = getChildScreenStartDestination(),  // use generated get{ScreenName}StartDestination function
       ) {
           childScreenComposable(  // use generated {screenName}Composable function
               onBackRequest = {}
           )
       }

       LaunchedEffect(Unit) {
           navController.navigateToChildScreen(  // use generated navigateTo{screenName} function
               arg1 = 1,
               arg2 = "2",
           )
       }
   }
   ```

## Setting default values to arguments

It is possible to make some arguments optional. To do this, mark parameters with `@DestinationOptionalParameter` annotation.

```kotlin
@DestinationScreen
@Composable
fun ChildScreen(
    @DestinationOptionalParameter(defaultValue = "1f") arg1: Float,
    arg2: Boolean,
    @DestinationOptionalParameter(defaultValue = "\"hello\"") arg3: String?,
    @DestinationOptionalParameter(defaultValue = "null") arg4: Int?,
    onBackRequest: () -> Unit,
) {
    // code
}
```

**Be careful!**
You should pass literal to `defaultValue` parameter. It means if you want to pass String value, you have to wrap your text with `\"` symbols (as in the example above).

## Supported argument types

|                    |                                              Passing via {screenName}Composable                                              |                                              Passing via navigateTo{ScreenName}                                              |                                                                     Support default values                                                                     |
|--------------------| :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------------------------------------------------------: |
| Callable (lambdas) | <img src="https://raw.githubusercontent.com/yakuraion/destinations-compose/main/attachments/images/correct.png" width="24" /> |  <img src="https://raw.githubusercontent.com/yakuraion/destinations-compose/main/attachments/images/close.png" width="24" />  |                   <img src="https://raw.githubusercontent.com/yakuraion/destinations-compose/main/attachments/images/close.png" width="24" />                   |
| Primitives         |  <img src="https://raw.githubusercontent.com/yakuraion/destinations-compose/main/attachments/images/close.png" width="24" />  | <img src="https://raw.githubusercontent.com/yakuraion/destinations-compose/main/attachments/images/correct.png" width="24" /> |                  <img src="https://raw.githubusercontent.com/yakuraion/destinations-compose/main/attachments/images/correct.png" width="24" />                  |
| Parcelable         |  <img src="https://raw.githubusercontent.com/yakuraion/destinations-compose/main/attachments/images/close.png" width="24" />  | <img src="https://raw.githubusercontent.com/yakuraion/destinations-compose/main/attachments/images/correct.png" width="24" /> |                   <img src="https://raw.githubusercontent.com/yakuraion/destinations-compose/main/attachments/images/close.png" width="24" />                   |
| Serializable       |  <img src="https://raw.githubusercontent.com/yakuraion/destinations-compose/main/attachments/images/close.png" width="24" />  | <img src="https://raw.githubusercontent.com/yakuraion/destinations-compose/main/attachments/images/correct.png" width="24" /> |                   <img src="https://raw.githubusercontent.com/yakuraion/destinations-compose/main/attachments/images/close.png" width="24" />                   |


## ViewModel-Koin

**destinations-compose** supports ViewModel as argument type if it is provided by Koin. Example:

```kotlin
@DestinationScreen
@Composable
fun ChildScreen(
    onBackRequest: () -> Unit,
    viewModel: ChildViewModel
) {
    // code
}

class ChildViewModel(
    arg1: Int,
    arg2: String?,
) : ViewModel() {
    // code
}
```

The library will generate the methods:
```kotlin
fun NavGraphBuilder.childScreenComposable(onBackRequest: () -> Unit)
```
and
```kotlin
fun NavController.navigateToChildScreen(
    arg1: Int,
    arg2: String?,
)
```

### Restrict passing arguments via navigation in ViewModel

If you have ViewModel's parameter that is Primitive, Serializable or Parcelable and you don't want to pass it via navigation, 
you can mark the parameter with the `@NotDestinationParameter` annotation. 
In this case you will have to pass it via DI.

```kotlin
class ChildViewModel(
    arg1: Int,
    @NotDestinationParameter arg2: String?,
) : ViewModel() {
    // code
}
```

will generate

```kotlin
fun NavController.navigateToChildScreen(
    arg1: Int,
)
```

If you have other parameters in ViewModel (usecases, interactors, etc.) that are not Primitives, Parcelables or Serializables, 
they will not be added to the navigation functions and should be provided by DI.
