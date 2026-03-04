import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import team.bue.bugle.buildsrc.ProjectProperties
import team.bue.bugle.buildsrc.Versions
import java.util.Properties



private val defaultBugleBaseUrl = "https://api.bugle.site"

private val bugleBaseUrl: String by lazy {
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")

    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use(localProperties::load)
    }

    localProperties
        .getProperty("BUGLE_BASE_URL", defaultBugleBaseUrl)
        .trim()
        .removeSurrounding("\"")
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.firebaseCrashlytics)
    alias(libs.plugins.firebasePerformance)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktlint)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.crashlytics)
            implementation(libs.firebase.performance)
            implementation(libs.firebase.messaging)
            implementation(libs.androidx.material)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.navigation3.ui)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.core)

            implementation(projects.core.designSystem)
            implementation(projects.core.data)
            implementation(projects.feature.splash)
            implementation(projects.feature.onboarding)
            implementation(projects.feature.emailLogin)
            implementation(projects.feature.signUp)
            implementation(projects.feature.findPassword)
            implementation(projects.feature.findId)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "team.bue.bugle"
    compileSdk = ProjectProperties.COMPILE_SDK

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "team.bue.bugle"
        minSdk = ProjectProperties.MIN_SDK
        targetSdk = ProjectProperties.TARGET_SDK
        versionCode = ProjectProperties.VERSION_CODE
        versionName = ProjectProperties.VERSION_NAME
        buildConfigField("String", "BUGLE_BASE_URL", "\"$bugleBaseUrl\"")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
