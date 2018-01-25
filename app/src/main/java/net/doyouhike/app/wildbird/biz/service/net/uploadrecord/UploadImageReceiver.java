package net.doyouhike.app.wildbird.biz.service.net.uploadrecord;

import com.google.gson.Gson;

import net.doyouhike.app.wildbird.biz.model.event.EventUploadRecord;
import net.doyouhike.app.wildbird.biz.model.response.UploadImageResponse;
import net.doyouhike.app.wildbird.util.LogUtil;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by zengjiang on 16/6/3.
 */
public class UploadImageReceiver extends UploadServiceBroadcastReceiver {


    ArrayList<UploadImageListener> listeners = new ArrayList<>();


    @Override
    public void onError(String uploadId, Exception exception) {

        EventBus.getDefault().post(EventUploadRecord.getErrEvent("图片上传失败"));
    }

    @Override
    public void onCompleted(String uploadId,
                            int serverResponseCode,
                            byte[] serverResponseBody) {

        String result = new String(serverResponseBody);

        UploadImageResponse response = new Gson().fromJson(result, UploadImageResponse.class);


        if (response.getRet() == 0) {
            response.setUploadId(uploadId);

            for (UploadImageListener listener : listeners) {
                if (listener.getUploadId().equals(uploadId)) {
                    //上传成功回调
                    listener.onUploadSuccess(response);
                }
            }


        } else {
            EventBus.getDefault().post(EventUploadRecord.getErrEvent("图片上传失败"));
        }


        LogUtil.d("发送观鸟记录", new String(serverResponseBody));

    }

    public void addUploadListener(UploadImageListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removedListener(UploadImageListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

}
