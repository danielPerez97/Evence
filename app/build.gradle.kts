import org.jetbrains.kotlin.config.KotlinCompilerVersion
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    signingConfigs {
        create("release") {
            val props = Properties()
            val propsFile = File(rootDir, "keystore.properties")
            props.load( propsFile.inputStream() )
            storeFile = File( propsFile.parentFile, props["storeFile"] as String )
            storePassword = props["storePassword"] as String
            keyAlias = props["keyAlias"] as String
            keyPassword = props["keyPassword"] as String
        }
    }
    compileSdkVersion(29)
    buildToolsVersion = "29.0.3"
    defaultConfig {
        applicationId = "projects.csce.evence"
        minSdkVersion(21)
        targetSdkVersion(29)
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
        coreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation( fileTree( mapOf( "dir" to "libs", "include" to "*.jar" ) ) )
    implementation( "androidx.appcompat:appcompat:1.1.0" )
    implementation( "androidx.constraintlayout:constraintlayout:1.1.3" )
    testImplementation( "junit:junit:4.12" )
    androidTestImplementation( "androidx.test:runner:1.2.0" )
    androidTestImplementation( "androidx.test.espresso:espresso-core:3.2.0" )
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))
    coreLibraryDesugaring( "com.android.tools:desugar_jdk_libs:1.0.5" )

    implementation( project( Project.ical ) )

    // Dagger 2
    implementation( Libs.dagger )
    kapt( Libs.daggerCompiler )

    // Timber
    implementation( Libs.timber )

    // RxJava
    implementation( Libs.rxJava )
    implementation( Libs.reactiveStreams )
    implementation( Libs.rxBinding )
    implementation( Libs.rxReplayingShare )


    // Moshi
    implementation( Libs.moshi )

    // OkHttp
    implementation( Libs.okhttp )

    // Retrofit
    implementation( Libs.retrofit )
    implementation( Libs.retrofitMoshi )
    implementation( Libs.retrofitScalars )
    implementation( Libs.retrofitRxJava )

    // ICal4j
    implementation( Libs.ical4j )

    // ZXING
    implementation( Libs.zxing )
    implementation( Libs.zxingAndroid )

    // Google API
    implementation( Libs.playServicesAuth )
    implementation( Libs.playApiClient )
    implementation( Libs.playApiClientAndroid )
    implementation( Libs.playApiDrive )

    // CardView
    implementation( Libs.cardView )
    implementation( Libs.recyclerView )


    // ViewModel, LiveData
    implementation( Libs.lifeCycle )

    //Material widgets
    implementation( Libs.material )

    //Google Mobile Vision API
    implementation(Libs.vision)
}
repositories {
    mavenCentral()
}