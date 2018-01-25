package net.doyouhike.app.wildbird.biz.service.net.uploadrecord;

import net.doyouhike.app.wildbird.biz.model.response.UploadImageResponse;

/**
 * 图片上传监听
 * Created by zengjiang on 16/6/6.
 */
public interface UploadImageListener {

    String getUploadId();

    void onUploadSuccess(UploadImageResponse response);
}
