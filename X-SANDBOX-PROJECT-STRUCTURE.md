# X-Sandbox Project Structure Blueprint

## Complete Directory Tree

```
x-sandbox/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── kotlin/
│   │   │   │   └── com/
│   │   │   │       └── xsandbox/
│   │   │   │           ├── MainActivity.kt
│   │   │   │           ├── data/
│   │   │   │           │   ├── models/
│   │   │   │           │   │   ├── AppInfo.kt
│   │   │   │           │   │   ├── DeviceProfile.kt
│   │   │   │           │   │   └── SandboxConfig.kt
│   │   │   │           │   ├── repository/
│   │   │   │           │   │   ├── AppRepository.kt
│   │   │   │           │   │   ├── SandboxRepository.kt
│   │   │   │           │   │   └── DeviceProfileRepository.kt
│   │   │   │           │   └── local/
│   │   │   │           │       ├── AppDatabase.kt
│   │   │   │           │       └── dao/
│   │   │   │           │           ├── AppInfoDao.kt
│   │   │   │           │           └── SandboxDao.kt
│   │   │   │           ├── ui/
│   │   │   │           │   ├── screens/
│   │   │   │           │   │   ├── HomeScreen.kt
│   │   │   │           │   │   ├── AppListScreen.kt
│   │   │   │           │   │   ├── SandboxScreen.kt
│   │   │   │           │   │   ├── DeviceProfileScreen.kt
│   │   │   │           │   │   └── SettingsScreen.kt
│   │   │   │           │   ├── components/
│   │   │   │           │   │   ├── AppListItem.kt
│   │   │   │           │   │   ├── SandboxCard.kt
│   │   │   │           │   │   └── DeviceProfileCard.kt
│   │   │   │           │   ├── theme/
│   │   │   │           │   │   ├── Color.kt
│   │   │   │           │   │   ├── Typography.kt
│   │   │   │           │   │   └── Theme.kt
│   │   │   │           │   └── viewmodels/
│   │   │   │           │       ├── AppListViewModel.kt
│   │   │   │           │       ├── SandboxViewModel.kt
│   │   │   │           │       └── DeviceProfileViewModel.kt
│   │   │   │           ├── ndk/
│   │   │   │           │   ├── IdentitySpoofEngine.kt
│   │   │   │           │   ├── NativeLibLoader.kt
│   │   │   │           │   └── JNIBindings.kt
│   │   │   │           ├── core/
│   │   │   │           │   ├── PackageScanner.kt
│   │   │   │           │   ├── SandboxManager.kt
│   │   │   │           │   └── PermissionHandler.kt
│   │   │   │           └── utils/
│   │   │   │               ├── Logger.kt
│   │   │   │               ├── FileUtils.kt
│   │   │   │               └── Constants.kt
│   │   │   └── res/
│   │   │       ├── drawable/
│   │   │       ├── layout/
│   │   │       ├── values/
│   │   │       │   └── strings.xml
│   │   │       └── values-ar/
│   │   │           └── strings.xml
│   ├── src/
│   │   ├── main/
│   │   │   └── cpp/
│   │   │       ├── CMakeLists.txt
│   │   │       ├── identity_spoof/
│   │   │       │   ├── identity_spoof.cpp
│   │   │       │   ├── identity_spoof.h
│   │   │       │   ├── device_properties.cpp
│   │   │       │   ├── device_properties.h
│   │   │       │   ├── native_hooks.cpp
│   │   │       │   ├── native_hooks.h
│   │   │       │   ├── jni_bindings.cpp
│   │   │       │   └── jni_bindings.h
│   │   │       └── utils/
│   │   │           ├── logger.cpp
│   │   │           ├── logger.h
│   │   │           ├── string_utils.cpp
│   │   │           └── string_utils.h
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── local.properties
└── gradle/
    └── wrapper/
        └── gradle-wrapper.properties
```

## Architecture Layers

### 1. **Presentation Layer (UI - Jetpack Compose)**
   - Screens for app listing, sandbox management, device profile configuration
   - Modern, minimalist design with RTL support

### 2. **Domain Layer (ViewModels & Use Cases)**
   - Business logic for app cloning, sandbox isolation
   - Device profile management

### 3. **Data Layer (Repository Pattern)**
   - Local database (Room) for caching app info and sandbox configs
   - Package scanner integration

### 4. **Core Layer (System Integration)**
   - PackageManager integration
   - DevicePolicyManager for enterprise profile (optional)
   - Native bridge (JNI)

### 5. **NDK Layer (C/C++ - Low-Level Spoofing)**
   - System call hooking / reflection interception
   - Device property injection
   - Identity spoofing engine

---

## Key Responsibilities per Layer

| Layer | Responsibility |
|-------|-----------------|
| UI (Compose) | Display apps, manage sandbox creation, show device profiles |
| ViewModel | Manage UI state, coordinate between UI and domain |
| Repository | Fetch/cache app data, sandbox configs |
| PackageScanner | Enumerate installed apps including system apps |
| NDK (C/C++) | Hook system calls, inject spoofed identities |

