plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "2.1.21-2.0.1"
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.madcamp1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.madcamp1"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true  // 필요하면 유지
    }

    // KSP가 생성하는 코드 경로 등록 필수!
    sourceSets.configureEach {
        java.srcDir("build/generated/ksp/${name}/kotlin")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("com.google.code.gson:gson:2.10.1")


    implementation("com.google.dagger:dagger-compiler:2.51.1")
    implementation(libs.androidx.media3.common.ktx)
    ksp("com.google.dagger:dagger-compiler:2.51.1")
    implementation("io.coil-kt:coil:2.4.0")
    implementation("com.github.chrisbanes:PhotoView:2.3.0")
    implementation("com.github.kizitonwose:CalendarView:2.4.1")
    implementation(libs.places)
    
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}