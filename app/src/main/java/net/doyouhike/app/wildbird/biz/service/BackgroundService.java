package net.doyouhike.app.wildbird.biz.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.biz.db.bean.DbWildBird;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.biz.model.bean.SpeciesInfo;
import net.doyouhike.app.wildbird.biz.model.event.CancelUploadRecordEvent;
import net.doyouhike.app.wildbird.biz.model.event.CheckVersionEvent;
import net.doyouhike.app.wildbird.biz.model.event.GetBannerEvent;
import net.doyouhike.app.wildbird.biz.model.event.RemoveUploadTaskEvent;
import net.doyouhike.app.wildbird.biz.model.event.RequestBirdUpdateEvent;
import net.doyouhike.app.wildbird.biz.model.request.get.CheckVersionReq;
import net.doyouhike.app.wildbird.biz.model.request.get.GetBannerListRequestParam;
import net.doyouhike.app.wildbird.biz.model.request.get.GetUpdateBirdReq;
import net.doyouhike.app.wildbird.biz.model.response.CheckVersionResponse;
import net.doyouhike.app.wildbird.biz.model.response.GetBrannerListResponse;
import net.doyouhike.app.wildbird.biz.model.response.GetUpdateBirdResponse;
import net.doyouhike.app.wildbird.biz.service.database.WildbirdDbService;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.biz.service.net.UploadRecordService;
import net.doyouhike.app.wildbird.biz.service.net.uploadrecord.UploadImageReceiver;
import net.doyouhike.app.wildbird.util.LocalSpManager;

import java.util.List;

import de.greenrobot.event.EventBus;

public class BackgroundService extends Service {


    /**
     * 上传图片接收器
     */
    private UploadImageReceiver mUploadImageReceiver = new UploadImageReceiver();

    UploadRecordService service;

    public BackgroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mUploadImageReceiver.register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mUploadImageReceiver.unregister(this);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    /**
     * 获取广告
     *
     * @param event 广告事件
     */
    public void onEventBackgroundThread(GetBannerEvent event) {

        ApiReq.doCancel(Content.REQ_BANNER_GETLIST);
        ApiReq.doGet(new GetBannerListRequestParam(),
                new Response.Listener<CommonResponse<GetBrannerListResponse>>() {
                    @Override
                    public void onResponse(CommonResponse<GetBrannerListResponse> response) {
                        LocalSpManager.saveBanner(response.getT().getBanners());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }


    /**
     * 版本检查更新
     *
     * @param event 版本检查
     */
    public void onEventBackgroundThread(CheckVersionEvent event) {

        ApiReq.doCancel(Content.REQ_VERSION_CHECK);
        ApiReq.doGet(new CheckVersionReq(),
                new Response.Listener<CommonResponse<CheckVersionResponse>>() {
                    @Override
                    public void onResponse(CommonResponse<CheckVersionResponse> response) {
                        LocalSpManager.saveBanner(response.getT().getVersion());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }

    /**
     * 更新鸟种
     *
     * @param event 更新鸟种事件
     */
    public void onEventBackgroundThread(RequestBirdUpdateEvent event) {

        String speciesId = WildbirdDbService.getInstance().getLastSpeciesId();

        if (TextUtils.isEmpty(speciesId)) {
            return;
        }
        ApiReq.doCancel(Content.REQ_GET_UPDATE_BIRD);
        ApiReq.doGet(new GetUpdateBirdReq(speciesId),
                new Response.Listener<CommonResponse<GetUpdateBirdResponse>>() {
                    @Override
                    public void onResponse(CommonResponse<GetUpdateBirdResponse> response) {

                        if (null == response || response.getT().getBird_list() == null || response.getT().getBird_list().isEmpty()) {
                            return;
                        }
                        final List<DbWildBird> birds = response.getT().getBird_list();

                        new Thread() {
                            @Override
                            public void run() {
                                for (DbWildBird info : birds) {
                                    WildbirdDbService.getInstance().insertWildbirdInfo(info);
                                }
                            }
                        }.start();


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });


    }

    /**
     * 发送观鸟纪录
     *
     * @param entity 观鸟纪录
     */
    public void onEventBackgroundThread(RecordEntity entity) {

        if (null != service) {
            service.cancel();
            service.onDestroy();
        }
        service = new UploadRecordService(this, entity);
        mUploadImageReceiver.addUploadListener(service);
        service.saveToNet();

    }

    /**
     * 取消发送观鸟纪录
     *
     * @param event 取消发送观鸟纪录事件
     */
    public void onEventBackgroundThread(CancelUploadRecordEvent event) {

        if (null != service) {
            service.cancel();
            service.onDestroy();
        }

    }

    /**
     * 取消监听绑定
     *
     * @param event 观鸟纪录发送任务
     */
    public void onEventBackgroundThread(RemoveUploadTaskEvent event) {
        mUploadImageReceiver.removedListener(event.getService());
    }
}
