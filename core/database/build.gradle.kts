@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.teacherapp.core.database"
    compileSdk = 33

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

sqldelight {
    databases {
        create("TeacherDatabase") { // This will be the name of the generated database class.
            packageName.set("com.example.teacherapp.core.database.generated")
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.sqldelight.android)
    implementation(libs.sqldelight.coroutines)

    implementation(libs.bundles.androidx)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}