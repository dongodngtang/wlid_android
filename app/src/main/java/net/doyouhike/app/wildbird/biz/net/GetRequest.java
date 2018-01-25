package net.doyouhike.app.wildbird.biz.net;

import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;


import net.doyouhike.app.wildbird.app.MyApplication;
import net.doyouhike.app.wildbird.biz.dao.net.ErrState;
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
 * Makes a get request and converts the response from JsonElement into a
 * list of objects/object using with Google Gson.
 */
public class GetRequest<T extends BaseResponse> extends Request<T> {

    private static final String TAG = "NET_GET";
    private final Response.Listener<T> listener;
    private IResponseProcess<T> mResponseProcess;
    private Map<String, String> map;

    /**
     * get请求端
     *
     * @param url             链接
     * @param map             请求参数
     * @param responseProcess 针对返回json的处理工具
     * @param listener        成功监听
     * @param errorListener   失败监听，包括网络请求失败，服务器返回失败等
     */
    public GetRequest
    (
            @NonNull final String url,
            final Map<String, String> map,
            @NonNull final IResponseProcess<T> responseProcess,
            @NonNull final Response.Listener<T> listener,
            @NonNull final Response.ErrorListener errorListener
    ) {
        super(Method.DEPRECATED_GET_OR_POST, url, errorListener);
        this.map = map;
        mResponseProcess = responseProcess;
        this.listener = listener;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (null != map) {
            return map;
        }

        return super.getParams();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {


        HashMap<String, String> headers = new HashMap<>();
        headers.put("Connection", "Keep-Alive");

        headers.put(Content.NET_HEADER_TOKEN,
                LocalSharePreferences.getValue
                        (MyApplication.getInstance().getApplicationContext(), Content.SP_TOKEN));
        return headers;
    }

    @Override
    protected void deliverResponse(T response) {
        LogUtil.d(TAG + " response", response.getClass().getName());
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
            netException.setTag(t.getTag());
            netException.setState(t.getState());
            netException.setMsg(t.getErrMsg());
            netException.setCode(t.getCode());

            return Response.error(netException);
        }


    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {

        LogUtil.d(TAG, "VolleyError" + volleyError.getClass().getName());
        volleyError.printStackTrace();
        LogUtil.d(TAG, "" + volleyError.getNetworkTimeMs() + "毫秒");

        String errMsg = volleyError.getMessage();

        if (volleyError.networkResponse != null) {
            LogUtil.d(TAG, "VolleyError" + volleyError.networkResponse.statusCode);
            errMsg = "网络不佳" + volleyError.networkResponse.statusCode;
        } else if (volleyError instanceof TimeoutError) {
            errMsg = "网络不佳";
        }

        NetException netException = new NetException(errMsg, volleyError);

        if (volleyError instanceof NoConnectionError){
            netException.setState(ErrState.NO_CONNECT);
            netException.setMsg("连接出错");
        }else if (volleyError instanceof TimeoutError){
            netException.setState(ErrState.TIMEOUT);
            netException.setMsg("连接超时");
        } else if (volleyError instanceof ServerError){
            netException.setState(ErrState.SERVICE_ERR);
            netException.setMsg("网络异常");
        }else {
            netException.setState(ErrState.OTHER_ERR);
            netException.setMsg("未知错误");
        }

        netException.setTag(mResponseProcess.getExtraTag());

        return netException;

    }
}
