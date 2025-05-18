
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
    alias(libs.plugins.google.gms.google.services)

}

buildscript {
    repositories {
        google()  // Ensure this is included
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath ("com.google.gms:google-services:4.4.2")

        val nav_version = "2.8.5"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")

    }
}
