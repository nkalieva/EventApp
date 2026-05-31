plugins {
    alias(libs.plugins.android.application) // Этот плагин подключает Android
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt") // Оставляем только одно объявление KAPT



}

android {
    namespace = "com.example.evenplusapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.evenplusapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Зависимости для Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Зависимости для Room
    implementation(libs.room.runtime) // Runtime
    kapt(libs.room.compiler) // Аннотационный процессор (KAPT)
    implementation(libs.room.ktx) // Kotlin Extensions

    // Зависимость для Gson
    implementation("com.google.code.gson:gson:2.8.9") // или другая версия // Добавьте эту строку
    implementation ("androidx.room:room-runtime:2.5.0")  // или последняя версия
    implementation ("androidx.room:room-ktx:2.5.0")
    annotationProcessor ("androidx.room:room-compiler:2.5.0")  // Kapt для обработки аннотаций
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")  // для поддержки корутин
    // Зависимости для тестирования
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
