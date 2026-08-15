// Pure-Kotlin/JVM module: domain models, muscle/mannequin geometry, and state-reducer logic
// that don't need the Android SDK. Kept separate from :app so this logic can be built and
// unit-tested with plain `./gradlew :core:test` even in environments without an Android SDK
// installed (e.g. this sandbox, which cannot reach dl.google.com to fetch SDK platforms).
plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
