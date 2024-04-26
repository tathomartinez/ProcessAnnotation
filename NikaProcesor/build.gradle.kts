plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "1.8.22-1.0.11"
}

group = "me.miste"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(kotlin("test"))

    implementation(project(":NikaAnnotation"))

    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.22-1.0.11")
    implementation("com.squareup:kotlinpoet:1.11.0")
    implementation("com.squareup:kotlinpoet-ksp:1.11.0")
    implementation("com.google.auto.service:auto-service:1.0-rc7")
    ksp("dev.zacsweers.autoservice:auto-service-ksp:1.0.0")
}

ksp {
    arg("autoServiceKsp.verfiy", "true")
    arg("autoServiceKsp.verbose", "true")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += listOf("-Xopt-in=squareup.kotlinpoet.ksp.KotlinPoetKspPreview")
}