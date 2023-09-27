// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {


    dependencies {
classpath("com.google.gms:google-services:4.4.0")
        //        classpath 'com.google.gms:google-services:4.3.15'
        val kotlin_version = "1.8.21"
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")

        val nav_version = "2.7.3"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
    repositories {
        mavenCentral()
    }
}
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
}