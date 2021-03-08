import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion( Evence.compileSdkVersion )
    buildToolsVersion = Evence.buildToolsVersion
    defaultConfig {
        applicationId = "projects.csce.evence"
        minSdkVersion( Evence.minSdkVersion )
        targetSdkVersion( Evence.targetSdkVersion )
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
//        getByName("debug") {
//            signingConfig( signing )
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
    implementation( fileTree( mapOf( "dir" to "libs", "include" to "*.jar" ) ) )
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))

    implementation( project( Project.core ) )
    implementation( project( Project.evenceDatabase ) )
    implementation( project( Project.fileSelectView ) )
    implementation( project( Project.generateQrView ) )
    implementation( project( Project.ical ) )
    implementation( project( Project.qrCameraView ) )
    implementation( project( Project.qrDialogView ) )

    // AppCompat
    implementation( Libs.appCompat )

    // Camerax core
    implementation( Libs.cameraXCore )

    // CardView
    implementation( Libs.cardView )
    implementation( Libs.recyclerView )

    // ConstraintLayout
    implementation( Libs.constraintLayout )

    // Coil
    implementation( Libs.coil )

    // CoordinatorLayout
    implementation( Libs.coordinatorLayout )

    // Dagger 2
    implementation( Libs.daggerHilt )
    kapt( Libs.daggerHiltCompiler )

    // Desugar
    coreLibraryDesugaring( Libs.desugar )

    // Guava Conflict
    implementation( Libs.guavaConflict )

    // Kotlin Date-Time
    api( Libs.kotlinxDateTime )

    // Material widgets
    implementation( Libs.material )

    // Moshi
    implementation( Libs.moshi )

    // OkHttp
    implementation( Libs.okhttp )

    // Retrofit
    implementation( Libs.retrofit )
    implementation( Libs.retrofitMoshi )
    implementation( Libs.retrofitScalars )
    implementation( Libs.retrofitRxJava )

    // RxJava
    implementation( Libs.rxJava )
    implementation( Libs.reactiveStreams )
    implementation( Libs.rxBinding )
    implementation( Libs.rxReplayingShare )

    // SQLDelight
    implementation( Libs.sqlDelightAndroidDriver )

    // Timber
    implementation( Libs.timber )

    // ViewModel
    implementation( Libs.viewModel )
    implementation( Libs.viewModelKtx )

    // ZXING
    implementation( Libs.zxing )
    implementation( Libs.zxingAndroid )

    // Testing
    testImplementation( TestLibs.junit4 )
    androidTestImplementation( TestLibs.runner )
    androidTestImplementation( TestLibs.espresso )
    debugImplementation( TestLibs.leakCanary )
}

repositories {
    mavenCentral()

    // Needed for kotlinx-datetime
    // TODO("Keep up with what JetBrains does with this, bintray is getting shut down in May this is going to break our build")
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}