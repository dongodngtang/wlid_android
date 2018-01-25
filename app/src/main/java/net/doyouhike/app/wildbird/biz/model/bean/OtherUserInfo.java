package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-15.
 */
public class OtherUserInfo {

    /**
     * UserID : 1508665
     * UserName : 图库-时间
     * avatar : http://dev.static.doyouhike.dev/files/faces/none_header.gif
     * recordNum : 0
     * speciesNum : 0
     * thisYearRecordNum : 0
     * thisYearSpeciesNum : 0
     * share_url :
     */

    private String UserID;
    private String UserName;
    private String avatar;
    private String recordNum;
    private String speciesNum;
    private String thisYearRecordNum;
    private String thisYearSpeciesNum;
    private String share_url;

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getUserID() {
        return UserID;
    }

    public String getUserName() {
        return UserName;
    }

    public String getAvatar() {
        return avatar;
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

    public String getShare_url() {
        return share_url;
    }
}
