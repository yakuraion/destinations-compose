plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "io.github.yakuraion.destinationscompose"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.yakuraion.destinationscompose"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
}

dependencies {
    implementation(libs.destinations.compose.core)
    ksp(libs.destinations.compose.ksp)
    ksp(libs.destinations.compose.ksp.viewmodel.koin)

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)

    implementation(libs.navigation.compose)

    implementation(libs.viewmodel.compose)
    
    implementation(libs.koin.compose)
}
