package net.doyouhike.app.wildbird.biz.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zaitu on 15-12-3.
 */
public class CheckDataUpdSucRepo {

    /**
     * needUpdate : 1
     * curDataVer : 5.3
     * downUrl : http://static.test.doyouhike.net/files/bird/offline_package-20151118.zip
     * md5 : 639923d015b419cee7c885e7dd25a4f5
     */
    @SerializedName("needUpdate")
    private int needUpdate;
    @SerializedName("curDataVer")
    private String curDataVer;
    @SerializedName("downUrl")
    private String downUrl;
    @SerializedName("md5")
    private String md5;

    public void setNeedUpdate(int needUpdate) {
        this.needUpdate = needUpdate;
    }

    public void setCurDataVer(String curDataVer) {
        this.curDataVer = curDataVer;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getNeedUpdate() {
        return needUpdate;
    }

    public String getCurDataVer() {
        return curDataVer;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public String getMd5() {
        return md5;
    }

    @Override
    public String toString() {
        return "CheckDataUpdSucRepo{" +
                "needUpdate=" + needUpdate +
                ", curDataVer='" + curDataVer + '\'' +
                ", downUrl='" + downUrl + '\'' +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
