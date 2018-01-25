package net.doyouhike.app.wildbird.biz.model.bean;

import android.text.TextUtils;

import net.doyouhike.app.wildbird.biz.model.response.GetRecordDetailResp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecordEntity implements Serializable, Comparable<RecordEntity> {

    private static final long serialVersionUID = 1L;


    protected Long record = 0l;// 数据库ID
    protected Integer recordID = 0;// 记录ID
    protected int speciesID = 0;// 鸟种ID
    protected String speciesName = "";// 鸟种名
    protected int number = 0;// 数量
    protected long time = 0;// 时间戳
    protected int hasImage = 0;// 是否有图片 0/1
    private String small_img;//小图地址
    protected Double latitude = 0.000000;// 纬度
    protected Double longitude = 0.000000;// 经度
    protected Double altitude = 0.000000;// 海拔
    protected int cityID = 0;// 城市ID
    protected String cityName = "";// 城市名
    protected String location = "";// 位置信息
    protected String description = "";// 描述
    protected List<RecordImage> list = new ArrayList<RecordImage>();//全部图片列表,包括网络图片,本地选择图片
    private List<Integer> delImages = new ArrayList<Integer>();//要删除的图片列表
    private List<RecordImage> addImages = new ArrayList<>();//要添加的图片列表
    private List<RecordImage> originImage = new ArrayList<>();//要添加的图片列表

    public List<Integer> getDelImages() {
        return delImages;
    }

    public void addDelImage(int imageID) {
        delImages.add(imageID);
    }

    public void setDelImages(List<Integer> delImages) {
        this.delImages = delImages;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RecordImage> getList() {
        return list;
    }

    public void setList(List<RecordImage> list) {
        this.list = list;
    }

    public Long getRecord() {
        return record;
    }

    public void setRecord(Long record) {
        this.record = record;
    }

    public Integer getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getSpeciesID() {
        return speciesID;
    }

    public void setSpeciesID(int speciesID) {
        this.speciesID = speciesID;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getHasImage() {
        return hasImage;
    }

    public void setHasImage(int hasImage) {
        this.hasImage = hasImage;
    }

    public void addUri(RecordImage image) {
        // TODO Auto-generated method stub
        list.add(image);
    }

    public String getSmall_img() {
        return small_img;
    }

    public void setSmall_img(String small_img) {
        this.small_img = small_img;
    }

    public List<RecordImage> getAddImages() {
        return addImages;
    }

    public void setAddImages(List<RecordImage> addImages) {
        this.addImages = addImages;
    }

    public List<RecordImage> getOriginImage() {
        return originImage;
    }

    public void setOriginImage(List<RecordImage> originImage) {

        //复制
        Collections.addAll(this.originImage, new RecordImage[originImage.size()]);
        Collections.copy(this.originImage, originImage);
    }

    public void parseJson(RecordEntity entity, JSONObject response) throws JSONException {
        JSONObject jsonObject = response.getJSONObject("data");
        entity.setRecordID(jsonObject.getInt("recordID"));
        JSONArray array = jsonObject.getJSONArray("images");
        List<RecordImage> list = new ArrayList<RecordImage>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            RecordImage image = new RecordImage();
            image.setImageID(object.getInt("imageID"));
            image.setImageUri(object.getString("imageUrl"));
            list.add(image);
        }
        entity.setList(list);

        JSONObject object2 = jsonObject.getJSONObject("location");
        entity.setLatitude(object2.getDouble("latitude"));
        entity.setLongitude(object2.getDouble("longitude"));
        entity.setAltitude(object2.getDouble("altitude"));
        entity.setCityID(object2.getInt("cityID"));
        entity.setCityName(object2.getString("location").replace("'", ""));
        entity.setLocation(object2.getString("location").replace("'", ""));

        entity.setTime(Long.parseLong(jsonObject.getString("time")));
        entity.setSpeciesID(Integer.parseInt(jsonObject.getString("speciesID")));
        entity.setSpeciesName(jsonObject.getString("speciesName").replace("'", ""));
        entity.setNumber(Integer.parseInt(jsonObject.getString("number")));
        entity.setDescription(jsonObject.getString("description").replace("'", ""));
    }

    public RecordEntity() {
    }

    /**
     * 对应的gson bean类与以前的重复，修改以前的担心出问题，弄了一个转换
     *
     * @param result
     */
    public RecordEntity(GetRecordDetailResp result) {

        setRecordID(result.getRecordID());

        List<GetRecordDetailResp.ImagesEntity> images = result.getImages();

        List<RecordImage> list = new ArrayList<RecordImage>();
        for (GetRecordDetailResp.ImagesEntity img : images) {
            RecordImage image = new RecordImage();
            image.setImageID(Integer.valueOf(img.getImageID()));
            image.setImageUri(img.getImageUrl());
            list.add(image);
        }
        setList(list);
        LocationEntity locationEntity = result.getLocation();

        setLatitude(locationEntity.getLatitude());
        setLongitude(locationEntity.getLongitude());
        setAltitude(locationEntity.getAltitude());

        setCityID(locationEntity.getCityID());
        setCityName("");
        setLocation(locationEntity.getLocation());

        setTime(result.getTime());
        setSpeciesID(Integer.parseInt(result.getSpeciesID()));
        setSpeciesName(result.getSpeciesName());
        setNumber(Integer.parseInt(result.getNumber()));
        setDescription(result.getDescription());

    }


    /**
     * 重置新增图片
     */
    public void resetAddImgItems() {
        addImages.clear();


        for (RecordImage image : list) {

            if (image.getImageID() <= 0) {
                continue;
            }

            if (!TextUtils.isEmpty(image.getImageUri())
                    && image.getImageUri().startsWith("http")) {
                continue;
            }

            addImages.add(image);
        }


    }



    /**
     * 设置删除图片
     */
    public void resetDeleteImgItems() {
        delImages.clear();

        if (originImage == null || originImage.isEmpty()) {
            return;
        }


        for (RecordImage originImg : originImage) {

            if (originImg.getImageID() <= 0) {
                continue;
            }


            if (list.contains(originImg)) {
                continue;
            }

            delImages.add(originImg.getImageID());
        }


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordEntity that = (RecordEntity) o;

        return recordID.equals(that.recordID);
    }

    @Override
    public int hashCode() {
        return recordID.hashCode();
    }

    @Override
    public int compareTo(RecordEntity another) {

        if (this.time == another.getTime()) {
            return 0;
        } else if (this.time > another.getTime()) {
            return 1;
        }
        return -1;
    }
}
