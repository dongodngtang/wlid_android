
/**
 * Summary: js脚本所能执行的函数空间
 * Version 1.0
 * Date: 13-11-20
 * Time: 下午4:40
 * Copyright: Copyright (c) 2013
 */
package net.doyouhike.app.wildbird.ui.webview;

import android.app.Activity;
import android.webkit.WebView;
import android.widget.Toast;

import net.doyouhike.app.wildbird.biz.model.event.BaseEvent;
import net.doyouhike.app.wildbird.biz.model.event.IObserver;
import net.doyouhike.app.wildbird.util.LogUtil;

import java.util.HashSet;


//HostJsScope中需要被JS调用的函数，必须定义成public static，且必须包含WebView这个参数
public class HostJsScope {

    private static HashSet<IObserver> observers = new HashSet<IObserver>();

    /**
     * 短暂气泡提醒
     *
     * @param webView 浏览器
     * @param message 提示信息
     */
    public static void toast(WebView webView, String message) {
        Toast.makeText(webView.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void activateSuccess(WebView webView, String userNm) {

        LogUtil.d("HostJsScope","activateSuccess  userNm:"+userNm);

        BaseEvent event = new BaseEvent();
        event.setStrContent(userNm);
        onWbEvent(event);
    }


    //---------------- 界面切换类 ------------------

    /**
     * 结束当前窗口
     *
     * @param view 浏览器
     */
    public static void goBack(WebView view) {
        if (view.getContext() instanceof Activity) {
            ((Activity) view.getContext()).finish();
        }
    }

    private static void onWbEvent(BaseEvent event) {
        for (IObserver observer : observers) {
            observer.onWbEvent(event);
        }
    }

    public static void addObserver(IObserver observer) {
        observers.add(observer);
    }

    public static void removedObserver(IObserver observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }

}