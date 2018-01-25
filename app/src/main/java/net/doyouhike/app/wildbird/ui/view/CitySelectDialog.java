/*
* -----------------------------------------------------------------
* Copyright (C) 2013-2015, by Gemo Info , All rights reserved.
* -----------------------------------------------------------------
*
* File: CitySelectDialog.java
* Author: ChenWeiZhen
* Version: 1.0
* Create: 2015-10-20
*
* Changes (from 2015-10-20)
* -----------------------------------------------------------------
* 2015-10-20 创建CitySelectDialog.java (ChenWeiZhen);
* -----------------------------------------------------------------
*/
package net.doyouhike.app.wildbird.ui.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.CitySelectInfo;
import net.doyouhike.app.wildbird.ui.adapter.CitySelectAdapter;
import net.doyouhike.app.wildbird.util.GetCityIDUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class CitySelectDialog {

    private Context context;

    private View contentView;
    private AlertDialog wheelDialog;

    private TextView completeTv;
    private ListView provinceLv;
    private CitySelectAdapter provinceAdapter;
    private ListView cityLv;
    private CitySelectAdapter cityAdapter;

    private List<String> provinceStrList;
    private List<List<String>> cityStrLists;
    private List<CitySelectInfo> cityList;

    private String provinceSelected;
    private String citySelected;

    public CitySelectDialog(Context context) {
        this.context = context;

        initXMLResource();

        initContentView();

        wheelDialog = new AlertDialog.Builder(context).create();
        wheelDialog.getWindow().setGravity(Gravity.BOTTOM);
        wheelDialog.getWindow().setWindowAnimations(R.style.bottom_dialog);
    }

    /**
     * 读取xml
     */
    private void initXMLResource() {
        boolean needRead = false;
        if (provinceStrList == null) {
            provinceStrList = new ArrayList<String>();
            needRead = true;
        }
        if (cityStrLists == null) {
            cityStrLists = new ArrayList<List<String>>();
            needRead = true;
        }
        if (cityList == null) {
            cityList = new ArrayList<CitySelectInfo>();
            needRead = true;
        }

        if (needRead) {

            cityList.clear();
            cityList.addAll(GetCityIDUtils.getCityDatas());


            // 分组,准备显示数据
            provinceStrList.add("");
            provinceStrList.add("");
            cityStrLists.add(new ArrayList<String>());
            cityStrLists.add(new ArrayList<String>());
            ArrayList<String> cSList = null;
            for (CitySelectInfo citySelectInfo : cityList) {
                String provinceName = citySelectInfo.getProvinceName();
                if (!provinceStrList.contains(provinceName)) {
                    cSList = new ArrayList<String>();
                    cSList.add("");
                    cSList.add("");
                    provinceStrList.add(provinceName);
                    cityStrLists.add(cSList);
                }
                int index = provinceStrList.indexOf(provinceName);
                cSList = (ArrayList<String>) cityStrLists.get(index);
                cSList.add(citySelectInfo.getCityName());
            }

            cityStrLists.add(new ArrayList<String>());
            cityStrLists.add(new ArrayList<String>());
            provinceStrList.add("");
            provinceStrList.add("");

            for (List<String> cityStrList : cityStrLists) {
                cityStrList.add("");
                cityStrList.add("");
            }
        }

    }

    @SuppressLint("InflateParams")
    private void initContentView() {

        contentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_city_select, null);

        completeTv = (TextView) contentView
                .findViewById(R.id.tv_city_select_complete);
        completeTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (wheelDialog != null) {
                    CitySelectInfo citySelectInfo = new CitySelectInfo(0, "",
                            0, "");
                    for (CitySelectInfo csInfo : cityList) {
                        if (csInfo.getProvinceName().equals(provinceSelected)
                                && csInfo.getCityName().equals(citySelected)) {
                            citySelectInfo = csInfo;
                            break;
                        }
                    }
                    EventBus.getDefault().post(citySelectInfo);
                    wheelDialog.dismiss();
                }
            }
        });

        OnScrollListener scrollListener = new OnScrollListener() {
            int provinceItem = 0;
            int cityItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == 0) {
                    View itemView = null;
                    int h = 0;
                    int y = 0;
                    switch (view.getId()) {
                        case R.id.lv_province_select:
                            setSelect(provinceLv, provinceAdapter, scrollState,
                                    provinceItem);

                            itemView = provinceAdapter.getItemView(provinceItem);
                            h = itemView.getHeight();
                            y = (int) itemView.getY(); // 注意是负数

                            if (y == 0 || y == -h + 5) {
                                List<String> cityStrList = null;
                                if (y == 0) {
                                    provinceSelected = provinceStrList
                                            .get(provinceItem + 2);

                                    cityStrList = cityStrLists
                                            .get(provinceItem + 2);
                                } else { // 最后一项比较奇葩
                                    provinceSelected = provinceStrList
                                            .get(provinceItem + 3);

                                    cityStrList = cityStrLists
                                            .get(provinceItem + 3);
                                }
                                citySelected = cityStrList.get(2);
                                cityAdapter = new CitySelectAdapter(context,
                                        cityStrList);
                                cityLv.setAdapter(cityAdapter);
                            }
                            break;
                        case R.id.lv_city_select:
                            setSelect(cityLv, cityAdapter, scrollState, cityItem);

                            itemView = cityAdapter.getItemView(cityItem);
                            h = itemView.getHeight();
                            y = (int) itemView.getY(); // 注意是负数
                            int provinceIndex = provinceStrList
                                    .indexOf(provinceSelected);
                            if (y == 0) {
                                citySelected = cityStrLists.get(provinceIndex).get(
                                        cityItem + 2);
                                System.out.println("citySelected=" + citySelected);
                            } else if (y == -h + 5) { // 最后一项比较奇葩
                                citySelected = cityStrLists.get(provinceIndex).get(
                                        cityItem + 3);
                                System.out.println("citySelected=" + citySelected);
                            }
                            break;

                        default:
                            break;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                switch (view.getId()) {
                    case R.id.lv_province_select:
                        this.provinceItem = firstVisibleItem;
                        changDisplay(provinceAdapter, firstVisibleItem);
                        break;
                    case R.id.lv_city_select:
                        this.cityItem = firstVisibleItem;
                        changDisplay(cityAdapter, firstVisibleItem);
                        break;

                    default:
                        break;
                }

            }
        };

        provinceLv = (ListView) contentView
                .findViewById(R.id.lv_province_select);
        provinceAdapter = new CitySelectAdapter(context, provinceStrList);
        provinceLv.setAdapter(provinceAdapter);
        provinceLv.setOnScrollListener(scrollListener);

        cityLv = (ListView) contentView.findViewById(R.id.lv_city_select);
        cityAdapter = new CitySelectAdapter(context, cityStrLists.get(2));
        cityLv.setAdapter(cityAdapter);
        cityLv.setOnScrollListener(scrollListener);
    }

    /**
     * 显示
     */
    public void show() {
        if (wheelDialog != null) {
            provinceSelected = provinceStrList.get(2); // 因为有两个补位，所以起始下标为2
            citySelected = cityStrLists.get(2).get(2); // 同上

            wheelDialog.show();
            wheelDialog.setContentView(contentView);
            wheelDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);

            provinceLv.setSelection(0);
            cityAdapter = new CitySelectAdapter(context, cityStrLists.get(2));
            cityLv.setAdapter(cityAdapter);
        }
    }

    /**
     * 滚动停止时，自动选中一个项
     *
     * @param wheelLv
     * @param wheelAdapter
     * @param scrollState
     * @param firstVisibleItem
     */
    private void setSelect(ListView wheelLv, CitySelectAdapter wheelAdapter,
                           int scrollState, int firstVisibleItem) {

        View itemView = wheelAdapter.getItemView(firstVisibleItem);
        float y = itemView.getY(); // 注意是负数
        float h = itemView.getHeight();

        // 选中
        if (scrollState == 0) {

            if (wheelAdapter.getItemView(firstVisibleItem) != null) {

                if (y != 0 && Math.abs(y) >= 7) {
                    if (y > -h / 2) {
                        wheelLv.smoothScrollBy(-7, 1);
                    } else {
                        wheelLv.smoothScrollBy(7, 1);
                    }
                } else if (y != 0) {
                    if (y > -h / 2) {
                        wheelLv.smoothScrollBy((int) y, 1);
                    } else {
                        wheelLv.smoothScrollBy((int) (h + y), 1);
                    }
                }
            }
        }
    }

    /**
     * 改变显示效果
     *
     * @param wheelAdapter
     * @param firstItemId
     */
    private void changDisplay(CitySelectAdapter wheelAdapter, int firstItemId) {

        View itemView = wheelAdapter.getItemView(firstItemId);
        float y = itemView.getY(); // 注意是负数
        float h = itemView.getHeight();
        float ratio = (-y) / h;

        for (int i = 0; i <= 5; i++) {
            itemView = wheelAdapter.getItemView(firstItemId + i);
            if (itemView == null) {
                break;
            }
            LinearLayout wheelItemLlyt = (LinearLayout) itemView
                    .findViewById(R.id.llyt_wheel_item);
            if (i < 5 / 2) {
                wheelItemLlyt.setAlpha(0.1f * (i + 1 - ratio));
            } else if (i == 5 / 2) {
                wheelItemLlyt.setAlpha(0.33f * (i + 1 - ratio));
            } else {
                wheelItemLlyt.setAlpha(0.1f * (5 + 1 - i + ratio));
            }

            wheelItemLlyt.setRotationX(33 * (2 - i + ratio));

            switch (i) {
                case 0:
                    wheelItemLlyt.setAlpha(0.1f * (i + 1 - ratio));
                    wheelItemLlyt.setTranslationY(h * (132 + 160 * ratio) / 200);
                    break;
                case 1:
                    wheelItemLlyt.setAlpha(0.1f * (i + 1 - ratio));
                    wheelItemLlyt.setTranslationY(h * (33 + 99 * ratio) / 200);
                    break;
                case 2:
                    wheelItemLlyt.setAlpha(0.33f * (i + 1) - 0.8f * ratio);
                    wheelItemLlyt.setTranslationY(h * (0 + 33 * ratio) / 200);
                    break;
                case 3:
                    wheelItemLlyt.setAlpha(0.1f * (5 - i) + 0.8f * ratio);
                    wheelItemLlyt.setTranslationY(-h * (33 - 33 * ratio) / 200);
                    break;
                case 4:
                    wheelItemLlyt.setAlpha(0.1f * (5 - i + ratio));
                    wheelItemLlyt.setTranslationY(-h * (132 - 99 * ratio) / 200);
                    break;
                case 5:
                    wheelItemLlyt.setAlpha(0.1f * (5 - i + ratio));
                    wheelItemLlyt.setTranslationY(-h * (298 - 166 * ratio) / 200);
                    break;

                default:
                    break;
            }
        }
    }
}
