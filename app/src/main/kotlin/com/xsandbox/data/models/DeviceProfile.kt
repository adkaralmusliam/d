package com.xsandbox.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * DeviceProfile: Represents a spoofed device configuration
 * Stored in Room database for persistence
 */
@Entity(tableName = "device_profiles")
data class DeviceProfile(
    @PrimaryKey(autoGenerate = false)
    val sandboxId: String,
    val deviceName: String,
    val androidId: String,
    val model: String,
    val brand: String,
    val manufacturer: String,
    val hardware: String,
    val buildFingerprint: String,
    val imei: String = "",
    val wifiMacAddress: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = false
)
