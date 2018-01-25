package net.doyouhike.app.wildbird.biz.service.net;

import android.support.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.doyouhike.app.wildbird.biz.dao.base.IResponseProcess;
import net.doyouhike.app.wildbird.biz.model.base.BaseGetRequest;
import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;
import net.doyouhike.app.wildbird.biz.net.GetRequest;
import net.doyouhike.app.wildbird.biz.net.PostRequest;
import net.doyouhike.app.wildbird.util.LogUtil;

import java.util.Map;

/**
 * Created by zaitu on 15-11-26.
 */
public class BaseApiReq {
    private static final String TAG = "NET_REQUEST";
    private static final int TIME_OUT = 1500;
    private static final int RETRY_COUNT = 3;
    /**
     * The default backoff multiplier
     */
    public static final float DEFAULT_BACKOFF_MULT = 1f;

    /**
     * 基础get请求端
     *
     * @param url             API接口地址：/user/reg
     * @param request         post参数
     * @param responseProcess 针对返回json的处理工具
     * @param listener        成功监听
     * @param errorListener   失败监听，包括网络请求失败，服务器返回失败等
     * @return
     */
    protected static GetRequest baseGet(
            @NonNull final String url,
            final BaseGetRequest request,
            @NonNull final IResponseProcess responseProcess,
            @NonNull final Response.Listener listener,
            @NonNull final Response.ErrorListener errorListener,
            DefaultRetryPolicy policy
    ) {

        responseProcess.setExtraTag(request.getTag());

        Map<String, String> map = request.toHashMap();

        LogUtil.d(TAG, "url:" + url);
        LogUtil.d(TAG, "get getParams:" + map.toString());

        GetRequest getRequest = new GetRequest(url, map, responseProcess, listener, errorListener);


        if (null==policy){
            policy=new DefaultRetryPolicy(TIME_OUT, RETRY_COUNT, DEFAULT_BACKOFF_MULT);
        }
        getRequest.setRetryPolicy(policy);


        return getRequest;
    }

    /**
     * 基础的post端口
     *
     * @param url             API接口地址：/user/reg
     * @param request         post参数
     * @param responseProcess 针对返回json的处理工具
     * @param listener        成功监听
     * @param errorListener   失败监听，包括网络请求失败，服务器返回失败等
     * @return
     */
    protected static PostRequest basePost(
            @NonNull final String url,
            @NonNull final BasePostRequest request,
            @NonNull final IResponseProcess responseProcess,
            @NonNull final Response.Listener listener,
            @NonNull final Response.ErrorListener errorListener,
            DefaultRetryPolicy policy
    ) {

        //将对象转为json格式字符串
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(request);

        LogUtil.d(TAG, "url:" + url);
        LogUtil.d(TAG, "post json:" + json);
        responseProcess.setExtraTag(request.getTag());
        PostRequest postRequest = new PostRequest(url, json, responseProcess, listener, errorListener);


        if (null==policy){
            policy=new DefaultRetryPolicy(TIME_OUT, RETRY_COUNT, DEFAULT_BACKOFF_MULT);
        }

        postRequest.setRetryPolicy(policy);
        return postRequest;
    }
    /**
     * 基础的post端口
     *
     * @param url             API接口地址：/user/reg
     * @param request         post参数
     * @param responseProcess 针对返回json的处理工具
     * @param listener        成功监听
     * @param errorListener   失败监听，包括网络请求失败，服务器返回失败等
     * @return
     */
    protected static PostRequest uploadRecordRequest(
            @NonNull final String url,
            @NonNull final BasePostRequest request,
            @NonNull final IResponseProcess responseProcess,
            @NonNull final Response.Listener listener,
            @NonNull final Response.ErrorListener errorListener
    ) {

        //将对象转为json格式字符串
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(request);

        LogUtil.d(TAG, "url:" + url);
        LogUtil.d(TAG, "post json:" + json);
        responseProcess.setExtraTag(request.getTag());
        PostRequest postRequest = new PostRequest(url, json, responseProcess, listener, errorListener);
        //超时1分
        postRequest.setRetryPolicy(new DefaultRetryPolicy(60*1000, 1, DEFAULT_BACKOFF_MULT));

        return postRequest;
    }
}
