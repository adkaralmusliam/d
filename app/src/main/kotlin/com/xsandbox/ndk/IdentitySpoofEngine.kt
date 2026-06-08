package com.xsandbox.ndk

import android.content.Context
import timber.log.Timber

/**
 * Kotlin JNI wrapper for the native IdentitySpoofEngine C++ library
 * Provides bridging between Kotlin and C++ for device identity spoofing
 */
class IdentitySpoofEngine {
    companion object {
        // Load the native library
        init {
            try {
                System.loadLibrary("identity_spoof")
                Timber.i("Native library loaded successfully")
            } catch (e: UnsatisfiedLinkError) {
                Timber.e(e, "Failed to load native library: identity_spoof")
            }
        }

        /**
         * Initialize the native spoofing engine
         */
        fun initialize(): Boolean {
            return nativeInitialize()
        }

        /**
         * Register a spoofed device identity
         */
        fun registerSpoofedIdentity(
            sandboxId: String,
            androidId: String,
            model: String,
            brand: String,
            manufacturer: String,
            hardware: String,
            imei: String = "",
            wifiMac: String = ""
        ) {
            nativeRegisterIdentity(
                sandboxId,
                androidId,
                model,
                brand,
                manufacturer,
                hardware,
                imei,
                wifiMac
            )
            Timber.d("Registered spoofed identity for sandbox: $sandboxId")
        }

        /**
         * Apply device spoofing for a specific sandbox
         */
        fun applySpoofing(sandboxId: String): Boolean {
            val result = nativeApplySpoofing(sandboxId)
            Timber.i("Applied spoofing for sandbox: $sandboxId, result=$result")
            return result
        }

        /**
         * Get the currently spoofed device model
         */
        fun getDeviceModel(): String {
            return nativeGetDeviceModel()
        }
    }

    // Native method declarations (JNI)
    private external companion object {
        fun nativeInitialize(): Boolean

        fun nativeRegisterIdentity(
            sandboxId: String,
            androidId: String,
            model: String,
            brand: String,
            manufacturer: String,
            hardware: String,
            imei: String,
            wifiMac: String
        )

        fun nativeApplySpoofing(sandboxId: String): Boolean

        fun nativeGetDeviceModel(): String
    }
}
