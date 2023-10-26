plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
}

subprojects {
    group = "io.github.yakuraion.destinationscompose"
    version = "0.1.0-SNAPSHOT"
}
