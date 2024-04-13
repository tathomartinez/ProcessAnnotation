pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("com.google.devtools.ksp") version "1.6.10-1.0.4"
        kotlin("jvm") version "1.6.10"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "Kiron"

include(":NikaAnnotation")
include(":NikaProcesor")
include(":Cliente")