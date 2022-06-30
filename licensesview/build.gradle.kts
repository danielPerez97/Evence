plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("de.mannodermaus.android-junit5")
}

android {
    compileSdk = Evence.minSdkVersion
    buildToolsVersion = Evence.buildToolsVersion

    defaultConfig {
        minSdk = Evence.minSdkVersion
        targetSdk = Evence.targetSdkVersion

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
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Libs.AndroidX.Compose.version
        kotlinCompilerVersion = "1.5.10"
    }
}

dependencies {
    implementation( project( Evence.core ) )

    implementation( Libs.AndroidX.coreKtx )
    implementation( Libs.AndroidX.appCompat )
    implementation( Libs.AndroidX.Lifecycle.lifecycleRuntimeKtx )
    implementation( Libs.AndroidX.Lifecycle.viewModelCompose )
    implementation( Libs.Google.materialAndroid )
    implementation( Libs.AndroidX.Compose.ui )
    implementation( Libs.AndroidX.Compose.material )
    implementation( Libs.AndroidX.Compose.uiTooling )
    implementation( Libs.AndroidX.Compose.activityCompose )
    implementation( Libs.AndroidX.Compose.constraintLayoutCompose )
    implementation( Coroutines.coroutinesCore )
    implementation( Coroutines.coroutinesAndroid )
    implementation( Libs.Google.hiltAndroid )
    implementation("androidx.test:monitor:1.4.0")
    implementation("androidx.test.ext:junit-ktx:1.1.3")
    kapt( Libs.Google.hiltCompiler )

    implementation( Libs.SquareUp.okio )
    implementation( Libs.SquareUp.moshi )
    kapt( Libs.SquareUp.moshiCodegen )

    // Testing
    testImplementation(TestLibs.junit5JupiterApi)
    testRuntimeOnly(TestLibs.junit5Runtime)
}