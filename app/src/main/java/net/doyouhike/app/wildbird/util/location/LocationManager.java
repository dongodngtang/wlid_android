package net.doyouhike.app.wildbird.util.location;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.WbLocation;
import net.doyouhike.app.wildbird.util.GetCityIDUtils;
import net.doyouhike.app.wildbird.util.LogUtil;

import de.greenrobot.event.EventBus;


/**
 * Created by Administrator on 14-3-13.
 */
public class LocationManager implements BDLocationListener {

    private static final String DEFAULT_ADDRESS = "";
    public static final String DEFAULT_CITY = "未知";

    public static LocationManager mLocationManager;
    private LocationClient mLocationClient;
    private Context mContext;

    public static LocationManager getInstance() {
        if (mLocationManager == null) {
            synInit();
        }
        return mLocationManager;
    }


    public void start(Context context) {

        if (null == mLocationClient) {
            init(context);
        }

        if (mLocationClient.isStarted()) {
            return;
        }

        setLocationOption();
        mLocationClient.start();
        mLocationClient.requestLocation();
    }


    public void stop() {
        if (null != mLocationClient)
            mLocationClient.stop();
    }

    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setProdName("WildBird");// 设置产品线
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 高精度定位
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        option.setIgnoreKillProcess(true);
        option.setIsNeedLocationDescribe(true);
        option.setProdName(mContext.getResources().getString(R.string.app_name));
        mLocationClient.setLocOption(option);

    }


    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

        LocationManager.getInstance().stop();
        EventLocation eventLocation = new EventLocation();

        if (bdLocation == null) {
            LogUtil.d("getLocation", "fail");
            eventLocation.setEnumLocation(EnumLocation.FAIL);
            eventLocation.setMsg("定位失败，返回信息为空为空");
            EventBus.getDefault().post(eventLocation);
            return;
        }

        stop();

        int locType = bdLocation.getLocType();
        LogUtil.d("getLocation", "success:" + bdLocation.getAddrStr() +
                "Longitude:" + bdLocation.getLongitude() + "LocType:" + locType);

        WbLocation location = new WbLocation();
        location.setLatitude(bdLocation.getLatitude());
        location.setLongitude(bdLocation.getLongitude());


//        百度定位中出现4.9E-324是什么意思
        String strAltitude = String.valueOf(bdLocation.getAltitude());
        if (strAltitude.contains("E") || strAltitude.contains("e")) {
            location.setAltitude(0);
        } else {
            location.setAltitude(bdLocation.getAltitude());
        }

        location.setCityCode(bdLocation.getCityCode());

        if (bdLocation.hasAddr()) {

            String address = bdLocation.getAddrStr();
            if (TextUtils.isEmpty(address)) {
                address = DEFAULT_ADDRESS;
            } else {
                address = LocationHelper.removeChina(address);
            }
            location.setAddress(address);
            String city = bdLocation.getCity();
            if (TextUtils.isEmpty(city)) {
                city = DEFAULT_CITY;
            } else {
                int cityId = GetCityIDUtils.getCityID(city);
                location.setCityCode(cityId + "");
                String provinceId = GetCityIDUtils.getProvinceId(bdLocation.getProvince()) + "";
                location.setProvinceCode(provinceId);
                if (cityId == 0) {
                    location.setCityCode(provinceId);
                }
            }
            location.setCity(city);
        } else {
            location.setAddress(DEFAULT_ADDRESS);
            location.setCity(DEFAULT_CITY);
        }
        eventLocation.setLocation(location);

        switch (locType) {
            case BDLocation.TypeGpsLocation:// GPS定位结果
                eventLocation.setEnumLocation(EnumLocation.SUCCESS);
                eventLocation.setMsg("GPS定位结果");
                break;
            case BDLocation.TypeNetWorkLocation:// 网络定位结果
                eventLocation.setEnumLocation(EnumLocation.SUCCESS);
                eventLocation.setMsg("网络定位结果");
                break;
            case BDLocation.TypeOffLineLocation:// 离线定位结果
                eventLocation.setEnumLocation(EnumLocation.SUCCESS);
                eventLocation.setMsg("离线定位结果");
                break;
            case BDLocation.TypeServerError:
                eventLocation.setEnumLocation(EnumLocation.PREVENT);
                eventLocation.setMsg(locType + ":服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位");
                break;
            case BDLocation.TypeNetWorkException:
                eventLocation.setEnumLocation(EnumLocation.FAIL);
                eventLocation.setMsg(locType + ":网络不同导致定位失败，请检查网络是否通畅");
                break;
            default:
                eventLocation.setEnumLocation(EnumLocation.FAIL);
                eventLocation.setMsg("定位失败：" + locType);
        }

        if (TextUtils.isEmpty(eventLocation.getLocation().getAddress())){
            eventLocation.setEnumLocation(EnumLocation.FAIL);
        }


        EventBus.getDefault().post(eventLocation);

    }

    private synchronized static void synInit() {

        if (mLocationManager == null) {
            mLocationManager = new LocationManager();
        }
    }

    private void init(Context context) {
        mContext = context.getApplicationContext();
        mLocationClient = new LocationClient(mContext);
        mLocationClient.registerLocationListener(this);
    }
}

