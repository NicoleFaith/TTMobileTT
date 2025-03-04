plugins {
    // Plugin for Android application
    alias(libs.plugins.android.application)

    // Kotlin support for Android
    alias(libs.plugins.kotlin.android)

    // ✅ Enable Kotlin KAPT for annotation processing (needed for Glide)
    id("kotlin-kapt")
}

android {
    // Unique package identifier for the app
    namespace = "com.example.ttanader"

    // Compile with the latest SDK version
    compileSdk = 35

    defaultConfig {
        // Unique application ID
        applicationId = "com.example.ttanader"

        // Minimum Android version supported
        minSdk = 24

        // Target Android version
        targetSdk = 35

        // Versioning for the app
        versionCode = 1
        versionName = "1.0"

        // Test runner for instrumented tests
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Disable code shrinking & obfuscation in release builds
            isMinifyEnabled = false

            // ProGuard rules for release builds
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // ✅ Enable View Binding to simplify UI interaction
    viewBinding {
        enable = true
    }

    compileOptions {
        // Use Java 11 compatibility for improved performance & features
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        // Target JVM 11 for Kotlin
        jvmTarget = "11"
    }
}

dependencies {
    // ✅ Core AndroidX libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // ✅ Material Components for modern UI elements
    implementation("com.google.android.material:material:1.11.0")

    // ✅ RecyclerView - Needed for list-based UI elements (e.g., tasks, projects)
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ✅ CardView - Useful for a polished UI with cards
    implementation("androidx.cardview:cardview:1.0.0")

    // ✅ GIF Support using Glide (Recommended)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0") // ✅ Fixed KAPT issue

    // ✅ Alternative GIF support using Coil (Optional)
    implementation("io.coil-kt:coil-gif:2.4.0")

    // Unit testing dependencies
    testImplementation(libs.junit)

    //para sa gif, DO NOT TOUCH
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.29")



    // ✅ Android UI testing dependencies
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
