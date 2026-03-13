import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import team.bue.bugle.buildsrc.ProjectProperties
import team.bue.bugle.buildsrc.Versions
import java.util.Properties

fun getLocalProperty(key: String): String {
    val properties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    require(localPropertiesFile.exists()) { "local.properties not found." }
    localPropertiesFile.inputStream().use(properties::load)
    return requireNotNull(properties.getProperty(key)) { "$key not found in local.properties" }
        .trim().removeSurrounding("\"")
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktlint)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
    }

    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "network"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            api(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            api(libs.kermit)

            implementation(projects.core.model)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "team.bue.bugle.core.network"
    compileSdk = ProjectProperties.COMPILE_SDK
    defaultConfig {
        minSdk = ProjectProperties.MIN_SDK
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        release {
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"${getLocalProperty("PROD_BASE_URL")}\"",
            )
            buildConfigField(
                type = "String",
                name = "WEB_VIEW_URL",
                value = "\"${getLocalProperty("WEB_VIEW_URL")}\"",
            )
        }
        debug {
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"${getLocalProperty("DEV_BASE_URL")}\"",
            )
            buildConfigField(
                type = "String",
                name = "WEB_VIEW_URL",
                value = "\"${getLocalProperty("WEB_VIEW_URL")}\"",
            )
        }
    }
    compileOptions {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
    }
}
