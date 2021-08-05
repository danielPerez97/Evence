object Evence
{
    const val compileSdkVersion = 30
    const val buildToolsVersion = "30.0.2"
    const val targetSdkVersion = 30
    const val minSdkVersion = 26

    const val core = ":Core"
    const val ical = ":ICal"
    const val fileSelectView = ":FileSelectView"
    const val generateQrView = ":GenerateQRView"
    const val qrCameraView = ":QRCameraView"
    const val qrDialogView = ":QRDialogView"
    const val evenceDatabase = ":EvenceDb"
    const val licensesView = ":licensesview"
}

object Libs
{
    // Coil
    const val coil = "io.coil-kt:coil:1.2.2"

    // Desugar
    const val desugar = "com.android.tools:desugar_jdk_libs:1.1.5"

    // Guava, Guava Conflict
    private const val guavaConflictVersion = "9999.0-empty-to-avoid-conflict-with-guava"
    private const val guavaVersion = "30.1-android"
    const val guava = "com.google.guava:guava:$guavaVersion"
    const val guavaConflict = "com.google.guava:listenablefuture:$guavaConflictVersion"

    object AndroidX
    {
        // App Compat
        const val appCompat = "androidx.appcompat:appcompat:1.3.0"

        // CardView
        private const val cardViewVersion = "1.0.0"
        const val cardView = "androidx.cardview:cardview:$cardViewVersion"

        // ConstraintLayout
        private const val constraintLayoutVersion = "2.0.4"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"

        // CoordinatorLayout
        private const val coordinatorLayoutVersion = "1.1.0"
        const  val coordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:$coordinatorLayoutVersion"

        // KTX Core
        const val coreKtx = "androidx.core:core-ktx:1.5.0"

        // Material
        private const val materialVersion = "1.3.0"
        const val material = "com.google.android.material:material:$materialVersion"

        // Reactive Streams Android
        private const val reactiveStreamsVersion = "2.3.1"
        const val reactiveStreams = "androidx.lifecycle:lifecycle-reactivestreams:2.1.0"

        // RecyclerView
        private const val recyclerViewVersion = "1.2.1"
        const val recyclerView = "androidx.recyclerview:recyclerview:$recyclerViewVersion"

        object Activity
        {
            const val activityCompose = "androidx.activity:activity-compose:1.3.0-beta01"
        }

        object Camera
        {
            private const val cameraXVersion = "1.0.0-rc01"
            private const val cameraXCameraViewVersion = "1.0.0-alpha10"
            private const val cameraXExtensionsVersion = "1.0.0-alpha10"
            const val cameraXCamera2 = "androidx.camera:camera-camera2:$cameraXVersion"
            const val cameraXCore = "androidx.camera:camera-core:$cameraXVersion"
            const val cameraXLifecycle = "androidx.camera:camera-lifecycle:$cameraXVersion"
            const val cameraXCameraView = "androidx.camera:camera-view:$cameraXCameraViewVersion"
            const val cameraXExtensions = "androidx.camera:camera-extensions:$cameraXExtensionsVersion"
        }

        object Compose
        {
            const val version = "1.0.1"

            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val material = "androidx.compose.material:material:$version"
            const val ui = "androidx.compose.ui:ui:$version"
            const val uiTooling = "androidx.compose.ui:ui-tooling:$version"

            const val materialAdapter = "com.google.android.material:compose-theme-adapter:$version"

            private const val activityComposeVersion = "1.3.0"
            const val activityCompose = "androidx.activity:activity-compose:$activityComposeVersion"

            private const val constraintLayoutComposeVersion = "1.0.0-beta02"
            const val constraintLayoutCompose = "androidx.constraintlayout:constraintlayout-compose:$constraintLayoutComposeVersion"
        }

        object ConstraintLayout
        {
            const val constraintLayoutCompose = "androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha07"
        }

        object Hilt
        {
            const val navigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha02"
        }

        object Lifecycle
        {
            // Runtime-KTX
            const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"

            // ViewModel
            private const val lifeCycleVersion = "2.3.0"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVersion"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel:$lifeCycleVersion"

            // ViewModel Compose
            private const val viewModelComposeVersion = "1.0.0-alpha07"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$viewModelComposeVersion"
        }

        object Navigation
        {
            const val compose = "androidx.navigation:navigation-compose:2.4.0-alpha02"
        }

        object Test
        {
            private const val testVersion = "1.3.0"
            const val runner = "androidx.test:runner:$testVersion"
            const val rules = "androidx.test:rules:$testVersion"

            private const val espressoVersion = "3.4.0"
            const val espressoCore = "androidx.test.espresso:espresso-core:$espressoVersion"

            object Ext
            {
                private const val version = "1.1.2"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }
        }
    }

    object Google
    {
        // Dagger Hilt
        const val daggerHiltVersion = "2.38.1"
        const val hiltAndroidGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$daggerHiltVersion"
        const val hiltAndroid = "com.google.dagger:hilt-android:$daggerHiltVersion"
        const val hiltAndroidTesting = "com.google.dagger:hilt-android-testing:$daggerHiltVersion"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:$daggerHiltVersion"

        // Material Android
        const val materialAndroid = "com.google.android.material:material:1.3.0"

        // Google ML Kit
        private const val mlkitBarcodeVersion= "16.1.2"
        private const val mlkitTextVersion= "16.2.0"
        const val mlkitBarcode = "com.google.mlkit:barcode-scanning:$mlkitBarcodeVersion"
        const val mlkitTextRecognition = "com.google.android.gms:play-services-mlkit-text-recognition:$mlkitTextVersion"

        // Mobile Vision
        private const val visionVersion = "20.1.3"
        const val vision = "com.google.android.gms:play-services-vision:$visionVersion"

        // ZXING
        private const val zxingVersion = "3.4.1"
        const val zxing = "com.google.zxing:core:$zxingVersion"
        const val zxingAndroid = "com.journeyapps:zxing-android-embedded:4.2.0"
    }

    object JUnit
    {
        const val junit4 = "junit:junit:4.13.2"
    }

    object Kotlin
    {
        const val version = "1.5.10"

        // Kotlinx DateTime
        private const val kotlinxDateTimeVersion = "0.2.1"
        const val kotlinxDateTime = "org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDateTimeVersion"
    }

    object JakeWharton
    {
        // RxBinding
        private const val rxBindingVersion = "4.0.0"
        const val rxBinding = "com.jakewharton.rxbinding4:rxbinding:$rxBindingVersion"


        // RxReplayingShare
        private const val rxReplayingShareVersion = "3.0.0"
        const val rxReplayingShare = "com.jakewharton.rx3:replaying-share:$rxReplayingShareVersion"

        // Timber
        private const val version = "4.7.1"
        const val timber = "com.jakewharton.timber:timber:$version"
    }

    object ReactiveX
    {
        // RxJava
        private const val rxjavaVersion = "3.0.13"
        private const val rxAndroidVersion = "3.0.0"
        const val rxJava = "io.reactivex.rxjava3:rxjava:$rxjavaVersion"
        const val rxAndroid = "io.reactivex.rxjava3:rxandroid:$rxAndroidVersion"
    }

    object SquareUp
    {
        // Moshi
        private const val moshiVersion = "1.12.0"
        const val moshi = "com.squareup.moshi:moshi:$moshiVersion"
        const val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"
        const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:$moshiVersion"

        // OkHttp
        private const val okhttpVersion = "4.7.2"
        const val okhttp = "com.squareup.okhttp3:okhttp:$okhttpVersion"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"

        // Okio
        private const val okioVersion = "2.10.0"
        const val okio = "com.squareup.okio:okio:$okioVersion"

        // Retrofit
        private const val retrofitVersion = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        const val retrofitMoshi = "com.squareup.retrofit2:converter-moshi:$retrofitVersion"
        const val retrofitRxJava = "com.squareup.retrofit2:adapter-rxjava3:$retrofitVersion"
        const val retrofitScalars = "com.squareup.retrofit2:converter-scalars:$retrofitVersion"

        // SQLDelight
        const val sqlDelightVersion = "1.5.1"
        const val sqlDelightAndroidDriver = "com.squareup.sqldelight:android-driver:$sqlDelightVersion"
        const val sqlDelightCoroutines = "com.squareup.sqldelight:coroutines-extensions-jvm:$sqlDelightVersion"
    }
}

object TestLibs
{
    private const val runnerVersion = "1.3.0"
    const val runner = "androidx.test:runner:$runnerVersion"

    private const val espressoVersion = "3.3.0"
    const val espresso = "androidx.test.espresso:espresso-core:$espressoVersion"

    const val junit4 = "junit:junit:4.13.2"

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.7"

    private const val junit5Version = "5.7.1"
    const val androidJunit5GradlePluginVersion = "1.7.1.1"
    const val androidJunit5GradlePlugin = "de.mannodermaus.android-junit5"
    const val junit5JupiterApi = "org.junit.jupiter:junit-jupiter-api:$junit5Version"
    const val junit5Runtime = "org.junit.jupiter:junit-jupiter-engine:$junit5Version"
}

object Coroutines
{
    private const val coroutinesVersion = "1.5.0"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
}