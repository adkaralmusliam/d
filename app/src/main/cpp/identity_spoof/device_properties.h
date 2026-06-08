#ifndef DEVICE_PROPERTIES_H
#define DEVICE_PROPERTIES_H

#include <string>

namespace xsandbox {
namespace device {

/**
 * DeviceProperties: Low-level device property getter/setter
 * Provides methods to spoof Android device properties at the system level
 */
class DeviceProperties {
public:
    /**
     * Get Android ID (Settings.Secure.ANDROID_ID)
     */
    static std::string getAndroidId();

    /**
     * Set spoofed Android ID
     */
    static bool setAndroidId(const std::string& spoofedId);

    /**
     * Get device model
     */
    static std::string getDeviceModel();

    /**
     * Set spoofed device model
     */
    static bool setDeviceModel(const std::string& model);

    /**
     * Get device brand
     */
    static std::string getDeviceBrand();

    /**
     * Set spoofed device brand
     */
    static bool setDeviceBrand(const std::string& brand);

    /**
     * Get device manufacturer
     */
    static std::string getDeviceManufacturer();

    /**
     * Set spoofed manufacturer
     */
    static bool setDeviceManufacturer(const std::string& manufacturer);

    /**
     * Get hardware identifier
     */
    static std::string getHardware();

    /**
     * Set spoofed hardware
     */
    static bool setHardware(const std::string& hardware);

    /**
     * Get build fingerprint
     */
    static std::string getBuildFingerprint();

    /**
     * Set spoofed build fingerprint
     */
    static bool setBuildFingerprint(const std::string& fingerprint);

private:
    DeviceProperties();  // Static class, no instantiation
};

}  // namespace device
}  // namespace xsandbox

#endif  // DEVICE_PROPERTIES_H