pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "T-CARDLY"

include(":app")

// Core modules
include(":core:designsystem")
include(":core:database")
include(":core:common")
include(":core:ui")
include(":core:billing")
include(":core:api")
include(":core:security")

// Domain & Data layers
include(":domain")
include(":data")

// Feature modules
include(":feature:home")
include(":feature:scan")
include(":feature:contacts")
include(":feature:company")
include(":feature:profile")
include(":feature:subscription")
include(":feature:settings")
