import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(29)
    buildToolsVersion = Project.buildToolsVerion

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(Project.sdkVersion)
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
//        coreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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

    implementation( project( Project.core ) )

    implementation( Libs.cardView )
    implementation( Libs.coordinatorLayout )
    implementation( Libs.dagger )
    kapt( Libs.daggerCompiler )
    implementation( Libs.recyclerView )
    implementation( Libs.rxJava )
}