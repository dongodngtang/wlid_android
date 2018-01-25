package net.doyouhike.app.wildbird.biz.model.response;

/**
 * Created by zengjiang on 16/6/3.
 */
public class UploadImageResponse {

    /**
     * ret : 0
     * data : {"imgURL":"http://static.test.doyouhike.net/files/2015/10/26/4/4dc936aa94bf562fd7380d7b15c66d20_m.jpg","photoID":1702470}
     * log : success
     * code : 100000
     */

    private int ret;
    /**
     * imgURL : http://static.test.doyouhike.net/files/2015/10/26/4/4dc936aa94bf562fd7380d7b15c66d20_m.jpg
     * photoID : 1702470
     */

    private DataBean data;
    private String log;
    private String code;

    private String uploadId;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class DataBean {
        private String imgURL;
        private int photoID;

        public String getImgURL() {
            return imgURL;
        }

        public void setImgURL(String imgURL) {
            this.imgURL = imgURL;
        }

        public int getPhotoID() {
            return photoID;
        }

        public void setPhotoID(int photoID) {
            this.photoID = photoID;
        }
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
}
