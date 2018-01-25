package net.doyouhike.app.wildbird.biz.service.net;

import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.doyouhike.app.wildbird.app.MyApplication;
import net.doyouhike.app.wildbird.biz.net.OkHttpStack;

/**
 * 功能：
 *
 * @author：曾江 日期：16-3-12.
 */
public class RequestUtil {

    private static  RequestUtil instance;
    // Volley request queue
    private RequestQueue mRequestQueue;

    public static synchronized RequestUtil getInstance() {
        if (null==instance){
            initInstance();
        }
        return instance;
    }

    private static synchronized void initInstance() {
        if (null==instance){
            instance=new RequestUtil();
        }
    }

    /**
     * Returns a Volley request queue for creating network requests
     *
     * @return {@link com.android.volley.RequestQueue}
     */
    public RequestQueue getVolleyRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(MyApplication.getInstance().getApplicationContext(), new OkHttpStack());
//			mRequestQueue = Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }

    /**
     * Adds a request to the Volley request queue
     *
     * @param request to be added to the Volley requests queue
     */
    private  void addRequest(@NonNull final Request<?> request) {
        getInstance().getVolleyRequestQueue().add(request);
    }

    /**
     * Adds a request to the Volley request queue with a given tag
     *
     * @param request is the request to be added
     * @param tag     tag identifying the request
     */
    public  void addRequest(@NonNull final Request<?> request, @NonNull final String tag) {
        request.setTag(tag);
        addRequest(request);
    }

    /**
     * Cancels all the request in the Volley queue for a given tag
     *
     * @param tag associated with the Volley requests to be cancelled
     */
    public  void cancelAllRequests(@NonNull final String tag) {
        if (getInstance().getVolleyRequestQueue() != null) {
            getInstance().getVolleyRequestQueue().cancelAll(tag);

        }
    }
}
