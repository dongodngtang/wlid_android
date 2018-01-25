package net.doyouhike.app.wildbird.biz.model.bean;

import java.io.Serializable;

/**
 * 功能：观鸟记录列表item
 *
 * @author：曾江 日期：16-4-12.
 */
public class BirdRecordDetailItem implements Serializable {

    private int record_id;  // 类型: int, 记录id
    private String user_id;  // 类型: int, 用户id
    private String user_name; // 类型: string, 用户名
    private String avatar;  // 类型: int, 用户头像
    private int city_id; // 类型: int, 城市id
    private int bird_num; // 类型: int, 观鸟数量
    private long rec_time;  //观鸟日期,unix时间戳
    private int has_image;  // 是否有图片 0无图片 1有图片
    private String small_img;//小图地址
    private String species_name;//鸟名字
    private int species_id;//鸟id

    public BirdRecordDetailItem() {
    }

    public BirdRecordDetailItem(MyRecord record) {
        setRecord_id(record.getRecord().getRecordID());
        setBird_num(record.getRecord().getNumber());
        setRec_time(record.getRecord().getTime());
    }

    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getBird_num() {
        return bird_num;
    }

    public void setBird_num(int bird_num) {
        this.bird_num = bird_num;
    }

    public long getRec_time() {
        return rec_time;
    }

    public void setRec_time(long rec_time) {
        this.rec_time = rec_time;
    }

    public int getHas_image() {
        return has_image;
    }

    public void setHas_image(int has_image) {
        this.has_image = has_image;
    }

    public String getSmall_img() {
        return small_img;
    }

    public void setSmall_img(String small_img) {
        this.small_img = small_img;
    }

    public String getSpecies_name() {
        return species_name;
    }

    public void setSpecies_name(String species_name) {
        this.species_name = species_name;
    }

    public int getSpecies_id() {
        return species_id;
    }

    public void setSpecies_id(int species_id) {
        this.species_id = species_id;
    }

    public RecordEntity toRecordEntity() {
        RecordEntity entity = new RecordEntity();

        entity.setRecordID(record_id);
        entity.setCityID(city_id);
        entity.setNumber(bird_num);
        entity.setTime(rec_time);

        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BirdRecordDetailItem that = (BirdRecordDetailItem) o;

        return record_id == that.record_id;

    }

    @Override
    public int hashCode() {
        return record_id;
    }
}
