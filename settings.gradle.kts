pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("com.google.devtools.ksp") version "1.8.22-1.0.11"
        kotlin("jvm") version "1.8.22"
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