
plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("EvenceDatabase") {
        packageName = "teamevence.evenceapp.evencedb"
    }
}

android {
    compileSdk = Evence.compileSdkVersion
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Evence.minSdkVersion
        targetSdk = Evence.targetSdkVersion
    }

    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}

kotlin {
    android()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib")
                api( Libs.Kotlin.kotlinxDateTime )
            }
        }

        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-common")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
            }
        }

        jvm()
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:5.6.0")
                implementation("org.jetbrains.kotlin:kotlin-test")
                implementation("org.jetbrains.kotlin:kotlin-test-junit5")
                implementation("com.squareup.sqldelight:sqlite-driver:${Libs.SquareUp.sqlDelightVersion}")
            }
        }

        val androidMain by getting {
            dependencies {
                api( Libs.Kotlin.kotlinxDateTime )
                api("com.squareup.sqldelight:rxjava3-extensions:${Libs.SquareUp.sqlDelightVersion}")
            }
        }
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
}