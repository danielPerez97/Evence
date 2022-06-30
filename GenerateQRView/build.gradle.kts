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
        kotlinCompilerExtensionVersion = Libs.AndroidX.Compose.version
    }

}

dependencies {
    implementation( fileTree( mapOf( "dir" to "libs", "include" to "*.jar"  ) ) )
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))

    implementation( project( Evence.core ) )
    implementation( project( Evence.ical ) )

    // Compose
    implementation( Libs.AndroidX.Compose.foundation )
    implementation( Libs.AndroidX.Compose.ui )
    implementation( Libs.AndroidX.Compose.uiTooling )
    implementation( Libs.AndroidX.Compose.material )
    implementation( Libs.AndroidX.Compose.activityCompose )
    implementation( Libs.AndroidX.Compose.constraintLayoutCompose )

    // Dagger
    implementation( Libs.Google.hiltAndroid )
    implementation("androidx.test.ext:junit-ktx:1.1.3")
    implementation("androidx.test:monitor:1.4.0")
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