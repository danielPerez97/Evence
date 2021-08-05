apply(plugin = "com.github.ben-manes.versions")

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven( url = "https://www.jetbrains.com/intellij-repository/releases" )
        maven( url = "https://jetbrains.bintray.com/intellij-third-party-dependencies" )
        maven( url =  "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
        classpath("com.android.tools.build:gradle:7.0.0")
        classpath("com.squareup.sqldelight:gradle-plugin:${Libs.SquareUp.sqlDelightVersion}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Libs.Google.daggerHiltVersion}")
        classpath( "app.cash.licensee:licensee-gradle-plugin:1.2.0" )
        classpath("com.github.ben-manes:gradle-versions-plugin:0.39.0")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:${TestLibs.androidJunit5GradlePluginVersion}")
    }
}


apply(plugin = "kotlin")
apply(plugin = "com.squareup.sqldelight")

allprojects {
    repositories {
        google()
        jcenter()
    }
}
