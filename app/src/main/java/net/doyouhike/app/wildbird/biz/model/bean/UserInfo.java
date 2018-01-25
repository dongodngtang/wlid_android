package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * Created by zaitu on 15-12-7.
 */
public class UserInfo {
    /**
     * userId : 1213026
     * userName : 曾江
     * sex : 1
     * recordNum : 4
     * speciesNum : 3
     * thisYearRecordNum : 4
     * thisYearSpeciesNum : 3
     * avatar : http://static.test.doyouhike.net/files/faces/b/3/b30e0a9b8.jpg
     */

    private String userId;
    private String userName;
    private int sex;
    private String recordNum;
    private String speciesNum;
    private String thisYearRecordNum;
    private String thisYearSpeciesNum;
    private String avatar;
    private String share_url;
    private String area_rank_share_url;
    private String person_rank_share_url;
    private int city_id; //用户所在城市id
    private String insitution; //所属机构
    private String camera; //相机 多个用英文逗号隔开
    private String telescope; //望远镜 多个用英文逗号隔开

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
    }

    public void setSpeciesNum(String speciesNum) {
        this.speciesNum = speciesNum;
    }

    public void setThisYearRecordNum(String thisYearRecordNum) {
        this.thisYearRecordNum = thisYearRecordNum;
    }

    public void setThisYearSpeciesNum(String thisYearSpeciesNum) {
        this.thisYearSpeciesNum = thisYearSpeciesNum;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getSex() {
        return sex;
    }

    public String getRecordNum() {
        return recordNum;
    }

    public String getSpeciesNum() {
        return speciesNum;
    }

    public String getThisYearRecordNum() {
        return thisYearRecordNum;
    }

    public String getThisYearSpeciesNum() {
        return thisYearSpeciesNum;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getArea_rank_share_url() {
        return area_rank_share_url;
    }

    public void setArea_rank_share_url(String area_rank_share_url) {
        this.area_rank_share_url = area_rank_share_url;
    }

    public String getPerson_rank_share_url() {
        return person_rank_share_url;
    }

    public void setPerson_rank_share_url(String person_rank_share_url) {
        this.person_rank_share_url = person_rank_share_url;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getInsitution() {
        return insitution;
    }

    public void setInsitution(String insitution) {
        this.insitution = insitution;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getTelescope() {
        return telescope;
    }

    public void setTelescope(String telescope) {
        this.telescope = telescope;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", sex=" + sex +
                ", recordNum='" + recordNum + '\'' +
                ", speciesNum='" + speciesNum + '\'' +
                ", thisYearRecordNum='" + thisYearRecordNum + '\'' +
                ", thisYearSpeciesNum='" + thisYearSpeciesNum + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
