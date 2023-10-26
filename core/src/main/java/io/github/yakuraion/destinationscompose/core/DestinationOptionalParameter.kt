package io.github.yakuraion.destinationscompose.core

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class DestinationOptionalParameter(val defaultValue: String)
