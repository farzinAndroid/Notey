plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id("androidx.navigation.safeargs")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "com.farzin.notey"
    compileSdk = 34

    lint {
        checkReleaseBuilds= false
        abortOnError= false
    }

    defaultConfig {
        applicationId = "com.farzin.notey"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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

    buildFeatures{
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
//    def lifecycle_version = "2.3.1"
//    def room_version = "2.4.0-alpha04"
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")


    implementation ("androidx.recyclerview:recyclerview:1.3.2")

    // RecyclerView Animator
    implementation ("jp.wasabeef:recyclerview-animators:4.0.2")

    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")

    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

    // Annotation processor
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.3.1")

    //Room
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")

    //Kotlin Extensions and Coroutines support for Room
    implementation ("androidx.room:room-ktx:2.6.1")

    // Coroutines
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.1.1")

    //material Components
    implementation ("com.google.android.material:material:1.5.0-alpha02")

    // Navigation Components
    implementation ("androidx.fragment:fragment-ktx:1.7.0-alpha07")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.5")

    //color picker library
    implementation ("com.thebluealliance:spectrum:0.7.1")

    // markdown edit text
    implementation ("io.github.yahiaangelo.markdownedittext:markdownedittext:1.1.3")
    implementation ("io.noties.markwon:core:4.6.0")
    implementation ("io.noties.markwon:ext-strikethrough:4.6.0")
    implementation ("io.noties.markwon:ext-tasklist:4.6.0")


    //hilt di
    implementation ("com.google.dagger:hilt-android:2.48")
    kapt ("com.google.dagger:hilt-compiler:2.48")
//    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")

    //datastore
    implementation ("androidx.datastore:datastore-preferences:1.0.0")

}