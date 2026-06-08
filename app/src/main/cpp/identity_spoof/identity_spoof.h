#ifndef IDENTITY_SPOOF_H
#define IDENTITY_SPOOF_H

#include <string>
#include <map>
#include <mutex>

namespace xsandbox {
namespace identity {

/**
 * SpoofedIdentity: Represents a spoofed device identity configuration
 */
struct SpoofedIdentity {
    std::string androidId;
    std::string deviceModel;
    std::string deviceBrand;
    std::string deviceManufacturer;
    std::string hardware;
    std::string imei;                // Can be stubbed
    std::string wifiMacAddress;      // Spoofed MAC
    std::string buildFingerprint;
    std::string buildSerial;
};

/**
 * IdentitySpoofEngine: Main engine for managing and applying device identity spoofing
 * Provides methods to register, retrieve, and apply spoofed identities per process/sandbox
 */
class IdentitySpoofEngine {
public:
    static IdentitySpoofEngine& getInstance();

    /**
     * Initialize the spoof engine and set up hooking mechanisms
     */
    bool initialize();

    /**
     * Register a spoofed identity for a specific sandbox context
     */
    void registerSpoofedIdentity(const std::string& sandboxId, const SpoofedIdentity& identity);

    /**
     * Retrieve registered spoofed identity for a sandbox
     */
    bool getSpoofedIdentity(const std::string& sandboxId, SpoofedIdentity& outIdentity);

    /**
     * Remove a registered spoofed identity
     */
    void removeSpoofedIdentity(const std::string& sandboxId);

    /**
     * Apply identity spoofing to current process context
     */
    bool applySpoofing(const std::string& sandboxId);

    /**
     * Get the current active spoofed identity (if any)
     */
    const SpoofedIdentity* getCurrentSpoofedIdentity() const;

private:
    IdentitySpoofEngine();
    ~IdentitySpoofEngine();

    IdentitySpoofEngine(const IdentitySpoofEngine&) = delete;
    IdentitySpoofEngine& operator=(const IdentitySpoofEngine&) = delete;

    // Singleton instance
    static IdentitySpoofEngine* instance_;
    static std::mutex instanceMutex_;

    // Storage for registered identities
    std::map<std::string, SpoofedIdentity> identities_;
    mutable std::mutex identitiesMutex_;

    // Current active identity
    SpoofedIdentity* currentIdentity_;
};

}  // namespace identity
}  // namespace xsandbox

#endif  // IDENTITY_SPOOF_H