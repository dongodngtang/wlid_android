package net.doyouhike.app.wildbird.ui.record;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.event.EventGetRecordDetail;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.biz.service.net.GetRecordDetailService;
import net.doyouhike.app.wildbird.ui.BirdPictureActivity;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.util.CheckNetWork;
import net.doyouhike.app.wildbird.util.LocalSharePreferences;
import net.doyouhike.app.wildbird.ui.view.TitleView;
import net.doyouhike.app.wildbird.ui.view.TitleView.ClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

@SuppressWarnings("deprecation")
public class LookRecordActivity extends BaseAppActivity implements ClickListener, OnGetGeoCoderResultListener {

    private TitleView titleview;
    private ViewPager viewpager;
    private TextView birduser, birdname, birdtime, birdaddr, birdcomment;
    private List<ImageView> list = null;
    private RecordEntity entity;
    private int recordID = 0;
    private GeoCoder mSearch = null;

    @SuppressLint({"HandlerLeak", "SimpleDateFormat"})
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    if (entity.getDescription() != null && !entity.getDescription().equals("NULL")) {
                        birdcomment.setText(entity.getDescription());
                    }
                    if (entity.getLocation() != null && !entity.getLocation().equals("") &&
                            !entity.getLocation().equals("NULL")) {
                        birdaddr.setVisibility(View.VISIBLE);
                        if (entity.getLatitude().intValue() == 0 &&
                                entity.getLongitude().intValue() == 0) {

                        } else {
                            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(
                                    new LatLng(entity.getLatitude(), entity.getLongitude())));
                        }
                    } else {
                        birdaddr.setVisibility(View.GONE);
                    }
                    // 图片
                    initList();
                    break;
                case 2:
                    birdname.setText(entity.getSpeciesName() + " x " + entity.getNumber());
                    if (entity.getLocation() != null && !entity.getLocation().equals("") &&
                            !entity.getLocation().equals("NULL")) {
                        birdaddr.setVisibility(View.VISIBLE);
                        birdaddr.setText(entity.getLocation());
                        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(
                                new LatLng(entity.getLatitude(), entity.getLongitude())));
                    } else {
                        birdaddr.setVisibility(View.GONE);
                    }
                    if (entity.getDescription() != null) {
                        birdcomment.setText(entity.getDescription());
                    }
                    Date date2 = new Date(entity.getTime() * 1000);
                    String datetime2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(date2);
                    birdtime.setText(datetime2);
                    break;
            }
        }

        ;
    };

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.look_record;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();

        EventBus.getDefault().register(this);
        titleview = (TitleView) super.findViewById(R.id.titleview);
        viewpager = (ViewPager) super.findViewById(R.id.viewpager);
        birduser = (TextView) super.findViewById(R.id.birduser);
        birdname = (TextView) super.findViewById(R.id.birdname);
        birdtime = (TextView) super.findViewById(R.id.birdtime);
        birdaddr = (TextView) super.findViewById(R.id.birdaddr);
        birdcomment = (TextView) super.findViewById(R.id.birdcomment);

        titleview.setListener(this);
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        entity = (RecordEntity) getIntent().getSerializableExtra("Record");
        handler.sendEmptyMessage(2);
        recordID = entity.getRecordID();
        if (recordID > 0) {
            if (CheckNetWork.isNetworkAvailable(this)) {
                mProgressDialog.showProgressDialog("正在获取鸟种记录，请稍候...");
                GetRecordDetailService.getInstance().getRecordDetail(recordID);
            }
        }
        initList();
    }


    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        GetRecordDetailService.getInstance().cancel();
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        // TODO Auto-generated method stub
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        birdaddr.setText(result.getAddress());
    }

    public void onEventMainThread(EventGetRecordDetail result) {
        mProgressDialog.dismissDialog();
        if (result.isSuc()) {
            entity = new RecordEntity(result.getResult());
            handler.sendEmptyMessage(1);
        } else {
            toast(result.getStrErrMsg());
        }
    }

    private void initList() {
        // TODO Auto-generated method stub
        if (list != null) list.clear();
        else list = new ArrayList<ImageView>();
        if (entity.getList().size() > 0) {
            for (int i = 0; i < entity.getList().size(); i++) {
                ImageView view = new ImageView(this);
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.u114)
                        .showImageOnFail(R.drawable.u114)
                        .cacheInMemory(true).cacheOnDisk(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();
                ImageLoader.getInstance().displayImage(entity.getList().get(i).getImageUri(), view, options);
                list.add(view);
            }
            String str = "图1/" + entity.getList().size();
            if (!LocalSharePreferences.getValue(getApplicationContext(), "userName").equals("")) {
                str += "：" + LocalSharePreferences.getValue(getApplicationContext(), "userName");
            }
            birduser.setText(str);
        } else {
            ImageView view = new ImageView(this);
            view.setImageResource(R.drawable.u114);
            list.add(view);
        }
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                // TODO Auto-generated method stub
                container.addView(list.get(position));
                list.get(position).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(LookRecordActivity.this, BirdPictureActivity.class);
                        intent.putExtra("hasImage", entity.getList().size() > 0);
                        intent.putExtra("name", entity.getSpeciesName());
                        intent.putExtra("myRecord", true);
                        intent.putExtra("index", position);
                        if (entity.getList().size() > 0) {
                            intent.putExtra("count", entity.getList().size());
                            for (int i = 0; i < entity.getList().size(); i++) {
                                intent.putExtra("image" + (i + 1), entity.getList().get(i).getImageUri());
                                intent.putExtra("author" + (i + 1), LocalSharePreferences.getValue(getApplicationContext(), "userName"));
                            }
                            startActivity(intent);
                        }
                    }
                });
                return list.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                // TODO Auto-generated method stub
                if (position >= list.size()) return;
                container.removeView(list.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                // TODO Auto-generated method stub
                return view == object;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return list.size();
            }
        });
        viewpager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                birduser.setText("图" + (arg0 + 1) + "/" + list.size() + ":" + LocalSharePreferences.getValue(getApplicationContext(), "userName"));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void clickLeft() {
        // TODO Auto-generated method stub
        finish();
    }

    @Override
    public void clickRight() {
        // TODO Auto-generated method stub
    }


}
