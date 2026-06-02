import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.vanniktechPublish)
}

group = "dev.jianastrero"
version = "0.1.0"

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "JourneyKMP"
            isStatic = true
        }
    }

    androidLibrary {
        namespace = "dev.jianastrero.journey"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }

        lint {
            abortOnError = true
            warningsAsErrors = true
            htmlReport = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":journey-kmp-annotations"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    coordinates("dev.jianastrero", "journey-kmp", "0.1.0")

    pom {
        name = "JourneyKMP"
        description = "Type-safe, annotation-driven navigation for Kotlin Multiplatform (Android & iOS), built on Navigation3."
        inceptionYear = "2025"
        url = "https://github.com/jianastrero/JourneyKMP"
        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                id = "jianastrero"
                name = "Jian Astrero"
                url = "https://github.com/jianastrero/"
            }
        }
        scm {
            url = "https://github.com/jianastrero/JourneyKMP"
            connection = "scm:git:git://github.com/jianastrero/JourneyKMP.git"
            developerConnection = "scm:git:ssh://git@github.com/jianastrero/JourneyKMP.git"
        }
    }
}
