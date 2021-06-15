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
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Libs.Kotlin.version}")
        classpath("com.android.tools.build:gradle:7.0.0-beta03")
        classpath("com.squareup.sqldelight:gradle-plugin:${Libs.SquareUp.sqlDelightVersion}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Libs.Google.daggerHiltVersion}")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.39.0")


        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
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
