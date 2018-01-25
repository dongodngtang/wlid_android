package net.doyouhike.app.wildbird.biz.model.request.get;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;

/**
 * Created by zaitu on 15-12-4.
 */
public class GetRecordStats extends BaseListGetParam {


    /**
     * month : 12
     * longitude : 113.99853
     * latitude : 22.543613
     * range : 100
     */

    private int month;
    private String longitude;
    private String latitude;
    private String city="";
    private String range; // 类型: String, location 范围

    @Override
    protected void setMapValue() {
        super.setMapValue();
        map.put("month", month + "");
        map.put("longitude",longitude);
        map.put("latitude",latitude);
        map.put("range",range+"");
        map.put("city",city);
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public int getMonth() {
        return month;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getRange() {
        return range;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
