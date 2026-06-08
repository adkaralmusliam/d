#ifndef NATIVE_HOOKS_H
#define NATIVE_HOOKS_H

#include <jni.h>

namespace xsandbox {
namespace hooks {

/**
 * NativeHooks: System call hooking mechanism
 * Intercepts system calls to inject spoofed device properties
 */
class NativeHooks {
public:
    /**
     * Initialize hooking mechanism
     * This may use PLT hooking, Frida, or similar techniques
     */
    static bool initializeHooks();

    /**
     * Hook into specific system functions (e.g., getprop, property_get)
     */
    static bool hookSystemCalls();

    /**
     * Intercept JNI calls to system properties
     */
    static bool hookJNIProperties();

    /**
     * Clean up and remove all hooks
     */
    static void cleanupHooks();

private:
    NativeHooks();  // Static class
    static bool hooksInitialized_;
};

}  // namespace hooks
}  // namespace xsandbox

#endif  // NATIVE_HOOKS_H