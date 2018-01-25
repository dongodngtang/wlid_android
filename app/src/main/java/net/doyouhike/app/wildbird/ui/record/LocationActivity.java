package net.doyouhike.app.wildbird.ui.record;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.util.CheckNetWork;
import net.doyouhike.app.wildbird.util.GetCityIDUtils;
import net.doyouhike.app.wildbird.util.LogUtil;
import net.doyouhike.app.wildbird.util.location.LocationHelper;
import net.doyouhike.app.wildbird.util.location.overlayutil.PoiOverlay;
import net.doyouhike.app.wildbird.ui.view.AutoEditText;
import net.doyouhike.app.wildbird.ui.view.TitleView;
import net.doyouhike.app.wildbird.ui.view.TitleView.ClickListener;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class LocationActivity extends BaseAppActivity implements ClickListener, OnClickListener,
        OnGetGeoCoderResultListener, OnGetPoiSearchResultListener, OnGetSuggestionResultListener {

    private TitleView titleview;
    private MapView bmapView;
    private EditText search_city;
    private ImageView daohang;
    private TextView address, searchAddr;
    private String mCity = "";
    private String mProvince = "";
    private int cityID = 0;
    private double lat = 0.0, lon = 0.0, alt = 0.0;

    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private AutoEditText keyWorldsView = null;
    private ArrayAdapter<String> sugAdapter = null;
    private BaiduMap mBaiduMap;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    GeoCoder mGeoSearch = null; // 搜索模块，因为百度定位sdk能够得到经纬度，但是却无法得到具体的详细地址，因此需要采取反编码方式去搜索此经纬度代表的地址
    BitmapDescriptor bdgeo = BitmapDescriptorFactory.fromResource(R.drawable.map2);

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.location_view;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();

        titleview = (TitleView) super.findViewById(R.id.titleview);
        bmapView = (MapView) super.findViewById(R.id.bmapView);
        keyWorldsView = (AutoEditText) super.findViewById(R.id.search_city);
        search_city = (EditText) super.findViewById(R.id.search_city);
        searchAddr = (TextView) super.findViewById(R.id.search);
        daohang = (ImageView) super.findViewById(R.id.daohang);
        address = (TextView) super.findViewById(R.id.address);

        titleview.setListener(this);
        searchAddr.setOnClickListener(this);
        daohang.setOnClickListener(this);

        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        mBaiduMap = bmapView.getMap();
        mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                // TODO Auto-generated method stub
                // 反Geo搜索
                mGeoSearch.reverseGeoCode(new ReverseGeoCodeOption().location(arg0.getPosition()));
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0) {
                // TODO Auto-generated method stub
                // 反Geo搜索
                // mGeoSearch.reverseGeoCode(new
                // ReverseGeoCodeOption().location(arg0));
            }
        });
        mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng arg0) {
                // TODO Auto-generated method stub
                // 反Geo搜索
                mGeoSearch.reverseGeoCode(new ReverseGeoCodeOption().location(arg0));
            }
        });
        resetMark(new LatLng(39.963175, 116.400244));
        sugAdapter = new ArrayAdapter<String>(this, R.layout.addr_text);
        keyWorldsView.setAdapter(sugAdapter);
        keyWorldsView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        keyWorldsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (cs.length() <= 0) {
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(cs.toString()).city(cs.toString()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        if (CheckNetWork.isNetworkAvailable(getApplicationContext())) {
            initLocClient();
        }
        mGeoSearch = GeoCoder.newInstance();
        mGeoSearch.setOnGetGeoCodeResultListener(this);
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        // TODO Auto-generated method stub
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        sugAdapter.clear();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                sugAdapter.add(info.key);
                mCity = info.city;
            }
        }
        sugAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        // TODO Auto-generated method stub
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            toast("抱歉，未找到结果");
        } else {

            String strAddress = LocationHelper.removeChina(result.getAddress());

            address.setText(strAddress);
            MyLocationData locData = new MyLocationData.Builder().accuracy(0).direction(100)
                    .latitude(result.getLocation().latitude).longitude(result.getLocation().longitude).build();
            mBaiduMap.setMyLocationData(locData);
            // 显示在地图上
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(result.getLocation());
            mBaiduMap.animateMapStatus(u);

            lat = result.getLocation().latitude;
            lon = result.getLocation().longitude;
            alt = 0;
            cityID = 0;

        }
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        // TODO Auto-generated method stub
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            toast(strInfo);
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        // TODO Auto-generated method stub
    }

    @SuppressLint("NewApi")
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        // TODO Auto-generated method stub
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        try {
            String strAddr = LocationHelper.removeChina(result.getAddress());
            address.setText(strAddr);
            mBaiduMap.clear();
            mBaiduMap.setMyLocationEnabled(false);

            if (result.getLocation() == null) {
                return;
            }

//            MyLocationData locData = new MyLocationData.Builder().accuracy(0).direction(100)
//                    .latitude(result.getLocation().latitude).longitude(result.getLocation().longitude).build();
//            mBaiduMap.setMyLocationData(locData);

            resetMark(result.getLocation());
            // // 显示在地图上
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(result.getLocation());
            if (null == u) {
                return;
            }
            mBaiduMap.animateMapStatus(u);

            lat = result.getLocation().latitude;
            lon = result.getLocation().longitude;
            alt = 0;
            cityID = 0;
            mCity = result.getAddressDetail().city;
            mProvince = result.getAddressDetail().province;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initLocClient() {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL, true, null));
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setProdName("WildBird");// 设置产品线
        option.setOpenGps(true);// 打开gps
        option.setLocationMode(LocationMode.Hight_Accuracy);// 高精度定位
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        option.setIgnoreKillProcess(true);
        option.setIsNeedLocationDescribe(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
        if (mLocClient != null && mLocClient.isStarted())
            mLocClient.requestLocation();
    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            mCity = poi.city;
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
            return true;
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || bmapView == null)
                return;
            if (lat == location.getLatitude() && lon == location.getLongitude()) {
                mLocClient.stop();
                return;
            }
//            mBaiduMap.clear();
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();


            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMarker.setPosition(latLng);
            mBaiduMap.setMyLocationData(locData);
            lat = location.getLatitude();
            lon = location.getLongitude();
            alt = location.getAltitude();
            mCity = location.getCity();
            if (location.getCityCode() != null && !location.getCityCode().equals("") &&
                    !location.getCityCode().equals("null")) {
                cityID = Integer.parseInt(location.getCityCode());
            }
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            String address = location.getAddrStr();
            if (!TextUtils.isEmpty(address)) {

                address = LocationHelper.removeChina(address);
                LocationActivity.this.address.setText(address);
            } else {
                // 反Geo搜索
                mGeoSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
            }
            // 显示在地图上
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);
        }
    }

    @Override
    protected void onDestroy() {
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if (mLocClient != null && mLocClient.isStarted()) {
            // 退出时销毁定位
            mLocClient.stop();
        }
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        bmapView.onDestroy();
        bmapView = null;
        super.onDestroy();
        // 回收 bitmap 资源
        bdgeo.recycle();
    }

    private void search() {
        // TODO Auto-generated method stub
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_city.getWindowToken(), 0);
        String key = search_city.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            toast("请先输入城市和详细地址.");
            return;
        }
        toast("正在搜索 " + key);
        address.setText(key);
        String str = search_city.getText().toString().trim();
        if (!TextUtils.isEmpty(this.mCity))
            str = this.mCity;
        mPoiSearch.searchInCity((new PoiCitySearchOption()).city(str).keyword(key).pageNum(0).pageCapacity(20));
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.daohang:
                if (CheckNetWork.isNetworkAvailable(getApplicationContext())) {
                    mBaiduMap.clear();
                    initLocClient();
                } else {
                    toast("当前没有网络。");
                }
                break;
            case R.id.search:
                mBaiduMap.clear();
                search();
                break;
        }
    }

    @Override
    public void clickLeft() {
        // TODO Auto-generated method stub
        finish();
    }

    @Override
    public void clickRight() {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.putExtra("city", mCity);
        intent.putExtra("location", address.getText().toString().trim());

        cityID = GetCityIDUtils.getCityID(mCity);

        if (cityID == 0) {
            cityID = GetCityIDUtils.getProvinceId(mProvince);
        }


        intent.putExtra("cityID", cityID);
        intent.putExtra("latitude", lat);
        intent.putExtra("longtitude", lon);
        intent.putExtra("altitude", alt);
        setResult(RESULT_OK, intent);
        finish();
    }


    private Marker mMarker;


    /**
     *
     * @param point 定义Maker坐标点
     */
    private void resetMark(LatLng point) {
        //定义Maker坐标点
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.map2);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions options = new MarkerOptions()
                .position(point)  //设置marker的位置
                .icon(bitmap)  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(true);  //设置手势拖拽
        //将marker添加到地图上
        mMarker = (Marker) (mBaiduMap.addOverlay(options));


        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                mMarker.setPosition(mapStatus.target);
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                mGeoSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mapStatus.target));
                LogUtil.d("map", "mapStatus:" + mapStatus.toString());
            }
        });

    }

}
