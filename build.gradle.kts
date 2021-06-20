apply(plugin = "com.github.ben-manes.versions")

buildscript {
    val compose_version by extra("1.0.0-beta07")
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven( url = "https://www.jetbrains.com/intellij-repository/releases" )
        maven( url = "https://jetbrains.bintray.com/intellij-third-party-dependencies" )
        maven( url =  "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
        classpath("com.android.tools.build:gradle:7.0.0-beta03")
        classpath("com.squareup.sqldelight:gradle-plugin:${Libs.SquareUp.sqlDelightVersion}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Libs.Google.daggerHiltVersion}")
        classpath( "app.cash.licensee:licensee-gradle-plugin:1.0.2" )
        classpath("com.github.ben-manes:gradle-versions-plugin:0.39.0")
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
