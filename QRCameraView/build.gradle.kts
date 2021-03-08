import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion( Evence.compileSdkVersion )
    buildToolsVersion = Evence.buildToolsVersion

    defaultConfig {
        minSdkVersion( Evence.minSdkVersion )
        targetSdkVersion( Evence.targetSdkVersion )
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
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
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    coreLibraryDesugaring( "com.android.tools:desugar_jdk_libs:1.0.5" )

    implementation( project( Project.ical ) )
    implementation( project( Project.core ) )

    // Dagger
    implementation( Libs.daggerHilt )
    kapt( Libs.daggerHiltCompiler )

    // CardView
    implementation( Libs.cardView )

    implementation("com.google.guava:guava:30.1-android")

    // Layouts
    implementation( Libs.constraintLayout )
    implementation( Libs.coordinatorLayout )

    //material
    implementation( Libs.material )

    // LifeCycle
    implementation( Libs.viewModel )

    // RxJava
    implementation( Libs.rxJava )
    implementation( Libs.reactiveStreams )
    implementation( Libs.rxBinding )
    implementation( Libs.rxReplayingShare )

    //CameraX
    implementation( Libs.cameraXCamera2 )
    implementation( Libs.cameraXCameraView)
    implementation( Libs.cameraXLifecycle )
    implementation( Libs.cameraXCore )
    implementation( Libs.cameraXExtensions )

    //ML kit barcode
    implementation( Libs.mlkitBarcode )

    //ml kit text
    implementation( Libs.mlkitTextRecognition )

    // Timber
    implementation ( Libs.timber )
}