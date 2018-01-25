package net.doyouhike.app.wildbird.util.location;

import android.text.TextUtils;

/**
 * Created by zaitu on 15-11-12.
 */
public class LocationHelper {
    public static String removeChina(String address){

        if (TextUtils.isEmpty(address)){
            return address;
        }

        //特殊处理，去掉头部中国两个字
        int index=address.indexOf("中国");

        if (index==0){
            int start="中国".length();
            int end=address.length();
            return address.substring(start,end);
        }


        return address;
    }
}
