import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Evence.compileSdkVersion
    buildToolsVersion = Evence.buildToolsVersion

    defaultConfig {
        minSdk = Evence.minSdkVersion
        targetSdk = Evence.targetSdkVersion
//        versionCode = 1
//        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
//        coreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation( fileTree( mapOf( "dir" to "libs", "include" to "*.jar"  ) ) )
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))

    coreLibraryDesugaring( Libs.desugar )

    implementation( project( Project.core ) )

    implementation( Libs.coil )
    implementation( Libs.AndroidX.appCompat )
    implementation( Libs.AndroidX.cardView )
    implementation( Libs.AndroidX.coordinatorLayout )
    implementation( Libs.AndroidX.coreKtx )
    implementation( Libs.AndroidX.recyclerView )
    implementation( Libs.Google.hiltAndroid )
    kapt( Libs.Google.hiltCompiler )
    implementation( Libs.ReactiveX.rxJava )
}