pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        }
    }
}

rootProject.name = "DestinationsCompose"

includeBuild("gradle/plugins/publishing")

include(":core")
include(":ksp")
include(":ksp-core")
include(":ksp-viewmodel-koin")

include(":samples")
