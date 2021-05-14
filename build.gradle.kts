// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha15")
        classpath("com.google.gms:google-services:4.3.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Project.kotlinVersion}")
        classpath("com.squareup.sqldelight:gradle-plugin:${Project.sqlDelightVersion}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Libs.daggerHiltVersion}")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
