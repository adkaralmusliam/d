-keep class com.xsandbox.** { *; }
-keep class com.xsandbox.ndk.** { *; }
-keep class com.xsandbox.core.** { *; }
-keep class com.xsandbox.data.** { *; }

# Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Room database
-keep class androidx.room.** { *; }

# Timber logging
-keepclassmembers class com.jakewharton.timber.log.** {
    *;
}

# Keep native method signatures
-keepclasseswithmembernames class * {
    native <methods>;
}

# Preserve line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
