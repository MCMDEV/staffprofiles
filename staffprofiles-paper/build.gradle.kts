plugins {
    `java-library`
    id("com.gradleup.shadow") version "8.3.8"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")

    implementation(project(":staffprofiles-common"))
}

tasks {
    compileJava {
        options.release = 21
    }
    jar {
        enabled = false
    }
    shadowJar {
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
    processResources {
        expand("version" to project.version)
    }
    runServer {
        minecraftVersion("1.21.8")
    }
}