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

    implementation( project(Project.ical) )

    coreLibraryDesugaring( Libs.desugar )

    // CardView
    implementation( Libs.cardView )

    // Coil
    implementation( Libs.coil )

    // Dagger
    implementation( Libs.dagger )
    kapt( Libs.daggerCompiler )

    //Google Mobile Vision API
    implementation( Libs.vision )

    implementation( Libs.material )

    // Okio
    implementation( Libs.okio )

    // RecyclerView
    implementation( Libs.recyclerView )

    // RxJava
    implementation( Libs.rxJava )
    implementation( Libs.rxAndroid )

    // Timber
    implementation( Libs.timber )

    // ZXING
    implementation( Libs.zxing )
    implementation( Libs.zxingAndroid )

}