package net.doyouhike.app.wildbird.ui.main.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.ShareContent;
import net.doyouhike.app.wildbird.biz.model.bean.Species;
import net.doyouhike.app.wildbird.biz.model.bean.WbLocation;
import net.doyouhike.app.wildbird.biz.model.request.get.GetRecordStats;
import net.doyouhike.app.wildbird.biz.model.response.GetRecordStatsResp;
import net.doyouhike.app.wildbird.ui.adapter.HabitAdapter;
import net.doyouhike.app.wildbird.ui.adapter.RecordAdapter;
import net.doyouhike.app.wildbird.ui.base.BaseFragment;
import net.doyouhike.app.wildbird.ui.record.LocationActivity;
import net.doyouhike.app.wildbird.util.CheckNetWork;
import net.doyouhike.app.wildbird.util.GotoActivityUtil;
import net.doyouhike.app.wildbird.util.LogUtil;
import net.doyouhike.app.wildbird.util.PermissionUtil;
import net.doyouhike.app.wildbird.util.UiUtils;
import net.doyouhike.app.wildbird.util.location.EnumLocation;
import net.doyouhike.app.wildbird.util.location.EventLocation;
import net.doyouhike.app.wildbird.util.location.LocationManager;
import net.doyouhike.app.wildbird.ui.view.MyAlertDialogUtil;
import net.doyouhike.app.wildbird.ui.view.MyGridView;
import net.doyouhike.app.wildbird.ui.view.ShareDialog;
import net.doyouhike.app.wildbird.ui.view.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

@SuppressWarnings("deprecation")
public class RecordFragment extends BaseFragment
        implements OnClickListener, OnItemClickListener, IRecordFragView, TitleView.ClickListener {

    private static final String TAG = RecordFragment.class.getSimpleName();
    @InjectView(R.id.titleview)
    TitleView titleview;

    private final String strShareTitle = "附近%s公里的鸟种记录";
    private final String strShareContent = "%s附近%s公里的观鸟记录";

    private View view, record_line;
    private TextView choose_addr, record_search, choose_record, findNone;
    private GridView records;
    private RecordAdapter adapter;
    private List<Species> birdlist;
    private PopupWindow popup;
    private HabitAdapter month_adapter, range_adapter;
    private String[] month = new String[12];
    private String[] range;
    private int month_pos = 0, range_pos = 1;

    private boolean first = true;// 区别第一次查询
    private boolean refreshed = false;// json重复请求判断
    private boolean started = false;// 滑动是否可以更新判断
    private ProgressDialog progressDialog = null;
    private String city = "";
    private double lat = 0.0, lon = 0.0;
    private boolean search = false;// 判断是否由编辑地图界面选择好地址了
    private static final int LOCATION_OK = 1;
    private RecordFragPret presenter;

    private String mLocationAddress = "";
    private ShareContent shareContent;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 1:
                    choose_addr.setText(city.replace("市", ""));
                    if (birdlist.size() <= 0 && !refreshed) {
                        refreshed = true;
                        getRecordNew(0);
                    }
                    break;
                case 2:
                    if (birdlist.size() <= 0) {
                        changeView(1);
                    }
                    choose_addr.setText("未知");
                    break;
            }
        }
    };

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.record_view;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();
        view = getView();
        choose_addr = (TextView) view.findViewById(R.id.choose_addr);
        choose_record = (TextView) view.findViewById(R.id.choose_record);
        record_search = (TextView) view.findViewById(R.id.record_search);
        record_line = view.findViewById(R.id.record_line);
        records = (GridView) view.findViewById(R.id.records);
        findNone = (TextView) view.findViewById(R.id.findNone);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d("MainActivity", "RecordFragment onActivityCreated ");

        shareContent = new ShareContent();
        titleview.setListener(this);
        titleview.showRightImg(false);

        birdlist = new ArrayList<Species>();
        adapter = new RecordAdapter(getActivity(), 1, birdlist);
        records.setAdapter(adapter);

        choose_addr.setOnClickListener(this);
        record_search.setOnClickListener(this);
        choose_record.setOnClickListener(this);
        records.setOnItemClickListener(this);

        range = new String[]{"0.5", "5", "20", "50", "100"};
        month_adapter = new HabitAdapter(getActivity(), month, 1);
        range_adapter = new HabitAdapter(getActivity(), range, 1);

        Time time = new Time("GMT+8");
        time.setToNow();
        month_pos = time.month;
        for (int i = 0; i < month.length; i++) {
            month[i] = (i + 1) + "月";
        }
        for (int i = 0; i < range.length; i++) {
            range[i] += "公里内";
        }
        month_adapter.setSelect(month_pos);
        range_adapter.setSelect(range_pos);
        choose_record.setText(month[month_pos] + " | " + range[range_pos]);
        records.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                if (!started && totalItemCount > 0 && firstVisibleItem + visibleItemCount == totalItemCount) {
                    started = true;
                    if (birdlist.size() % 20 == 0 && !refreshed) {
                        toast("正在加载...");
                        refreshed = true;
                        LogUtil.d("onScroll:" + refreshed);
                        getRecordNew(birdlist.size());
                    }
                }
            }
        });
        if (CheckNetWork.isNetworkAvailable(getActivity())) {
            findNone.setText("查询较久，请耐心稍候...");

            initLocClient();

        } else {
            findNone.setText("当前没有网络,无法获取记录");
            choose_addr.setText("未知");
        }
        presenter = new RecordFragPret(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        LogUtil.d("MainActivity", "RecordFragment onResume ");
        getActivity().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    private void initLocClient() {

        // 定位初始化
        LocationManager.getInstance().start(getContext());

    }


    private void changeView(int from) {
        // TODO Auto-generated method stub
        if (birdlist.size() == 0) {
            if (from == 0) findNone.setText("没有查询到记录。");
            else findNone.setText("网络不佳，无法获取记录。");
            findNone.setVisibility(View.VISIBLE);
            records.setVisibility(View.GONE);
        } else {
            findNone.setVisibility(View.GONE);
            records.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            search = true;
            city = data.getStringExtra("city");
            lat = data.getDoubleExtra("latitude", 0.0);
            lon = data.getDoubleExtra("longtitude", 0.0);
            if (city != null && !city.equals("")) {
                choose_addr.setText(city.replace("市", ""));
            }
            getRecordNew(0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtil.getInstance().onRequestLocationionsResult(requestCode, permissions, grantResults)) {
            initLocClient();
        }
    }

    @Override
    public void onDestroyView() {
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        LocationManager.getInstance().stop();
        handler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent != null) {
                String action = intent.getAction();
                if (action != null) {
                    if (action.equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)) {
                        initLocClient();
                    }
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub229
        switch (v.getId()) {
            case R.id.choose_addr:
                startActivityForResult(new Intent(getActivity(), LocationActivity.class), 111);
                break;
            case R.id.record_search:
                if (city == null || city.equals("")) {
                    toast("位置不能为空！");
                    return;
                }
                if (CheckNetWork.isNetworkAvailable(getActivity())) {
                    if (!refreshed) {
                        progressDialog = MyAlertDialogUtil.showDialog(getActivity(), "正在查询记录，请稍后...");
                        birdlist.clear();
                        LogUtil.d("onClick" + refreshed);
                        refreshed = true;

                        getRecordNew(0);
                    }
                } else {
                    toast("请设置好网络条件。");
                }
                break;
            case R.id.choose_record:
                showPopupWindow();
                break;
        }
    }

    private void getRecordNew(final int skip) {
        if (city == null || city.equals("")) {
            toast("位置不能为空！");
            return;
        }

        if (skip == 0) {
            shareContent.setTitle(String.format(strShareTitle, range[range_pos]));
            shareContent.setContent(String.format(strShareContent, mLocationAddress, range[range_pos]));
        }
        GetRecordStats param = new GetRecordStats();
        param.setLatitude(String.valueOf(lat));
        param.setLongitude(String.valueOf(lon));
        param.setCity(city);
        param.setMonth(Integer.parseInt(month[month_pos].replace("月", "")));
        param.setRange(range[range_pos].replace("公里内", ""));
        param.setSkip(skip);
        param.setCount(20);
        LogUtil.d(TAG, param.toString());
        presenter.getRecord(param);

    }


    @SuppressLint({"InflateParams", "ClickableViewAccessibility"})
    private void showPopupWindow() {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.habit_view, null);

        TextView text1 = (TextView) view.findViewById(R.id.text1);
        MyGridView gridMonth = (MyGridView) view.findViewById(R.id.choose_colors);
        TextView text2 = (TextView) view.findViewById(R.id.text2);
        UiUtils.showView(text2,true);
        MyGridView gridRange = (MyGridView) view.findViewById(R.id.show_doing);
        UiUtils.showView(gridRange,true);
        TextView color_sure = (TextView) view.findViewById(R.id.color_sure);
        color_sure.setVisibility(View.GONE);
        view.findViewById(R.id.view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popup.dismiss();
            }
        });
        text1.setText("请选择月份");
        gridMonth.setAdapter(month_adapter);
        gridMonth.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                month_adapter.setSelect(month_pos);
                month_adapter.setSelect(position);
                choose_record.setText(month[position] + " | " + range[range_pos]);
                month_pos = position;
            }
        });
        text2.setText("请选择范围");
        gridRange.setVerticalSpacing(15);
        gridRange.setAdapter(range_adapter);
        gridRange.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                range_adapter.setSelect(range_pos);
                range_adapter.setSelect(position);
                choose_record.setText(month[month_pos] + " | " + range[position]);
                range_pos = position;
            }
        });
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int mScreenWidth = metric.widthPixels;
        int height = metric.heightPixels;
        popup = new PopupWindow(view, mScreenWidth, height);
        popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setTouchable(true);
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);
        popup.setBackgroundDrawable(new BitmapDrawable(getActivity().getResources(), ""));
        // 动画效果 从底部弹起
        popup.setAnimationStyle(R.style.GrowFromTop);
        popup.showAsDropDown(record_line);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
//        Intent intent = new Intent(getActivity(), BirdInfoActivity.class);
//        intent.putExtra("from", false);
//        intent.putExtra("Species", birdlist.get(position));
//        startActivity(intent);
        GotoActivityUtil.totoBirdDetailActivity(mContext,birdlist.get(position).getSpeciesID()+"");
    }

    /**
     * 定位返回处理
     *
     * @param event
     */
    public void onEventMainThread(EventLocation event) {

        //定位不处理
        if (!TextUtils.isEmpty(city) && !city.equals(LocationManager.DEFAULT_CITY)) {
            return;
        }


        //判断是否由编辑地图界面选择好地址了
        //如果由编辑地图选择地址，不处理
        if (search) {
            return;
        }

        EnumLocation enumLocation = event.getEnumLocation();
        switch (enumLocation) {
            case FAIL:
//                toast(event.getMsg());
                handler.sendEmptyMessage(2);
                break;
            case PREVENT:
                toast(event.getMsg());
                handler.sendEmptyMessage(2);
                break;
            case SUCCESS:
                WbLocation location = event.getLocation();
                mLocationAddress = location.getAddress();
                city = location.getCity();
                lat = location.getLatitude();
                lon = location.getLongitude();
                handler.sendEmptyMessage(1);
                break;
        }
    }

    @Override
    public void updateRecord(GetRecordStatsResp resp, int skip) {
        LogUtil.d("updateRecord:" + refreshed);
        refreshed = false;
        try {

            if (skip == 0) {
                birdlist.clear();

                if (null != resp.getSpecies() && !resp.getSpecies().isEmpty()) {
                    titleview.showRightImg(true);
                    setShareContent(resp);
                }
            }
            birdlist.addAll(resp.getSpecies());

            if (started) {
                started = false;
            }
            adapter.notifyDataSetChanged();
            changeView(0);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            changeView(1);
        }
        closeDialog();

    }

    private void setShareContent(GetRecordStatsResp resp) {

        shareContent.setHaveContent(true);
        shareContent.setUrl(resp.getShare_url());
        shareContent.setImgUrl(resp.getSpecies().get(0).getImageUrl());
    }

    private void closeDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void fail(VolleyError error) {
        LogUtil.d("fail:" + refreshed);
        refreshed = false;
        if (birdlist.size() <= 0) {
            changeView(1);
        }
        closeDialog();
    }

    @Override
    public void clickLeft() {
        getActivity().finish();
    }

    @Override
    public void clickRight() {

        ShareDialog dialog = new ShareDialog(getActivity());
        dialog.showShareDialog(shareContent);
    }
}
