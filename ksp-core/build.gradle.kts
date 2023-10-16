plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    api(project(":core"))

    api("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.13")
    api("com.squareup:kotlinpoet-ksp:1.14.2")
}
