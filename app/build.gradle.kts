plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)

}

android {
    namespace = "com.example.pickcourt"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pickcourt"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        renderscriptTargetApi = 21
        renderscriptSupportModeEnabled  = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}



dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.play.services.maps); // Google Maps
    implementation (libs.glide) // Glide
    implementation (libs.card.form) // Credit Card Form
    implementation (libs.material.v180) // Material Design 3
    implementation(libs.play.services.auth) // Google

    // Firebase:
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
}