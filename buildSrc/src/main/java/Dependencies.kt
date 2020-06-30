object Libs
{
    // ConstraintLayout
    private const val constraintLayoutVersion = "1.1.3"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"

    // CoordinatorLayout
    private const val coordinatorLayoutVersion = "1.1.0"
    const  val coordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:$coordinatorLayoutVersion"

    // Dagger
    private const val daggerVersion = "2.25.3"
    const val dagger = "com.google.dagger:dagger:$daggerVersion"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"

    // ICal4J
    private const val ical4jVersion = "3.0.9"
    const val ical4j = "org.mnode.ical4j:ical4j:$ical4jVersion"

    // Google
    const val playServicesAuth = "com.google.android.gms:play-services-auth:17.0.0"
    const val playApiClient = "com.google.api-client:google-api-client:1.22.0"
    const val playApiClientAndroid = "com.google.api-client:google-api-client-android:1.22.0"
    const val playApiDrive = "com.google.android.gms:play-services-drive:17.0.0"

    // Material
    private const val materialVersion = "1.1.0"
    const val material = "com.google.android.material:material:$materialVersion"

    // Material Widgets
    private const val materialWidgetsVersion = "1.1.0"
    const val materialWidgets = "com.google.android.material:material:$materialWidgetsVersion"

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
    private const val okioVersion = "2.6.0"
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
    const val retrofitRxJava = "com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion"
    const val retrofitScalars = "com.squareup.retrofit2:converter-scalars:$retrofitVersion"

    // RxBinding
    private const val rxBindingVersion = "3.0.0"
    const val rxBinding = "com.jakewharton.rxbinding3:rxbinding:$rxBindingVersion"

    // RxJava
    private const val rxjavaVersion = "2.2.19"
    private const val rxAndroidVersion = "2.1.1"
    const val rxJava = "io.reactivex.rxjava2:rxjava:$rxjavaVersion"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:$rxAndroidVersion"

    // RxReplayingShare
    private const val rxReplayingShareVersion = "2.2.0"
    const val rxReplayingShare = "com.jakewharton.rx2:replaying-share:$rxReplayingShareVersion"

    // Timber
    private const val timberVersion = "4.7.1"
    const val timber = "com.jakewharton.timber:timber:$timberVersion"

    // ViewModel, LiveData
    private const val lifeCycleVersion = "2.1.0"
    const val lifeCycle = "androidx.lifecycle:lifecycle-extensions:$lifeCycleVersion"

    // ZXING
    private const val zxingVersion = "3.2.1"
    const val zxing = "com.google.zxing:core:$zxingVersion"
    const val zxingAndroid = "com.journeyapps:zxing-android-embedded:3.2.0@aar"

    //Google Mobile Vision
    private const val visionVersion="20.0.0"
    const val vision="com.google.android.gms:play-services-vision:$visionVersion"

    //Google ML Kit barcode-scanning
    private const val mlkitBarcodeVersion="16.0.0"
    const val mlkitBarcode = "com.google.mlkit:barcode-scanning:$mlkitBarcodeVersion"
}

object Project
{
    const val sdkVersion = 30
    const val buildToolsVerion = "30.0.0"
    const val core = ":Core"
    const val ical = ":ICal"
    const val fileSelectView = ":FileSelectView"
    const val generateQrView = ":GenerateQRView"
    const val qrCameraView = ":QRCameraView"
    const val qrDialogView = ":QRDialogView"
}