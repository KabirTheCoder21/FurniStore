// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath ("com.google.gms:google-services:4.4.0")

        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.5")
    }
}
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id ("com.android.library") version "7.3.0" apply false
}