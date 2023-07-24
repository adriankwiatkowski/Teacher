buildscript {
    extra.apply {
        set("android_core_ktx_version", "1.10.1")
        set("activity_compose_version", "1.7.2")
        set("compose_ui_version", "1.4.2")
        set("lifecycle_version", "2.6.0")
        set("nav_version", "2.6.0")
        set("coroutines_version", "1.6.4")
        set("sqldelight_version", "1.5.4")
        set("hilt_version", "2.46.1")
        set("hilt_workmanager_version", "1.0.0")
        set("hilt_nav_compose_version", "1.0.0")
        set("desugar_jdk_libs", "2.0.3")
        set("datastore_preferences_version", "1.0.0")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.sqldelight) apply false
}
true // Needed to make the Suppress annotation work for the plugins block