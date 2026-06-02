import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.vanniktechPublish)
}

group = "dev.jianastrero"
version = "0.1.0"

kotlin {
    jvm()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            // intentionally empty — annotations have no runtime deps
        }
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    coordinates("dev.jianastrero", "journey-kmp-annotations", "0.1.0")

    pom {
        name = "JourneyKMP Annotations"
        description = "Annotation definitions for JourneyKMP: @Journey, @Step, @Exit, @Piggyback."
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
