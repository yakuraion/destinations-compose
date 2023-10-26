plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("publishing-conventions")
}

description = "Core component for Destinations-compose"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
