package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * Created by zaitu on 15-12-4.
 */
public class MineRecordsEntity {
    /**
     * hasImage : 0
     * time : 1449216965
     * location : 广东省深圳市南山区汕头街东e-2栋
     * speciesName : 白颊山鹧鸪
     * recordID : 6001303
     * cityID : null
     * number : 6
     * speciesID : 18
     */
    private int hasImage;
    private long time;
    private String location;
    private String speciesName;
    private String recordID;
    private Object cityID;
    private int number;
    private int speciesID;

    public void setHasImage(int hasImage) {
        this.hasImage = hasImage;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }

    public void setCityID(Object cityID) {
        this.cityID = cityID;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setSpeciesID(int speciesID) {
        this.speciesID = speciesID;
    }

    public int getHasImage() {
        return hasImage;
    }

    public long getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public String getRecordID() {
        return recordID;
    }

    public Object getCityID() {
        return cityID;
    }

    public int getNumber() {
        return number;
    }

    public int getSpeciesID() {
        return speciesID;
    }
}
