import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
//    implementation(kotlin("stdlib-jdk8", org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION))

    implementation( Libs.SquareUp.okio )
    implementation( Libs.JakeWharton.timber )

    testImplementation(TestLibs.junit5JupiterApi)
    testRuntimeOnly(TestLibs.junit5Runtime)
}

tasks.test {
    useJUnitPlatform()
}

// config JVM target to 1.8 for kotlin compilation tasks
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}