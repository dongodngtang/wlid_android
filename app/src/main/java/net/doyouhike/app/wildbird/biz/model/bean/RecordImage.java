package net.doyouhike.app.wildbird.biz.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecordImage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Expose
    @SerializedName("photoID")
    protected Integer imageID = 0;// 图片的ID
    protected String imageUri = "";// 图片的url地址
    private String uploadImageId = "";//上传图片ID

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUploadImageId() {
        return uploadImageId;
    }

    public void setUploadImageId(String uploadImageId) {
        this.uploadImageId = uploadImageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordImage image = (RecordImage) o;

        if (imageID != null ? !imageID.equals(image.imageID) : image.imageID != null) return false;
        return imageUri != null ? imageUri.equals(image.imageUri) : image.imageUri == null;

    }

    @Override
    public int hashCode() {
        int result = imageID != null ? imageID.hashCode() : 0;
        result = 31 * result + (imageUri != null ? imageUri.hashCode() : 0);
        return result;
    }
}
