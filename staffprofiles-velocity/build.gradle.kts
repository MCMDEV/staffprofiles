plugins {
    `java-library`
    id("com.gradleup.shadow") version "8.3.8"
    id("net.kyori.blossom") version "2.1.0"

    id("xyz.jpenilla.run-velocity") version "2.3.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")

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
    runVelocity {
        velocityVersion("3.4.0-SNAPSHOT")
    }
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", project.version.toString())
            }
        }
    }
}