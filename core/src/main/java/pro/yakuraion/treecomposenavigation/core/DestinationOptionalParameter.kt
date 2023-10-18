package pro.yakuraion.treecomposenavigation.core

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class DestinationOptionalParameter(val defaultValue: String = "")
