plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.ml"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ml"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    //image classification
    implementation("com.google.mlkit:image-labeling:17.0.8")
    //custom image classification
    implementation("com.google.mlkit:image-labeling-custom:17.0.2")
    //object Detection
    implementation("com.google.mlkit:object-detection:17.0.1")
    //face detection
    implementation("com.google.mlkit:face-detection:16.1.6")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}