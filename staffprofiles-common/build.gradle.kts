plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    // Gson is already included in the Vanilla server and thus in all platforms,
    // making it the obvious choice for config file serialization.
    compileOnly("com.google.code.gson:gson:2.7")

    // LuckPerms is used by the LuckPermsPermissionProvider
    compileOnly("net.luckperms:api:5.4")

    // Provides the @Blocking annotation
    compileOnly("org.jetbrains:annotations:26.0.2")
}

tasks {
    compileJava {
        options.release = 21
    }
}