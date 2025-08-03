rootProject.name = "staffprofiles"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include("staffprofiles-common")
include("staffprofiles-paper")
include("staffprofiles-velocity")