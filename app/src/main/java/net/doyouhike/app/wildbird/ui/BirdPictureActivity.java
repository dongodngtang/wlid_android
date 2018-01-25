package net.doyouhike.app.wildbird.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.library.PhotoView;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.BirdPictureUserInfo;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;
import net.doyouhike.app.wildbird.util.TimeUtil;
import net.doyouhike.app.wildbird.util.UiUtils;
import net.doyouhike.app.wildbird.ui.view.TitleView;
import net.doyouhike.app.wildbird.ui.view.dialog.BottomDialogList;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BirdPictureActivity extends BaseAppActivity implements TitleView.ClickListener {


    public static final String I_TITLE = BirdPictureActivity.class.getSimpleName() + "param1";
    public static final String I_USERINFO = BirdPictureActivity.class.getSimpleName() + "param2";
    public static final String I_INDEX = BirdPictureActivity.class.getSimpleName() + "param3";
    public static final String I_COUNT = BirdPictureActivity.class.getSimpleName() + "param4";
    public static final String I_HAS_IMG = BirdPictureActivity.class.getSimpleName() + "param5";
    public static final String I_IMAGE = BirdPictureActivity.class.getSimpleName() + "param6";
    public static final String I_AUTHOR = BirdPictureActivity.class.getSimpleName() + "param7";

    @InjectView(R.id.titleview)
    TitleView titleview;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.iv_bird_picture_avatar)
    ImageView ivBirdPictureAvatar;
    @InjectView(R.id.tv_bird_picture_user_name)
    TextView tvBirdPictureUserName;
    @InjectView(R.id.tv_bird_picture_location)
    TextView tvLocation;
    @InjectView(R.id.tv_bird_picture_date)
    TextView tvBirdPictureDate;
    @InjectView(R.id.vi_bird_picture_user_info)
    RelativeLayout viBirdPictureUserInfo;

    private List<View> list = new ArrayList<View>();
    private String mUserName;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_bird_picture_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();

        BirdPictureUserInfo userInfo = (BirdPictureUserInfo) getIntent().getSerializableExtra(I_USERINFO);

        UiUtils.showView(viBirdPictureUserInfo, userInfo != null);

        if (userInfo != null) {
            ImageLoaderUtil.getInstance().showAvatar(ivBirdPictureAvatar, userInfo.getAvatar());
            tvBirdPictureUserName.setText(userInfo.getUserName());
            mUserName=userInfo.getUserName();
            tvLocation.setText(userInfo.getLocation());
            tvBirdPictureDate.setText(TimeUtil.getFormatTimeFromTimestamp(userInfo.getTime() * 1000, "yyyy-MM-dd"));
        }

        if (getIntent().getBooleanExtra(I_HAS_IMG, false)) {

            int count = getIntent().getIntExtra(I_COUNT, 0);
            for (int i = 0; i < count; i++) {
//                final String uri ="http://pic36.nipic.com/20131225/11221033_091328313177_2.jpg";
                final String uri = (String) getIntent().getSerializableExtra(I_IMAGE + (i + 1));
                String author = getIntent().getStringExtra(I_AUTHOR + (i + 1));

                final String waterMarkTitle=TextUtils.isEmpty(mUserName)?author:mUserName;


                View view = View.inflate(this, R.layout.layout_bird_imageview, null);
                View parent = view.findViewById(R.id.bird_image_parent);
                PhotoView image = (PhotoView) view.findViewById(R.id.birdImage);
                // 启用图片缩放功能
                image.enable();
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BottomDialogList bottomDialog = new BottomDialogList(v.getContext(), new BottomDialogList.BottomDialogListener() {
                            @Override
                            public void onItemSelected(int position) {
                                ImageLoaderUtil.getInstance().savePicture(uri, waterMarkTitle);
                            }

                            @Override
                            public void onDismiss() {

                            }
                        }, "保存图片");
                        bottomDialog.show();
                    }
                });
                TextView text = (TextView) view.findViewById(R.id.birdText);
                ImageLoaderUtil.getInstance().showImg(image, uri);
                UiUtils.showView(text, !TextUtils.isEmpty(author));

                if (!TextUtils.isEmpty(author)) {
                    text.setText("作者：" + author);
                }
                list.add(view);
            }
        } else {
            ImageView view = new ImageView(this);
            view.setImageResource(R.drawable.u114);
            list.add(view);
        }
        titleview.setTitle("1/" + list.size());
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(list.get(position));
                return list.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
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
                if (list.size() > 1) {
                    titleview.setTitle((arg0 + 1) + " / " + list.size());
                }
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
        viewpager.setCurrentItem(getIntent().getIntExtra(I_INDEX, 0), false);
    }

    @Override
    public void clickLeft() {
        finish();
    }

    @Override
    public void clickRight() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.inject(this);
    }
}
