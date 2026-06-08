package com.xsandbox.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * SandboxConfig: Configuration for a cloned app sandbox instance
 */
@Entity(tableName = "sandbox_configs")
data class SandboxConfig(
    @PrimaryKey(autoGenerate = false)
    val sandboxId: String,
    val clonedPackageName: String,
    val clonedAppName: String,
    val deviceProfileId: String,
    val isolationLevel: Int = ISOLATION_FULL,  // 0=None, 1=Partial, 2=Full
    val storagePath: String,
    val cacheDir: String,
    val databaseDir: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isRunning: Boolean = false
) {
    companion object {
        const val ISOLATION_NONE = 0
        const val ISOLATION_PARTIAL = 1
        const val ISOLATION_FULL = 2
    }
}
