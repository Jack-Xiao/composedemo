// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    id("com.datadoghq.dd-sdk-android-gradle-plugin") version "1.15.0"
}

// 确保 Kotlin 版本与 Compose 编译器扩展版本兼容
buildscript {
    repositories {
        google()
        mavenCentral()
    }
}