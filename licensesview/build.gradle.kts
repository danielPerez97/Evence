plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
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
        kotlinCompilerExtensionVersion = Compose.version
        kotlinCompilerVersion = "1.5.10"
    }
}

dependencies {
    implementation( project( Project.core ) )

    implementation( Libs.AndroidX.coreKtx )
    implementation( Libs.AndroidX.appCompat )
    implementation( Libs.AndroidX.Lifecycle.lifecycleRuntimeKtx )
    implementation( Libs.AndroidX.Lifecycle.viewModelCompose )
    implementation( Libs.Google.materialAndroid )
    implementation( Compose.ui )
    implementation( Compose.material )
    implementation( Compose.uiTooling )
    implementation( Compose.activityCompose )
    implementation( Compose.constraintLayoutCompose )
    implementation( Coroutines.coroutinesCore )
    implementation( Coroutines.coroutinesAndroid )
    implementation( Libs.Google.hiltAndroid )
    kapt( Libs.Google.hiltCompiler )

    implementation( Libs.SquareUp.okio )
    implementation( Libs.SquareUp.moshi )
    kapt( Libs.SquareUp.moshiCodegen )
}