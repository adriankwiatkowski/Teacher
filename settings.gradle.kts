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
rootProject.name = "TeacherApp"
include(":app")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:model")
include(":core:datastore")
include(":core:ui")
include(":feature:settings")
include(":feature:schoolyear")
include(":feature:schoolclass")
include(":feature:student")
include(":feature:lesson")
include(":feature:studentnote")
include(":feature:grade")
