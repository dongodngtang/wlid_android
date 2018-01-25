package net.doyouhike.app.wildbird.biz.service.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.biz.model.bean.RecordImage;
import net.doyouhike.app.wildbird.biz.model.event.EventUploadRecord;
import net.doyouhike.app.wildbird.biz.model.event.RemoveUploadTaskEvent;
import net.doyouhike.app.wildbird.biz.model.request.post.ModifyRecordParam;
import net.doyouhike.app.wildbird.biz.model.request.post.PublishRecordParam;
import net.doyouhike.app.wildbird.biz.model.response.UploadImageResponse;
import net.doyouhike.app.wildbird.biz.model.response.UploadRecordResp;
import net.doyouhike.app.wildbird.biz.service.net.uploadrecord.UploadImageListener;
import net.doyouhike.app.wildbird.util.BitmapUtil;
import net.doyouhike.app.wildbird.util.ImageUtil;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * 上传纪录
 * Created by zaitu on 15-12-9.
 */
public class UploadRecordService implements UploadImageListener {

    //上传网址
    private final String UPLOAD_URL = Content.WB_URL + Content.REQ_uploadImage;

    //要上传的纪录
    private RecordEntity entity;
    private Context context;
    private String mUploadId;

    public UploadRecordService(Context context, RecordEntity entity) {
        this.context = context;
        this.entity = entity;
    }

    public void onDestroy() {
        context = null;
    }


    public void saveToNet() {
        onUploadSuccess(null);
    }

    /**
     * 上传图片
     *
     * @param url 图片地址
     * @return 上传id
     */
    private String uploadImage(final String url) {

        String picUrl = url;


        if (url.startsWith("file://")) {
            picUrl = url.replace("file://", "");
        }
        String newPicUrl = "";
        try {
            newPicUrl = ImageUtil.getSmallImagePath(picUrl);
        } catch (IOException e) {
            e.printStackTrace();
            newPicUrl = picUrl;
        }

        try {
            return new MultipartUploadRequest(context, UPLOAD_URL)
                    .addFileToUpload(newPicUrl, "file")
                    .addHeader(Content.NET_HEADER_TOKEN, UserInfoSpUtil.getInstance().getToken())
                    .setMaxRetries(2)
                    .startUpload();
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return "";
    }


    public void publishBirdRecord() {


        int recordId = entity.getRecordID();
        entity.resetAddImgItems();
        entity.resetDeleteImgItems();

        BasePostRequest param;

        String tag;
        if (recordId > 0) {
            param = new ModifyRecordParam(entity);
            tag = Content.REQ_modifyBirdRecord;
        } else {
            param = new PublishRecordParam(entity);
            tag = Content.REQ_publishBirdRecord;

        }

        ApiReq.uploadRecord(param, tag, uploadRecordSuc, uploadRecordErr);

    }

    public void cancel() {
        RequestUtil.getInstance().cancelAllRequests(Content.REQ_publishBirdRecord);
        RequestUtil.getInstance().cancelAllRequests(Content.REQ_modifyBirdRecord);
        UploadService.stopAllUploads();
    }

    //      上传个人观鸟记录统计失败回调
    private Response.ErrorListener uploadRecordErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            String errMsg = error.getMessage();

            if (errMsg == null || errMsg.isEmpty() || errMsg.contains("网络不佳")) {
                errMsg = "很抱歉！提交记录失败。";
            }

            EventBus.getDefault().post(EventUploadRecord.getErrEvent(errMsg));
        }
    };

    //      上传个人观鸟记录统计成功回调
    private Response.Listener<CommonResponse<UploadRecordResp>> uploadRecordSuc =
            new Response.Listener<CommonResponse<UploadRecordResp>>() {

                @Override
                public void onResponse(CommonResponse<UploadRecordResp> response) {

                    EventUploadRecord event = new EventUploadRecord();
                    event.setIsSuc(true);

                    UploadRecordResp recordResp = response.getT();

                    if (null != recordResp) {
                        event.setNode_id(response.getT().getNode_id());
                    }

                    EventBus.getDefault().post(event);
                }
            };

    @Override
    public String getUploadId() {
        return mUploadId;
    }

    @Override
    public void onUploadSuccess(UploadImageResponse response) {


        if (null != entity.getList()) {

            for (RecordImage image : entity.getList()) {

                if (null == response) {
                    break;
                }

                //设置上传图片ID
                if (image.getUploadImageId().equals(response.getUploadId())) {
                    image.setImageID(response.getData().getPhotoID());
//                    image.setImageUri(response.getData().getImgURL());
                    break;
                }
            }

            for (RecordImage image : entity.getList()) {
                //上传下一张图片
                if (image.getImageID() <= 0 &&
                        !TextUtils.isEmpty(image.getImageUri())) {
                    mUploadId = uploadImage(image.getImageUri());
                    image.setUploadImageId(mUploadId);
                    return;
                }
            }
        }

        //不需要上传图片了,反注册上传监听
        EventBus.getDefault().post(new RemoveUploadTaskEvent(this));

        publishBirdRecord();
    }
}
