plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.mygamingdatabase"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mygamingdatabase"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.compose.ui:ui:1.5.3")
    implementation ("androidx.compose.material3:material3:1.3.1")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.5.3")
    implementation ("androidx.navigation:navigation-compose:2.7.3")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation ("androidx.activity:activity-compose:1.7.2")
    implementation ("androidx.compose.runtime:runtime-livedata:1.5.3")
    implementation ("androidx.core:core-splashscreen:1.0.1")
    implementation ("androidx.compose.material:material-icons-core:1.x.x")
    implementation ("androidx.compose.material:material-icons-extended:1.x.x")

    // Notifications
    implementation ("androidx.core:core-ktx:1.10.1")

    // Coil dependency (to request images through URLs)
    implementation ("io.coil-kt:coil-compose:2.1.0")

    // Accompanist WebView dependency
    implementation ("com.google.accompanist:accompanist-webview:0.28.0")

    // Jetpack DataStore
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    // Animations
    implementation ("androidx.compose.animation:animation:1.5.4")

    // Serialization
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Retrofit and GSON Serialization
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // Test
    implementation (libs.androidx.monitor)
    implementation (libs.androidx.junit.ktx)
    implementation (libs.androidx.media3.common.ktx)
    implementation(libs.firebase.database.ktx)
    androidTestImplementation (libs.junit.junit)
    debugImplementation ("androidx.compose.ui:ui-tooling:1.5.4")
}