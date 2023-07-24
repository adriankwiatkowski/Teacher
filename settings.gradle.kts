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
include(":core:mylibrary")
