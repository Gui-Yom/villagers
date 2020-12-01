plugins {
    kotlin("jvm") version "1.4.20"
    kotlin("plugin.serialization") version "1.4.20"
    application
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("java-library")
        plugin("org.jetbrains.kotlin.plugin.serialization")
    }
}

allprojects {
    group = "marais.villagers"
    version = "0.1.0"

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }

    java {
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks {
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_11.toString()
                //javaParameters = true
                //freeCompilerArgs = listOf("-Xemit-jvm-type-annotations")
            }
        }

        withType(JavaCompile::class).configureEach {
            options.encoding = "UTF-8"
        }
    }
}

dependencies {
    val kotlinVersion: String by project
    implementation(platform(kotlin("bom", kotlinVersion)))
    implementation(kotlin("stdlib-jdk8"))

    val coroutinesVersion: String by project
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:$coroutinesVersion"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

    val log4jVersion: String by project
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")

    implementation(project(":game"))
}

application {
    // Define the main class for the application.
    mainClass.set("marais.villagers.MainKt")
    mainClassName = mainClass.get()
}

tasks {
    shadowJar {
        mergeServiceFiles()
        //minimize()
    }
}
