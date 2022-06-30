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
        isCoreLibraryDesugaringEnabled = true
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
        kotlinCompilerVersion = "1.5.10"
    }
}

dependencies {
    implementation( fileTree( mapOf( "dir" to "libs", "include" to "*.jar"  ) ) )
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))

    implementation( project(Evence.ical) )
    implementation("androidx.test.ext:junit-ktx:1.1.3")

    coreLibraryDesugaring( Libs.desugar )

    // CardView
    implementation( Libs.AndroidX.cardView )

    // Compose
    implementation( Libs.AndroidX.Compose.foundation )
    implementation( Libs.AndroidX.Compose.material )
    implementation( Libs.AndroidX.Compose.ui )
    implementation( Libs.AndroidX.Compose.uiTooling )
    implementation( Libs.AndroidX.Compose.materialAdapter )

    // Coil
    implementation( Libs.coil )

    // Dagger
    implementation( Libs.Google.hiltAndroid )
    kapt( Libs.Google.hiltCompiler )

    implementation( Libs.AndroidX.material )
    implementation( Libs.AndroidX.coordinatorLayout )

    // KTX
    implementation( Libs.AndroidX.coreKtx )

    // Okio
    implementation( Libs.SquareUp.okio )

    // RecyclerView
    implementation( Libs.AndroidX.recyclerView )

    // RxJava
    implementation( Libs.ReactiveX.rxJava )
    implementation( Libs.ReactiveX.rxAndroid )

    // Timber
    implementation( Libs.JakeWharton.timber )

    // ZXING
    implementation( Libs.Google.zxing )
    implementation( Libs.Google.zxingAndroid )

}