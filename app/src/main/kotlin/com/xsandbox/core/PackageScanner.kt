package com.xsandbox.core

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Data class representing an installed application
 */
data class AppInfo(
    val packageName: String,
    val appName: String,
    val versionName: String,
    val versionCode: Long,
    val isSystemApp: Boolean,
    val isEnabled: Boolean,
    val icon: Drawable?,
    val firstInstallTime: Long,
    val lastUpdateTime: Long
)

/**
 * PackageScanner: Scans and retrieves all installed applications including system apps
 * Uses PackageManager API to enumerate packages efficiently
 */
class PackageScanner(private val context: Context) {

    private val packageManager: PackageManager = context.packageManager

    /**
     * Fetch all installed applications (both user and system apps)
     * Runs on IO dispatcher to avoid blocking UI thread
     */
    suspend fun getAllInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        try {
            val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            packages.mapNotNull { appInfo ->
                try {
                    getAppInfo(appInfo)
                } catch (e: Exception) {
                    Timber.w("Failed to fetch info for ${appInfo.packageName}: ${e.message}")
                    null
                }
            }.sortedBy { it.appName }
        } catch (e: Exception) {
            Timber.e(e, "Failed to fetch installed applications")
            emptyList()
        }
    }

    /**
     * Fetch only user-installed applications (excludes system apps)
     */
    suspend fun getUserInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        getAllInstalledApps().filter { !it.isSystemApp }
    }

    /**
     * Fetch only system applications
     */
    suspend fun getSystemApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        getAllInstalledApps().filter { it.isSystemApp }
    }

    /**
     * Get app info for a specific package
     */
    suspend fun getAppInfoForPackage(packageName: String): AppInfo? = withContext(Dispatchers.IO) {
        try {
            val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            getAppInfo(appInfo)
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.w("Package not found: $packageName")
            null
        }
    }

    /**
     * Convert ApplicationInfo to AppInfo data class
     */
    private fun getAppInfo(appInfo: ApplicationInfo): AppInfo {
        val packageInfo = packageManager.getPackageInfo(appInfo.packageName, 0)
        val isSystemApp = isSystemApplication(appInfo)

        return AppInfo(
            packageName = appInfo.packageName,
            appName = appInfo.loadLabel(packageManager).toString(),
            versionName = packageInfo.versionName ?: "1.0",
            versionCode = packageInfo.longVersionCode,
            isSystemApp = isSystemApp,
            isEnabled = appInfo.enabled,
            icon = appInfo.loadIcon(packageManager),
            firstInstallTime = packageInfo.firstInstallTime,
            lastUpdateTime = packageInfo.lastUpdateTime
        )
    }

    /**
     * Check if the application is a system application
     */
    private fun isSystemApplication(appInfo: ApplicationInfo): Boolean {
        return (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0 ||
                (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
    }

    /**
     * Search apps by name or package name
     */
    suspend fun searchApps(query: String): List<AppInfo> = withContext(Dispatchers.IO) {
        val lowerQuery = query.lowercase()
        getAllInstalledApps().filter { app ->
            app.appName.lowercase().contains(lowerQuery) ||
            app.packageName.lowercase().contains(lowerQuery)
        }
    }
}