package com.ons.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.annotation.PublicSDK;

/**
 *  ONS Automatic Data Collection related configuration.
 */
@PublicSDK
public class ONSDataCollectionConfig {

    /**
     * Editor interface to edit the ONSDataCollectionConfig
     */
    @PublicSDK
    public interface Editor {
        void edit(ONSDataCollectionConfig config);
    }

    /**
     * Whether ONS should resolve the user's region/location from the ip address.
     * Default: false
     */
    @Nullable
    private Boolean geoIPEnabled;

    /**
     * Whether ONS should send the device brand information.
     * Default: false
     */
    @Nullable
    private Boolean deviceBrandEnabled;

    /**
     * Whether ONS should send the device model information.
     * Default: false
     */
    @Nullable
    private Boolean deviceModelEnabled;

    /**
     * Set whether ONS can resolve the user's region/location from the ip address.
     *
     * @param geoIPEnabled Whether ONS can resolve the geoip.
     * @return This ONSDataCollectionConfig instance for method chaining
     */
    public ONSDataCollectionConfig setGeoIPEnabled(boolean geoIPEnabled) {
        this.geoIPEnabled = geoIPEnabled;
        return this;
    }

    /**
     * Set whether ONS should send the device brand information.
     *
     * @param deviceBrandEnabled Whether ONS can collect the device brand.
     * @return This ONSDataCollectionConfig instance for method chaining
     */
    public ONSDataCollectionConfig setDeviceBrandEnabled(boolean deviceBrandEnabled) {
        this.deviceBrandEnabled = deviceBrandEnabled;
        return this;
    }

    /**
     * Set whether ONS should send the device model information.
     *
     * @param deviceModelEnabled Whether ONS can collect the device model.
     * @return This ONSDataCollectionConfig instance for method chaining
     */
    public ONSDataCollectionConfig setDeviceModelEnabled(boolean deviceModelEnabled) {
        this.deviceModelEnabled = deviceModelEnabled;
        return this;
    }

    /**
     * Get whether the geoip is enabled to resolve the user's location/region on server side.
     *
     * @return whether the geoip is enabled to resolve the user's location/region.
     */
    @Nullable
    public Boolean isGeoIpEnabled() {
        return geoIPEnabled;
    }

    /**
     * Get whether the device brand collect is enabled (null mean unchanged from last modification or default value : false).
     *
     * @return Whether the device brand collect is enabled t.
     */
    @Nullable
    public Boolean isDeviceBrandEnabled() {
        return deviceBrandEnabled;
    }

    /**
     * Get whether the device model collect is enabled (null mean unchanged from last modification or default value : false).
     *
     * @return Whether the device model collect is enabled.
     */
    @Nullable
    public Boolean isDeviceModelEnabled() {
        return deviceModelEnabled;
    }

    /**
     * To String method
     * @return A string representation of the data collection config.
     */
    @NonNull
    @Override
    public String toString() {
        return (
            "ONSDataCollectionConfig{" +
            "geoIPEnabled=" +
            geoIPEnabled +
            ", deviceBrandEnabled=" +
            deviceBrandEnabled +
            ", deviceModelEnabled=" +
            deviceModelEnabled +
            '}'
        );
    }
}
