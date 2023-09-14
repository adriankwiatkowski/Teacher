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
rootProject.name = "Teacher"
include(":app")
include(":core:model")
include(":core:common")
include(":core:domain")
include(":core:data")
include(":core:datastore")
include(":core:database")
include(":core:auth")
include(":core:ui")
include(":feature:schedule")
include(":feature:schoolclass")
include(":feature:schoolyear")
include(":feature:student")
include(":feature:studentnote")
include(":feature:lesson")
include(":feature:grade")
include(":feature:note")
include(":feature:auth")
include(":feature:settings")
