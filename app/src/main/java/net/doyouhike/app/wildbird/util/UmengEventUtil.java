package net.doyouhike.app.wildbird.util;

import com.umeng.analytics.MobclickAgent;

import net.doyouhike.app.wildbird.app.MyApplication;

import java.util.HashMap;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-20.
 */
public class UmengEventUtil {



    public static void onBannerEvent(String httpUrl){
        HashMap<String,String> map = new HashMap<>();
        map.put("httpUrl",httpUrl);
        MobclickAgent.onEvent(MyApplication.getInstance().getApplicationContext(), "banner", map);
    }

    public static void onLoginEvent(){
        MobclickAgent.onEvent(MyApplication.getInstance().getApplicationContext(), "login");
    }
}
