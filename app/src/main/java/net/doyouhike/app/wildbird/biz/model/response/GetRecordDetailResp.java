package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.model.bean.LocationEntity;

import java.util.List;

/**
 * Created by zaitu on 15-12-9.
 */
public class GetRecordDetailResp {

    /**
     * time : 1448002549
     * location : {"longitude":"113.998402000","latitude":"22.543733000","location":"广东省深圳市南山区汕头街东e-2栋","altitude":"0.0","cityID":"0"}
     * description :
     * speciesName : 红胸山鹧鸪
     * recordID : 6000736
     * images : [{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/20/7/78bdef104330d17fbf0d6b46b61eb23f.jpeg","imageID":"6656495"},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/20/1/15ed42f041edb5707fec6eac906185cc.jpeg","imageID":"6656496"},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/20/b/ba3904e5bdfacdb596e41a5720411158.jpeg","imageID":"6656497"}]
     * number : 4
     * speciesID : 20
     */

    private long time;

    private LocationEntity location;
    private String description;
    private String speciesName;
    private int recordID;
    private String number;
    private String speciesID;

    private int comment_count;
    private int like_count;
    private int  is_like;// 类型: int, 是否点赞 0未点赞 大于0表示已点赞

    /**
     * imageUrl : http://static.test.doyouhike.net/files/2015/11/20/7/78bdef104330d17fbf0d6b46b61eb23f.jpeg
     * imageID : 6656495
     */

    private List<ImagesEntity> images;

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

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setSpeciesID(String speciesID) {
        this.speciesID = speciesID;
    }

    public void setImages(List<ImagesEntity> images) {
        this.images = images;
    }

    public long getTime() {
        return time;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
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

    public int getRecordID() {
        return recordID;
    }

    public String getNumber() {
        return number;
    }

    public String getSpeciesID() {
        return speciesID;
    }

    public List<ImagesEntity> getImages() {
        return images;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public static class ImagesEntity {
        private String imageUrl;
        private String imageID;

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setImageID(String imageID) {
            this.imageID = imageID;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getImageID() {
            return imageID;
        }
    }
}
