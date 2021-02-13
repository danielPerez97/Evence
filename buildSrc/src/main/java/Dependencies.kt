object Evence
{
    const val compileSdkVersion = 29
    const val buildToolsVersion = "29.0.3"
    const val targetSdkVersion = 29
    const val minSdkVersion = 26
}

object Libs
{
    // Coil
    private const val coilVersion = "1.1.1"
    // const val coilBase = "io.coil-kt:coil-base:$coilVersion"
    const val coil = "io.coil-kt:coil:$coilVersion"

    // ConstraintLayout
    private const val constraintLayoutVersion = "1.1.3"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"

    // CoordinatorLayout
    private const val coordinatorLayoutVersion = "1.1.0"
    const  val coordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:$coordinatorLayoutVersion"

    // Dagger
    private const val daggerVersion = "2.31.2"
    const val dagger = "com.google.dagger:dagger:$daggerVersion"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"

    // Desugar
    private const val desugarVersion = "1.0.9"
    const val desugar = "com.android.tools:desugar_jdk_libs:$desugarVersion"


    // ICal4J
    private const val ical4jVersion = "3.0.9"
    const val ical4j = "org.mnode.ical4j:ical4j:$ical4jVersion"

    // Google
    const val playServicesAuth = "com.google.android.gms:play-services-auth:17.0.0"
    const val playApiClient = "com.google.api-client:google-api-client:1.22.0"
    const val playApiClientAndroid = "com.google.api-client:google-api-client-android:1.22.0"
    const val playApiDrive = "com.google.android.gms:play-services-drive:17.0.0"

    // Material
    private const val materialVersion = "1.2.1"
    const val material = "com.google.android.material:material:$materialVersion"

    // Moshi
    private const val moshiVersion = "1.9.2"
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

    // Reactive Streams
    private const val reactiveStreamsVersion = "2.1.0"
    const val reactiveStreams = "androidx.lifecycle:lifecycle-reactivestreams:2.1.0"

    // RecyclerView, CardView
    private const val recyclerViewVersion = "1.0.0"
    const val recyclerView = "androidx.recyclerview:recyclerview:$recyclerViewVersion"
    const val cardView = "androidx.cardview:cardview:$recyclerViewVersion"

    // Retrofit
    private const val retrofitVersion = "2.9.0"
    const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    const val retrofitMoshi = "com.squareup.retrofit2:converter-moshi:$retrofitVersion"
    const val retrofitRxJava = "com.squareup.retrofit2:adapter-rxjava3:$retrofitVersion"
    const val retrofitScalars = "com.squareup.retrofit2:converter-scalars:$retrofitVersion"

    // RxBinding
    private const val rxBindingVersion = "4.0.0"
    const val rxBinding = "com.jakewharton.rxbinding4:rxbinding:$rxBindingVersion"

    // RxJava
    private const val rxjavaVersion = "3.0.9"
    private const val rxAndroidVersion = "3.0.0"
    const val rxJava = "io.reactivex.rxjava3:rxjava:$rxjavaVersion"
    const val rxAndroid = "io.reactivex.rxjava3:rxandroid:$rxAndroidVersion"

    // RxReplayingShare
    private const val rxReplayingShareVersion = "3.0.0"
    const val rxReplayingShare = "com.jakewharton.rx3:replaying-share:$rxReplayingShareVersion"

    // SqlDelight
    const val sqlDelightAndroidDriver = "com.squareup.sqldelight:android-driver:${Project.sqlDelightVersion}"
    const val sqlDelightInMemoryDriver = "com.squareup.sqldelight:sqlite-driver:${Project.sqlDelightVersion}"

    // Timber
    private const val timberVersion = "4.7.1"
    const val timber = "com.jakewharton.timber:timber:$timberVersion"

    // ViewModel, LiveData
    private const val lifeCycleVersion = "2.1.0"
    const val lifeCycle = "androidx.lifecycle:lifecycle-extensions:$lifeCycleVersion"

    // ZXING
    private const val zxingVersion = "3.4.1"
    const val zxing = "com.google.zxing:core:$zxingVersion"
    const val zxingAndroid = "com.journeyapps:zxing-android-embedded:3.2.0@aar"

    //Google Mobile Vision
    private const val visionVersion="20.0.0"
    const val vision="com.google.android.gms:play-services-vision:$visionVersion"

    //CameraX
    private const val cameraXVersion = "1.0.0-beta06"
    private const val cameraXCameraViewVersion = "1.0.0-alpha13"
    const val cameraXCamera2 = "androidx.camera:camera-camera2:$cameraXVersion"
    const val cameraXLifecycle = "androidx.camera:camera-lifecycle:$cameraXVersion"
    const val cameraXCameraView = "androidx.camera:camera-view:$cameraXCameraViewVersion"
    const val cameraXCore = "androidx.camera:camera-core:$cameraXVersion"

    //Google ML Kit barcode-scanning
    private const val mlkitBarcodeVersion="16.0.0"
    const val mlkitBarcode = "com.google.mlkit:barcode-scanning:$mlkitBarcodeVersion"

    //guava conflict - listenablefuture
    //private const val guavaConflictVersion = "1.0"
    private const val guavaConflictVersion = "9999.0-empty-to-avoid-conflict-with-guava"
    const val guavaConflict = "com.google.guava:listenablefuture:$guavaConflictVersion"

    //guava
    private const val guavaVersion = "29.0-android"
    const val guava ="com.google.guava:guava:$guavaVersion"

}

object Project
{
    const val sdkVersion = 30
    const val buildToolsVerion = "30.0.0"
    const val kotlinVersion = "1.4.21"
    const val sqlDelightVersion = "1.4.3"
    const val core = ":Core"
    const val ical = ":ICal"
    const val fileSelectView = ":FileSelectView"
    const val generateQrView = ":GenerateQRView"
    const val qrCameraView = ":QRCameraView"
    const val qrDialogView = ":QRDialogView"
    const val evenceDatabase = ":EvenceDb"
}