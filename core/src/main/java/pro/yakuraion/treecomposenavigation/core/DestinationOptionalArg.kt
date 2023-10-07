package pro.yakuraion.treecomposenavigation.core

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class DestinationOptionalArg(val defaultValue: String = "")
