package net.doyouhike.app.wildbird.biz.model.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by zaitu on 15-12-4.
 */
public class LocationEntity {

    /**
     * longitude : 113.998402000
     * latitude : 22.543733000
     * location : 广东省深圳市南山区汕头街东e-2栋
     * altitude : 0.0
     * cityID : 0
     */
    @Expose
    private double longitude;
    @Expose
    private double latitude;
    @Expose
    private String location;
    @Expose
    private double altitude;
    @Expose
    private int cityID;

    private String cityName;

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getLocation() {
        return location;
    }

    public double getAltitude() {
        return altitude;
    }

    public int getCityID() {
        return cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "LocationEntity{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", location='" + location + '\'' +
                ", altitude=" + altitude +
                ", cityID=" + cityID +
                '}';
    }
}
