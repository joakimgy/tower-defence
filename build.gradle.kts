import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    application
}

group = "me.joakim"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("com.badlogicgames.gdx:gdx:1.9.10")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl:1.9.10")
    implementation("com.badlogicgames.gdx:gdx-platform:1.9.10:natives-desktop")
    implementation("io.github.libktx:ktx-app:1.9.10-b2")
    implementation("io.github.libktx:ktx-graphics:1.9.10-b2")
    implementation("io.github.libktx:ktx-inject:1.9.12-b1")
    implementation("io.github.libktx:ktx-ashley:1.9.12-b1")
    implementation("io.github.libktx:ktx-assets:1.9.12-b1")
    implementation("com.badlogicgames.ashley:ashley:1.7.3")
    implementation("com.badlogicgames.gdx:gdx-freetype:1.9.10")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:1.9.10:natives-desktop")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "MainKt"
}
