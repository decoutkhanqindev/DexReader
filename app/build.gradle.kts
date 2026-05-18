import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.baselineprofile)
  id("com.google.devtools.ksp")
  id("com.google.dagger.hilt.android")
  id("com.google.gms.google-services")
}

android {
  namespace = "com.decoutkhanqindev.dexreader"
  compileSdk = 36

  defaultConfig {
    applicationId = "com.decoutkhanqindev.dexreader"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    android.buildFeatures.buildConfig = true

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    buildConfigField(
      type = "String",
      name = "BASE_URL",
      value = "\"${properties.getProperty("BASE_URL")}\""
    )

    buildConfigField(
      type = "String",
      name = "UPLOAD_URL",
      value = "\"${properties.getProperty("UPLOAD_URL")}\""
    )
  }

  buildTypes {
    release {
      isMinifyEnabled = true // Enable code shrinking for release builds
      isShrinkResources = true // Enable resource shrinking for release builds
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("debug")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  buildFeatures {
    compose = true
  }

  lint {
    // False positive on AGP 9.x: ComponentActivity is a valid Activity subclass
    // but Lint's bytecode analysis doesn't resolve the inheritance chain correctly.
    disable += "Instantiatable"
  }
}

composeCompiler {
  // Emit reports only when explicitly requested via -PcomposeCompilerReports=true
  // to avoid slowing down regular builds.
  if (providers.gradleProperty("composeCompilerReports").orNull == "true") {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
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
  implementation(libs.androidx.lifecycle.process)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  // Immutable Collections
  implementation(libs.kotlinx.collections.immutable)

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

  // Zoomable image for Coil
  implementation(libs.zoomable.image.coil3)

  // Hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)

  // Material3 Icons Extended
  implementation(libs.androidx.material.icons.extended)

  // Firebase
  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.auth.ktx)
  implementation(libs.firebase.firestore.ktx)
  implementation(libs.firebase.analytics)

  // Logging
  implementation(libs.timber)

  // Shimmer
  implementation(libs.compose.shimmer)

  // Profile Installer — required for Macrobenchmark to install/drop baseline profile at runtime
  implementation(libs.androidx.profileinstaller)

  baselineProfile(project(":baselineprofile"))
}