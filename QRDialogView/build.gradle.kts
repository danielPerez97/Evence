import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation( fileTree( mapOf( "dir" to "libs", "include" to "*.jar"  ) ) )
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))

    implementation( Libs.AndroidX.appCompat )

    implementation( project( Project.core ) )

    implementation( Libs.AndroidX.cardView )
    implementation( Libs.coil )
    implementation( Libs.AndroidX.coordinatorLayout )
    implementation( Libs.Google.hiltAndroid )
    kapt( Libs.Google.hiltCompiler )
    coreLibraryDesugaring( Libs.desugar )
    implementation( Libs.ReactiveX.rxAndroid )
    implementation( Libs.JakeWharton.rxBinding )
    implementation( Libs.JakeWharton.timber )
}