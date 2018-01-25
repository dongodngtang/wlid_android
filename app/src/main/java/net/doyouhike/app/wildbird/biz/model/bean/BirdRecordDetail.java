package net.doyouhike.app.wildbird.biz.model.bean;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-12.
 */
public class BirdRecordDetail {

//
//    {
//        "recordID":"xxx",
//            "images":[
//        {
//            "imageID":"xxx",
//                "imageUrl":  "xxx"
//        }
//        ],
//        "location":
//        {
//            "latitude":"",
//                "longitude":"",
//                "altitude":"",
//                "cityID":"",
//                "cityName":"",
//                "location":"深圳蛇口码头"
//        },
//        "time":5456431312,
//            "speciesID":"xxx",
//            "speciesName": "xxx",
//            "number":5,
//            "description":"xxx" }


    /**
     * recordID : xxx
     * images : [{"imageID":"xxx","imageUrl":"xxx"}]
     * location : {"latitude":"","longitude":"","altitude":"","cityID":"","cityName":"","location":"深圳蛇口码头"}
     * time : 5456431312
     * speciesID : xxx
     * speciesName : xxx
     * number : 5
     * description : xxx
     */

    private String recordID;
    /**
     * latitude :
     * longitude :
     * altitude :
     * cityID :
     * cityName :
     * location : 深圳蛇口码头
     */

    private LocationEntity location;
    private long time;
    private String speciesID;
    private String speciesName;
    private int number;
    private String description;
    /**
     * imageID : xxx
     * imageUrl : xxx
     */

    private List<ImagesEntity> images;

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setSpeciesID(String speciesID) {
        this.speciesID = speciesID;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImages(List<ImagesEntity> images) {
        this.images = images;
    }

    public String getRecordID() {
        return recordID;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public long getTime() {
        return time;
    }

    public String getSpeciesID() {
        return speciesID;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public List<ImagesEntity> getImages() {
        return images;
    }

    public static class LocationEntity {
        private String latitude;
        private String longitude;
        private String altitude;
        private String cityID;
        private String cityName;
        private String location;

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public void setAltitude(String altitude) {
            this.altitude = altitude;
        }

        public void setCityID(String cityID) {
            this.cityID = cityID;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getAltitude() {
            return altitude;
        }

        public String getCityID() {
            return cityID;
        }

        public String getCityName() {
            return cityName;
        }

        public String getLocation() {
            return location;
        }
    }

    public static class ImagesEntity {
        private String imageID;
        private String imageUrl;

        public void setImageID(String imageID) {
            this.imageID = imageID;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getImageID() {
            return imageID;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
