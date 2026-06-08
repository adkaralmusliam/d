package com.xsandbox.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xsandbox.core.AppInfo
import com.xsandbox.core.PackageScanner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * AppListViewModel: Manages app list state and interactions
 * Handles fetching, filtering, and searching of installed apps
 */
@HiltViewModel
class AppListViewModel @Inject constructor(
    private val context: Context
) : ViewModel() {

    private val packageScanner = PackageScanner(context)

    // UI State
    private val _appListState = MutableStateFlow<AppListState>(AppListState.Loading)
    val appListState: StateFlow<AppListState> = _appListState

    private val _selectedApps = MutableStateFlow<Set<String>>(emptySet())
    val selectedApps: StateFlow<Set<String>> = _selectedApps

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _filterSystemApps = MutableStateFlow(true)  // Show system apps by default
    val filterSystemApps: StateFlow<Boolean> = _filterSystemApps

    init {
        loadAllApps()
    }

    /**
     * Load all installed applications
     */
    fun loadAllApps() {
        viewModelScope.launch {
            _appListState.value = AppListState.Loading
            try {
                val apps = packageScanner.getAllInstalledApps()
                Timber.i("Loaded ${apps.size} applications")
                _appListState.value = AppListState.Success(apps)
            } catch (e: Exception) {
                Timber.e(e, "Failed to load apps")
                _appListState.value = AppListState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Toggle system app filter
     */
    fun toggleSystemAppFilter() {
        _filterSystemApps.value = !_filterSystemApps.value
        performSearch(_searchQuery.value)
    }

    /**
     * Search apps by query
     */
    fun searchApps(query: String) {
        _searchQuery.value = query
        performSearch(query)
    }

    /**
     * Perform filtered search
     */
    private fun performSearch(query: String) {
        viewModelScope.launch {
            try {
                val results = if (query.isEmpty()) {
                    packageScanner.getAllInstalledApps()
                } else {
                    packageScanner.searchApps(query)
                }

                val filtered = if (_filterSystemApps.value) {
                    results.filter { !it.isSystemApp }
                } else {
                    results
                }

                _appListState.value = AppListState.Success(filtered)
            } catch (e: Exception) {
                Timber.e(e, "Search failed")
                _appListState.value = AppListState.Error(e.message ?: "Search failed")
            }
        }
    }

    /**
     * Toggle app selection
     */
    fun toggleAppSelection(packageName: String) {
        val current = _selectedApps.value.toMutableSet()
        if (current.contains(packageName)) {
            current.remove(packageName)
        } else {
            current.add(packageName)
        }
        _selectedApps.value = current
        Timber.d("App selection toggled: $packageName, selected count: ${current.size}")
    }

    /**
     * Clear all selections
     */
    fun clearSelections() {
        _selectedApps.value = emptySet()
    }

    /**
     * Get selected apps
     */
    fun getSelectedApps(): Set<String> = _selectedApps.value
}

/**
 * UI State sealed class for app list
 */
sealed class AppListState {
    object Loading : AppListState()
    data class Success(val apps: List<AppInfo>) : AppListState()
    data class Error(val message: String) : AppListState()
}
