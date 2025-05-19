import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  id("com.google.devtools.ksp")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.decoutkhanqindev.dexreader"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.decoutkhanqindev.dexreader"
    minSdk = 24
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    android.buildFeatures.buildConfig = true

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    buildConfigField(
      type = "String",
      name = "BASE_URL",
      value = properties.getProperty("BASE_URL")
    )

    buildConfigField(
      type = "String",
      name = "UPLOAD_URL",
      value = properties.getProperty("UPLOAD_URL")
    )
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  buildFeatures {
    compose = true
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  // Preferences DataStore
  implementation(libs.androidx.datastore.preferences)

  // Room Database
  implementation(libs.androidx.room.runtime)
  ksp(libs.androidx.room.compiler)
  implementation(libs.androidx.room.ktx)

  // ViewModel
  implementation(libs.androidx.lifecycle.viewmodel.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.lifecycle.viewmodel.savedstate)

  // Navigation
  implementation(libs.androidx.navigation.compose)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.androidx.hilt.navigation.compose)

  // Moshi
  implementation(libs.moshi.kotlin)
  ksp(libs.moshi.kotlin.codegen)

  // Retrofit
  implementation(libs.retrofit)
  implementation(libs.converter.moshi)

  // OkHttp
  implementation(libs.okhttp)
  implementation(libs.logging.interceptor)

  // Coil
  implementation(libs.coil.compose)
  implementation(libs.coil.network.okhttp)

  // Hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}