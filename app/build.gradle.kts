plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.music_player_task"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.music_player_task"
        minSdk = 24
        targetSdk = 34
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //Room
//    val roomVersion = "2.6.1"
//    implementation("androidx.room:room-runtime:$roomVersion")
//    annotationProcessor("androidx.room:room-compiler:$roomVersion")
//    kapt("androidx.room:room-compiler:$roomVersion")

    //navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Arrow
    implementation(libs.arrow.core)
    implementation(libs.arrow.fx.coroutines)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //Coil
    implementation(libs.coil.compose)
    //dagger hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
//    implementation("androidx.hilt:hilt-work:1.1.0")
//    kapt("androidx.hilt:hilt-compiler:1.1.0")
//    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation(libs.androidx.hilt.navigation.compose)

    //Material extended icon
    implementation(libs.androidx.material.icons.extended.android)
    //Palette
    implementation(libs.androidx.palette)
    implementation(libs.accompanist.systemuicontroller)

    //Wavy Slider
    implementation(libs.wavy.slider)

}