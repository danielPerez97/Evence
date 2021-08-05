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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {
    implementation( fileTree( mapOf( "dir" to "libs", "include" to "*.jar"  ) ) )
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))

    implementation( Libs.AndroidX.appCompat )

    coreLibraryDesugaring( Libs.desugar )

    implementation( project( Evence.ical ) )
    implementation( project( Evence.core ) )

    // Dagger
    implementation( Libs.Google.hiltAndroid )
    kapt( Libs.Google.hiltCompiler )

    // CardView
    implementation( Libs.AndroidX.cardView )

    implementation( Libs.guava )


    // KTX
    implementation( Libs.AndroidX.coreKtx )

    // Layouts
    implementation( Libs.AndroidX.constraintLayout )
    implementation( Libs.AndroidX.coordinatorLayout )

    //material
    implementation( Libs.AndroidX.material )

    // LifeCycle
    implementation( Libs.AndroidX.Lifecycle.viewModel )

    // RxJava
    implementation( Libs.ReactiveX.rxJava )
    implementation( Libs.AndroidX.reactiveStreams )
    implementation( Libs.JakeWharton.rxBinding )
    implementation( Libs.JakeWharton.rxReplayingShare )

    //CameraX
    implementation( Libs.AndroidX.Camera.cameraXCamera2 )
    implementation( Libs.AndroidX.Camera.cameraXCameraView)
    implementation( Libs.AndroidX.Camera.cameraXLifecycle )
    implementation( Libs.AndroidX.Camera.cameraXCore )
    implementation( Libs.AndroidX.Camera.cameraXExtensions )

    //ML kit barcode
    implementation( Libs.Google.mlkitBarcode )

    //ml kit text
    implementation( Libs.Google.mlkitTextRecognition )

    // Timber
    implementation ( Libs.JakeWharton.timber )

    // Testing
    androidTestImplementation( Libs.JUnit.junit4 )
    androidTestImplementation( Libs.Google.hiltAndroidTesting )
    kaptAndroidTest( Libs.Google.hiltCompiler )
    androidTestImplementation( Libs.AndroidX.Test.runner )
    androidTestImplementation( Libs.AndroidX.Test.espressoCore )
    androidTestImplementation( Libs.AndroidX.Test.rules )
    androidTestImplementation( Libs.AndroidX.Test.Ext.junit )
}