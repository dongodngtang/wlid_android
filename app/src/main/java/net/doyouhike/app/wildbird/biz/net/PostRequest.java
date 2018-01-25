package net.doyouhike.app.wildbird.biz.net;

import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import net.doyouhike.app.wildbird.app.MyApplication;
import net.doyouhike.app.wildbird.biz.dao.net.NetException;
import net.doyouhike.app.wildbird.biz.dao.base.IResponseProcess;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.util.LocalSharePreferences;
import net.doyouhike.app.wildbird.util.LogUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Makes a post request and converts the response from JsonElement into a
 * list of objects/object using with Google Gson.
 */
public class PostRequest<T extends BaseResponse> extends JsonRequest<T> {

    private static final String TAG = "NET_POST";
    private final Response.Listener<T> listener;
    private final IResponseProcess<T> mResponseProcess;

    /**
     * post请求
     *
     * @param url             链接
     * @param body            post的参数Json格式
     * @param responseProcess 针对返回json的处理工具
     * @param listener        成功监听
     * @param errorListener   失败监听，包括网络请求失败，服务器返回失败等
     */
    public PostRequest
    (
            @NonNull final String url,
            @NonNull final String body,
            @NonNull final IResponseProcess<T> responseProcess,
            @NonNull final Response.Listener<T> listener,
            @NonNull final Response.ErrorListener errorListener
    ) {
        super(Method.POST, url, body, listener, errorListener);

        this.mResponseProcess = responseProcess;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        headers.put(Content.NET_HEADER_TOKEN,
                LocalSharePreferences.getValue
                        (MyApplication.getInstance().getApplicationContext(), Content.SP_TOKEN));
        return headers;

    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {


        String json;
        try {
            json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {

            NetException netException = new NetException(new ParseError(e));
            netException.setTag(mResponseProcess.getExtraTag());

            return Response.error(netException);
        }

        LogUtil.d(TAG + " response", response.statusCode + ":" + json);
        LogUtil.d(TAG, "" + response.networkTimeMs + "毫秒");

        T t = mResponseProcess.getResponse(json);

        if (t.isSuccess()) {
            return Response.success(t, HttpHeaderParser.parseCacheHeaders(response));
        } else {

            NetException netException = new NetException(t.getErrMsg());
            netException.setTag(mResponseProcess.getExtraTag());

            return Response.error(netException);
        }


    }


    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {

        if (null != volleyError) {

            LogUtil.d(TAG, "VolleyError" + volleyError.getClass().getName());
            volleyError.printStackTrace();
        }


        String errMsg = volleyError.getMessage();

        if (volleyError.networkResponse != null) {
            LogUtil.d(TAG, "VolleyError" + volleyError.networkResponse.statusCode);
            errMsg = "网络不佳" + volleyError.networkResponse.statusCode;
        } else if (volleyError instanceof TimeoutError) {
            errMsg = "网络不佳";
        }

        NetException netException = new NetException(errMsg, volleyError);
        netException.setTag(mResponseProcess.getExtraTag());
        return netException;
    }
}
