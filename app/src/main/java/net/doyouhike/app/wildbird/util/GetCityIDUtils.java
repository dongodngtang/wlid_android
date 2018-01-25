package net.doyouhike.app.wildbird.util;

import android.text.TextUtils;
import android.util.Xml;


import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.app.MyApplication;
import net.doyouhike.app.wildbird.biz.model.bean.CitySelectInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZouWenJie 获取城市ID的工具类
 */
public class GetCityIDUtils {
    /**
     * @param city 传入定位的城市
     * @return 返回定位城市的ID，若无，返回0
     */
    public static int getCityID(String city) {

        if (TextUtils.isEmpty(city)){
            return 0;
        }

        List<CitySelectInfo> cityDates = getCityDatas();

        //遍历城市数组
        if (cityDates != null && cityDates.size() > 0) {
            for (CitySelectInfo citySelectInfo : cityDates) {
                if (city.contains(citySelectInfo.getCityName())||citySelectInfo.getCityName().contains(city)) {
                    return citySelectInfo.getCityId();// 返回传入城市的id
                }
            }
        }
        return 0;
    } /**
     * @param province 传入定位的城市
     * @return 返回定位省份的ID，若无，返回0
     */
    public static int getProvinceId(String province) {

        if (TextUtils.isEmpty(province)){
            return 0;
        }


        List<CitySelectInfo> cityDates = getCityDatas();

        //遍历城市数组
        if (cityDates != null && cityDates.size() > 0) {
            for (CitySelectInfo citySelectInfo : cityDates) {
                if (province.contains(citySelectInfo.getCityName())||citySelectInfo.getCityName().contains(province)) {
                    return citySelectInfo.getProvinceId();// 返回省份的ID
                }
            }
        }
        return 0;
    }

    public static String getCity(int cityID) {
        if (0==cityID) {
            return "";
        }

        int id = Integer.valueOf(cityID);

        List<CitySelectInfo> cityDates = getCityDatas();
        if (null == cityDates) {
            return "";
        }


        //遍历城市数组
        for (CitySelectInfo citySelectInfo : cityDates) {
            if (citySelectInfo.getCityId()==id) {
                return citySelectInfo.getCityName();
            }
        }

        return "";

    }


    public static List<CitySelectInfo> getCityDatas() {
        List<CitySelectInfo> cityDates = null;
        CitySelectInfo cityInfo = null;
        try {
            InputStream is = MyApplication.getInstance().getApplicationContext().getResources().openRawResource(R.raw.city);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "utf-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        cityDates = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("row")) {
                            cityInfo = new CitySelectInfo(0, null, 0, null);
                            break;
                        } else if (parser.getName().equals("CityID")) {
                            eventType = parser.next();
                            cityInfo.setCityId(Integer.parseInt(parser.getText()));
                            break;
                        } else if (parser.getName().equals("ProvinceID")) {
                            eventType = parser.next();
                            cityInfo.setProvinceId(Integer.parseInt(parser
                                    .getText()));
                            break;
                        } else if (parser.getName().equals("Province")) {
                            eventType = parser.next();
                            cityInfo.setProvinceName(parser.getText());
                            break;
                        } else if (parser.getName().equals("Name")) {
                            eventType = parser.next();
                            cityInfo.setCityName(parser.getText());
                            break;
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("row")) {
                            cityDates.add(cityInfo);
                            cityInfo=null;
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cityDates;
    }
}
