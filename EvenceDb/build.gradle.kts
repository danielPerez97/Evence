
plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("EvenceDatabase") {
        packageName = "daniel.perez.evencedb"
    }
}

repositories {
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-common")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
            }
        }
    }

    jvm()
    sourceSets {
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:5.6.0")
                implementation("org.jetbrains.kotlin:kotlin-test")
                implementation("org.jetbrains.kotlin:kotlin-test-junit5")
                implementation("com.squareup.sqldelight:sqlite-driver:${Project.sqlDelightVersion}")
            }
        }
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
}