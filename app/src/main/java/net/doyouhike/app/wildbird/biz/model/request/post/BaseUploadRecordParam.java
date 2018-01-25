package net.doyouhike.app.wildbird.biz.model.request.post;

import com.google.gson.annotations.Expose;

import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;
import net.doyouhike.app.wildbird.biz.model.bean.LocationEntity;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.biz.model.bean.RecordImage;

import java.util.List;

/**
 * Created by zaitu on 15-12-4.
 */
public class BaseUploadRecordParam extends BasePostRequest {

    /**
     * images : []
     * number : 3
     * time : 1449210387
     * location : {"longitude":113.998495,"latitude":22.543602,"location":"广东省深圳市南山区汕头街东e-2栋","altitude":22.543602,"cityID":340}
     * description : 到
     * speciesName : 白颊山鹧鸪
     * speciesID : 18
     */

    @Expose
    private int number;
    @Expose
    private long time;
    /**
     * longitude : 113.998495
     * latitude : 22.543602
     * location : 广东省深圳市南山区汕头街东e-2栋
     * altitude : 22.543602
     * cityID : 340
     */

    @Expose
    private LocationEntity location;
    @Expose
    private String description;
    @Expose
    private String speciesName;
    @Expose
    private int speciesID;
    @Expose
    private List<RecordImage> images;

    public BaseUploadRecordParam() {
    }

    public BaseUploadRecordParam(RecordEntity entity) {
        setContent(entity);
    }


    public void setNumber(int number) {
        this.number = number;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public void setSpeciesID(int speciesID) {
        this.speciesID = speciesID;
    }

    public void setImages(List<RecordImage> images) {
        this.images = images;
    }

    public int getNumber() {
        return number;
    }

    public long getTime() {
        return time;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public int getSpeciesID() {
        return speciesID;
    }

    public List<RecordImage> getImages() {
        return images;
    }


    private void setContent(RecordEntity entity) {
        setNumber(entity.getNumber());
        setTime(entity.getTime());

        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setAltitude(entity.getAltitude());
        locationEntity.setCityID(entity.getCityID());
        locationEntity.setLongitude(entity.getLongitude());
        locationEntity.setLatitude(entity.getLatitude());
        locationEntity.setLocation(entity.getLocation());

        setLocation(locationEntity);
        setDescription(entity.getDescription());
        setSpeciesName(entity.getSpeciesName());
        setSpeciesID(entity.getSpeciesID());

        setImages(entity.getAddImages());

    }
}
