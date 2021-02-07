
plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("EvenceDatabase") {
        packageName = "daniel.perez.evencedb"
    }
}

repositories {
    // Needed for kotlinx-datetime
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}

kotlin {
    android()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")
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
                implementation("com.squareup.sqldelight:sqlite-driver:${Project.sqlDelightVersion}")
            }
        }

        val androidMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")
            }
        }
    }
}

android {
    compileSdkVersion( Evence.compileSdkVersion )
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion( Evence.minSdkVersion )
        targetSdkVersion( Evence.targetSdkVersion )
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
}