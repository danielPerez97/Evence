import org.jetbrains.kotlin.config.KotlinCompilerVersion
import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("app.cash.licensee")
}

// Create a variable called keystorePropertiesFile, and initialize it to your
// keystore.properties file, in the rootProject folder.
val keystorePropertiesFile = rootProject.file("keystore.properties")

// Initialize a new Properties() object called keystoreProperties.
val keystoreProperties = Properties()

// Load your keystore.properties file into the keystoreProperties object.
keystoreProperties.load(FileInputStream(keystorePropertiesFile))


android {
    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    compileSdk = Evence.compileSdkVersion
    buildToolsVersion = Evence.buildToolsVersion
    defaultConfig {
        applicationId = "projects.evenceteam.evence"
        minSdk = Evence.minSdkVersion
        targetSdk = Evence.targetSdkVersion
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        signingConfig = signingConfigs.getByName("release")
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
    }
}

licensee {
    allow("Apache-2.0")
    allow("CC0-1.0")
    allowUrl("https://developer.android.com/studio/terms.html")
    allowUrl("https://developers.google.com/ml-kit/terms")
    allowUrl("http://www.gnu.org/software/classpath/license.html")
    allowUrl("http://opensource.org/licenses/MIT")
    allowUrl("http://www.apache.org/license/LICENSE-2.0.txt")
    allowUrl("https://github.com/journeyapps/zxing-android-embedded/blob/master/COPYING")
    ignoreDependencies("io.coil-kt") {
        because("Uses Apache 2.0")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to "*.jar")))
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))

    implementation(project(Evence.core))
    implementation(project(Evence.evenceDatabase))
    implementation(project(Evence.fileSelectView))
    implementation(project(Evence.generateQrView))
    implementation(project(Evence.ical))
    implementation(project(Evence.qrCameraView))
    implementation(project(Evence.qrDialogView))
    implementation(project(Evence.licensesView))

    // AppCompat
    implementation(Libs.AndroidX.appCompat)

    // Camerax core
    implementation(Libs.AndroidX.Camera.cameraXCore)

    // CardView
    implementation(Libs.AndroidX.cardView)
    implementation(Libs.AndroidX.recyclerView)

    // ConstraintLayout
    implementation(Libs.AndroidX.constraintLayout)

    // Coil
    implementation(Libs.coil)

    // CoordinatorLayout
    implementation(Libs.AndroidX.coordinatorLayout)

    // Dagger 2
    implementation(Libs.Google.hiltAndroid)
    implementation("androidx.test.ext:junit:1.1.3")
    kapt(Libs.Google.hiltCompiler)

    // Desugar
    coreLibraryDesugaring(Libs.desugar)

    // Guava Conflict
    implementation(Libs.guavaConflict)

    // Kotlin Date-Time
    api(Libs.Kotlin.kotlinxDateTime)

    // Material widgets
    implementation(Libs.AndroidX.material)

    // Moshi
    implementation(Libs.SquareUp.moshi)

    // OkHttp
    implementation(Libs.SquareUp.okhttp)

    // Retrofit
    implementation(Libs.SquareUp.retrofit)
    implementation(Libs.SquareUp.retrofitMoshi)
    implementation(Libs.SquareUp.retrofitScalars)
    implementation(Libs.SquareUp.retrofitRxJava)

    // RxJava
    implementation(Libs.ReactiveX.rxJava)
    implementation(Libs.AndroidX.reactiveStreams)
    implementation(Libs.JakeWharton.rxBinding)
    implementation(Libs.JakeWharton.rxReplayingShare)

    // SQLDelight
    implementation(Libs.SquareUp.sqlDelightAndroidDriver)

    // Timber
    implementation(Libs.JakeWharton.timber)

    // ViewModel
    implementation(Libs.AndroidX.Lifecycle.viewModel)
    implementation(Libs.AndroidX.Lifecycle.viewModelKtx)

    // ZXING
    implementation(Libs.Google.zxing)
    implementation(Libs.Google.zxingAndroid)

    // Testing
    testImplementation(TestLibs.junit4)
    androidTestImplementation(TestLibs.runner)
    androidTestImplementation(TestLibs.espresso)
    debugImplementation(TestLibs.leakCanary)
}

repositories {
    mavenCentral()

    // Needed for kotlinx-datetime
    // TODO("Keep up with what JetBrains does with this, bintray is getting shut down in May this is going to break our build")
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}

/**
 * Used to copy artifacts.json into resources/raw
 */
tasks.register<Copy>("copyLicenses") {
    description = "Copying artifacts.json into res/raw..."
    from(layout.buildDirectory.dir("reports/licensee/release/artifacts.json"))
    println(layout.buildDirectory.dir("reports/licensee/release/artifacts.json"))
    into("src/main/res/raw")
}

afterEvaluate {
    tasks.named("assemble") {
        dependsOn("copyLicenses")
    }

    tasks.named("copyLicenses") {
        dependsOn("licensee")
    }
}