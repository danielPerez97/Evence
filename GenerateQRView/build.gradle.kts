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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.version
    }

}

dependencies {
    implementation( fileTree( mapOf( "dir" to "libs", "include" to "*.jar"  ) ) )
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))

    implementation( project( Project.core ) )
    implementation( project( Project.ical ) )

    // Compose
    implementation( Compose.foundation )
    implementation( Compose.ui )
    implementation( Compose.uiTooling )
    implementation( Compose.material )
    implementation( Compose.activityCompose )
    implementation( Compose.constraintLayoutCompose )

    // Dagger
    implementation( Libs.Google.hiltAndroid )
    kapt( Libs.Google.hiltCompiler )

    // Desugar
    coreLibraryDesugaring( Libs.desugar )

    // Material
    implementation( Libs.AndroidX.material )

    // Okio
    implementation( Libs.SquareUp.okio )

    // RxJava
    implementation( Libs.ReactiveX.rxAndroid )
    implementation( Libs.JakeWharton.rxBinding )
    implementation( Libs.ReactiveX.rxJava )

    // Timber
    implementation( Libs.JakeWharton.timber )

    // ViewModel
    implementation( Libs.AndroidX.Lifecycle.viewModel )

}