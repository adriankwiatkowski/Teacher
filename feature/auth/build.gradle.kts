plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.teacher.feature.auth"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "com.example.teacher.core.testing.TeacherTestRunner"
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
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
        unitTests.all {
            it.jvmArgs("-noverify")
        }
    }
}

dependencies {
    implementation(projects.core.ui)
    testImplementation(projects.core.testing)
    androidTestImplementation(projects.core.testing)
    testImplementation(projects.core.database)

    // Hilt
    implementation(libs.hilt.android)
    testImplementation(libs.hilt.android.testing)
    kapt(libs.hilt.compiler)
    kaptTest(libs.hilt.compiler)
    kapt(libs.hiltx.compiler)
    kaptTest(libs.hiltx.compiler)
    // Hilt Navigation Compose
    implementation(libs.hilt.nav.compose)

    coreLibraryDesugaring(libs.desugar.jdk)

    implementation(libs.bundles.androidx)

    testImplementation(libs.bundles.test.compose)
    debugImplementation(libs.bundles.debug.compose)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}