object Libs
{
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

    // ViewModel, LiveData
    private const val lifeCycleVersion = "2.1.0"
    const val lifeCycle = "androidx.lifecycle:lifecycle-extensions:$lifeCycleVersion"

    // ZXING
    private const val zxingVersion = "3.2.1"
    const val zxing = "com.google.zxing:core:$zxingVersion"
    const val zxingAndroid = "com.journeyapps:zxing-android-embedded:3.2.0@aar"
}

object Project
{
    const val ical = ":ICal"
}

//// Desugaring(Java Streams API)
//coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:1.0.5'
//
//// Dagger 2
//def daggerVersion = "2.25"
//implementation "com.google.dagger:dagger-android:$daggerVersion"
//implementation "com.google.dagger:dagger-android-support:$daggerVersion"
//// if you use the support libraries
//kapt "com.google.dagger:dagger-android-processor:$daggerVersion"
//kapt "com.google.dagger:dagger-compiler:$daggerVersion"
//
//// RxJava
//implementation "io.reactivex.rxjava2:rxjava:2.2.13"
//implementation 'androidx.lifecycle:lifecycle-reactivestreams:2.1.0'
//implementation 'com.jakewharton.rxbinding3:rxbinding:3.0.0'
//implementation 'com.jakewharton.rx2:replaying-share:2.2.0'
//
//
//// Moshi
//implementation("com.squareup.moshi:moshi:1.8.0")
//
//// OkHttp
//implementation("com.squareup.okhttp3:okhttp:4.2.0")
//
//// Retrofit
//implementation('com.squareup.retrofit2:retrofit:2.6.2')
//implementation("com.squareup.retrofit2:converter-moshi:2.6.1")
//implementation 'com.squareup.retrofit2:converter-scalars:2.6.1'
//implementation 'com.squareup.retrofit2:adapter-rxjava2:2.6.1'
//
//// ICal4j
//implementation("org.mnode.ical4j:ical4j:3.0.9")
//
////ZXING
//implementation 'com.google.zxing:core:3.2.1'
//implementation 'com.journeyapps:zxing-android-embedded:3.2.0@aar'
//
//// Google API
//implementation "com.google.android.gms:play-services-auth:17.0.0"
//implementation 'com.google.api-client:google-api-client:1.22.0'
//implementation 'com.google.api-client:google-api-client-android:1.22.0'
//implementation 'com.google.android.gms:play-services-drive:17.0.0'
//
//// CardView
//def recyclerview_version = "1.0.0"
//implementation 'androidx.cardview:cardview:1.0.0'
//implementation "androidx.recyclerview:recyclerview:$recyclerview_version"
//
//
//// ViewModel, LiveData
//def lifecycle_version = "2.1.0"
//implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
//
////Material widgets
//implementation 'com.google.android.material:material:1.1.0'