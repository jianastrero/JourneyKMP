import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm")
    alias(libs.plugins.vanniktechPublish)
}

group = "dev.jianastrero"
version = "0.1.0"

dependencies {
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
    implementation(project(":journey-kmp-annotations"))
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    coordinates("dev.jianastrero", "journey-kmp-ksp", "0.1.0")

    pom {
        name = "JourneyKMP KSP Processor"
        description = "KSP code generator for JourneyKMP — produces typed controllers, sealed view classes, and Compose host composables."
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
