package net.doyouhike.app.wildbird.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.doyouhike.app.wildbird.app.MyApplication;
import net.doyouhike.app.wildbird.biz.model.bean.Banner;
import net.doyouhike.app.wildbird.biz.model.bean.Version;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-18.
 */
public class LocalSpManager {

    /**
     * 广告
     */
    private final static String SP_PARAM_BANNER=LocalSpManager.class.getSimpleName()+"param1";
    /**
     * 版本信息
     */
    private final static String SP_PARAM_VERSION=LocalSpManager.class.getSimpleName()+"param2";

    /**
     * @return 广告数据
     */
    public static List<Banner> getBanner(){
        List<Banner> banners=new ArrayList<>();

        String strBanner=LocalSharePreferences.getValue(MyApplication.getInstance().getApplicationContext(),SP_PARAM_BANNER);


        if (!TextUtils.isEmpty(strBanner)){
            List<Banner> tempBanner=new Gson().fromJson(strBanner,
                    new TypeToken<List<Banner>>() {}.getType());
            if (null!=tempBanner){
                banners.addAll(tempBanner);
            }
        }


        return banners;
    }


    /**
     * @param banners 广告数据
     */
    public static void saveBanner(List<Banner> banners){

        String strBanners="";

        if (null!=banners){
            strBanners=new Gson().toJson(banners);
        }

        LocalSharePreferences.commit(MyApplication.getInstance().getApplicationContext(),SP_PARAM_BANNER,strBanners);

    }
    /**
     * @return 网络版本信息
     */
    public static Version getVersion(){

        String strVersion=LocalSharePreferences.getValue(MyApplication.getInstance().getApplicationContext(),SP_PARAM_VERSION);


        if (!TextUtils.isEmpty(strVersion)){
            return new Gson().fromJson(strVersion,Version.class);
        }

        return null;
    }


    /**
     * @param version 网络版本信息
     */
    public static void saveBanner(Version version){

        String strVersion="";

        if (null!=version){
            strVersion=new Gson().toJson(version);
        }

        LocalSharePreferences.commit(MyApplication.getInstance().getApplicationContext(),SP_PARAM_VERSION,strVersion);

    }




}
