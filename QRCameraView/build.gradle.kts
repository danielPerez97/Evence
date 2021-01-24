import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
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
    configurations {
        all {
            exclude(group = "com.google.guava", module= "listenablefuture")
        }
    }
}

dependencies {
    implementation( fileTree( mapOf( "dir" to "libs", "include" to "*.jar"  ) ) )
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    coreLibraryDesugaring( "com.android.tools:desugar_jdk_libs:1.0.5" )

    implementation( project( Project.ical ) )
    implementation( project( Project.core ) )

    // Dagger
    implementation( Libs.dagger )
    kapt( Libs.daggerCompiler )

    // CardView
    implementation( Libs.cardView )

    // ConstraintLayout
    implementation( Libs.constraintLayout )

    // LifeCycle
    implementation( Libs.lifeCycle )

    //CameraX
    implementation( Libs.cameraXCamera2 )
    implementation( Libs.cameraXCameraView)
    implementation( Libs.cameraXLifecycle )
    implementation( Libs.cameraXCore )

    //ML kit barcode
    implementation( Libs.mlkitBarcode)

    // Timber
    implementation ( Libs.timber )

    //guava
    //implementation( Libs.guava )

    //guava conflict - listenablefuture
    //implementation( Libs.guavaConflict )


}