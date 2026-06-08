package com.xsandbox.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xsandbox.data.models.SandboxConfig
import com.xsandbox.ndk.IdentitySpoofEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

/**
 * SandboxViewModel: Manages sandbox creation and lifecycle
 */
@HiltViewModel
class SandboxViewModel @Inject constructor() : ViewModel() {

    private val _sandboxState = MutableStateFlow<SandboxState>(SandboxState.Idle)
    val sandboxState: StateFlow<SandboxState> = _sandboxState

    private val _activeSandboxes = MutableStateFlow<List<SandboxConfig>>(emptyList())
    val activeSandboxes: StateFlow<List<SandboxConfig>> = _activeSandboxes

    init {
        initializeNativeEngine()
    }

    /**
     * Initialize the native spoofing engine
     */
    private fun initializeNativeEngine() {
        viewModelScope.launch {
            try {
                val initialized = IdentitySpoofEngine.initialize()
                if (initialized) {
                    Timber.i("Native identity spoof engine initialized")
                    _sandboxState.value = SandboxState.Ready
                } else {
                    Timber.e("Failed to initialize native engine")
                    _sandboxState.value = SandboxState.Error("Native initialization failed")
                }
            } catch (e: Exception) {
                Timber.e(e, "Exception during native engine initialization")
                _sandboxState.value = SandboxState.Error(e.message ?: "Initialization error")
            }
        }
    }

    /**
     * Create a new sandbox for a cloned app
     */
    fun createSandbox(
        packageName: String,
        appName: String,
        deviceProfile: com.xsandbox.data.models.DeviceProfile
    ) {
        viewModelScope.launch {
            try {
                _sandboxState.value = SandboxState.Creating

                val sandboxId = UUID.randomUUID().toString()
                Timber.i("Creating sandbox: $sandboxId for app: $packageName")

                // Register the spoofed identity with native engine
                IdentitySpoofEngine.registerSpoofedIdentity(
                    sandboxId = sandboxId,
                    androidId = deviceProfile.androidId,
                    model = deviceProfile.model,
                    brand = deviceProfile.brand,
                    manufacturer = deviceProfile.manufacturer,
                    hardware = deviceProfile.hardware,
                    imei = deviceProfile.imei,
                    wifiMac = deviceProfile.wifiMacAddress
                )

                // Apply spoofing
                val applied = IdentitySpoofEngine.applySpoofing(sandboxId)
                if (!applied) {
                    throw Exception("Failed to apply spoofing to sandbox")
                }

                // Create sandbox config
                val sandbox = SandboxConfig(
                    sandboxId = sandboxId,
                    clonedPackageName = packageName,
                    clonedAppName = appName,
                    deviceProfileId = deviceProfile.sandboxId,
                    storagePath = "/data/xsandbox/$sandboxId",
                    cacheDir = "/data/xsandbox/$sandboxId/cache",
                    databaseDir = "/data/xsandbox/$sandboxId/databases",
                    isRunning = true
                )

                // Update active sandboxes
                val updated = _activeSandboxes.value.toMutableList()
                updated.add(sandbox)
                _activeSandboxes.value = updated

                Timber.i("Sandbox created successfully: $sandboxId")
                _sandboxState.value = SandboxState.SandboxCreated(sandbox)
            } catch (e: Exception) {
                Timber.e(e, "Failed to create sandbox")
                _sandboxState.value = SandboxState.Error(e.message ?: "Creation failed")
            }
        }
    }
}

/**
 * Sandbox state sealed class
 */
sealed class SandboxState {
    object Idle : SandboxState()
    object Ready : SandboxState()
    object Creating : SandboxState()
    data class SandboxCreated(val sandbox: SandboxConfig) : SandboxState()
    data class Error(val message: String) : SandboxState()
}
