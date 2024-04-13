plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "1.6.10-1.0.4"
}

group = "me.miste"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":NikaAnnotation"))
    ksp(project(":NikaProcesor"))
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}