package net.doyouhike.app.wildbird.ui.main.birdinfo.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.doyouhike.app.library.ui.widgets.XSwipeRefreshLayout;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.Image;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.biz.model.bean.ShareContent;
import net.doyouhike.app.wildbird.biz.model.bean.SpeciesInfo;
import net.doyouhike.app.wildbird.biz.service.database.WildbirdDbService;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.ui.main.birdinfo.common.BirdPictureHelper;
import net.doyouhike.app.wildbird.ui.main.birdinfo.news.BirdNewsActivity;
import net.doyouhike.app.wildbird.ui.main.birdinfo.record.list.BirdRecordListActivity;
import net.doyouhike.app.wildbird.util.GotoActivityUtil;
import net.doyouhike.app.wildbird.util.ShareUtil;
import net.doyouhike.app.wildbird.util.SoundUtil;
import net.doyouhike.app.wildbird.util.UiUtils;
import net.doyouhike.app.wildbird.ui.view.ShareDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

/**
 * 野鸟详情
 */
public class BirdDetailActivity extends BaseAppActivity {

    /**
     * 野鸟id
     */
    public static final String I_SPECIES_ID = "param1";//speciesID
    /**
     * 鸟的名字
     */
    public static final String I_SPECIES_NAME = "param2";//speciesName

    private static final String FORMAT_SPECIES_ID = "%s-%s-%s";//鸟种种类格式化

    /**
     * 分享内容标题
     */
    private final String strShareTitle = "%s-中国野鸟速查";
    /**
     * 分享的内容格式化
     */
    private final String strShareContent = "我在野鸟APP学习%s%s鸟种的知识";
    /**
     * 分享相关封装实体类
     */
    private ShareContent shareContent;


    /**
     * 下拉刷新
     */
    @InjectView(R.id.refresh_activity_bird_detail)
    XSwipeRefreshLayout viRefresh;
    /**
     * 返回按钮
     */
    @InjectView(R.id.btn_act_bird_detail_back)
    RelativeLayout btnActBirdDetailBack;
    /**
     * 分享按钮
     */
    @InjectView(R.id.btn_act_bird_detail_share)
    RelativeLayout btnActBirdDetailShare;
    /**
     * 添加观鸟记录按钮
     */
    @InjectView(R.id.btn_act_bird_detail_add)
    RelativeLayout btnActBirdDetailAdd;
    /**
     * 鸟图片画廊
     */
    @InjectView(R.id.vp_bird_detail_pictures)
    ViewPager vpBirdDetailPictures;
    /**
     * 图片画廊指示
     */
    @InjectView(R.id.indicator_bird_detail_pictures)
    CircleIndicator indicatorBirdDetailPictures;
    /**
     * 图片数量
     */
    @InjectView(R.id.tv_bird_detail_pictures_size)
    TextView tvBirdDetailPicturesSize;
    /**
     * 鸟名
     */
    @InjectView(R.id.tv_bird_detail_local_name)
    TextView tvBirdDetailLocalName;
    /**
     * 叫声
     */
    @InjectView(R.id.tv_bird_detail_content_sound)
    TextView tvBirdSound;
    /**
     * 鸟声音
     */
    @InjectView(R.id.btn_act_bird_detail_sound)
    LinearLayout btnActBirdDetailSound;
    /**
     * 鸟声音
     */
    @InjectView(R.id.iv_act_bird_detail_sound)
    ImageView ivActBirdDetailSound;
    /**
     * 英文名字
     */
    @InjectView(R.id.tv_bird_detail_eng_name)
    TextView tvBirdDetailEngName;
    /**
     * 拉丁名字
     */
    @InjectView(R.id.tv_bird_detail_latin_name)
    TextView tvBirdDetailLatinName;
    /**
     * 观察记录数量
     */
    @InjectView(R.id.tv_bird_detail_record_quantity)
    TextView tvBirdDetailRecordQuantity;
    /**
     * 观察记录item
     */
    @InjectView(R.id.btn_act_bird_detail_record)
    LinearLayout btnActBirdDetailRecord;
    /**
     * 内容区域
     */
    @InjectView(R.id.btn_act_bird_detail_area)
    LinearLayout btnActBirdDetailArea;
    /**
     * 内容介绍指示
     */
    @InjectView(R.id.iv_bird_detail_content_show_indicate)
    ImageView ivBirdDetailContentShowIndicate;
    /**
     * 内容介绍item
     */
    @InjectView(R.id.btn_act_bird_detail_content)
    LinearLayout btnActBirdDetailContent;
    /**
     * 鸟种分类
     */
    @InjectView(R.id.tv_bird_detail_content_kind)
    TextView tvBirdDetailContentKind;
    /**
     * B2W名录
     */
    @InjectView(R.id.tv_bird_detail_content_b2w)
    TextView tvBirdDetailContentB2w;
    /**
     * cite等级
     */
    @InjectView(R.id.tv_bird_detail_content_cite)
    TextView tvBirdDetailContentCite;
    /**
     * rdb等级
     */
    @InjectView(R.id.tv_bird_detail_content_rdb)
    TextView tvBirdDetailContentRdb;
    /**
     * prot名录
     */
    @InjectView(R.id.tv_bird_detail_content_prot)
    TextView tvBirdDetailContentProt;
    /**
     * 描述
     */
    @InjectView(R.id.tv_bird_detail_content_description)
    TextView tvBirdDetailContentDescription;
    /**
     * 颜色
     */
    @InjectView(R.id.tv_bird_detail_content_color)
    TextView tvBirdDetailContentColor;
    /**
     * 分布范围
     */
    @InjectView(R.id.tv_bird_detail_content_range)
    TextView tvBirdDetailContentRange;
    /**
     * 分布状况
     */
    @InjectView(R.id.tv_bird_detail_content_distribution_sate)
    TextView tvBirdDetailContentDistributionSate;
    /**
     * 国际新闻
     */
    @InjectView(R.id.tv_bird_detail_content_international_news)
    TextView tvBirdDetailContentInternationalNews;
    /**
     * 鸟种内容介绍
     */
    @InjectView(R.id.vi_bird_detail_content)
    LinearLayout viBirdDetailContent;

    @OnClick(R.id.btn_act_bird_detail_content)
    public void showMoreContent() {
        //收展更多内容
        boolean isCurentShow = viBirdDetailContent.isShown();

        ivBirdDetailContentShowIndicate.setImageDrawable(isCurentShow ?
                getResources().getDrawable(R.drawable.ic_bird_detail_content_indicate_down) :
                getResources().getDrawable(R.drawable.ic_bird_detail_content_indicate_up));

        UiUtils.showView(viBirdDetailContent, !viBirdDetailContent.isShown());
    }

    @OnClick(R.id.btn_act_bird_detail_back)
    public void activityBack() {
        //返回
        this.finish();
    }

    @OnClick(R.id.btn_act_bird_detail_sound)
    public void playSound() {
        //播放声音
        if (null != mSoundUtil) {
            mSoundUtil.start();
        }
    }

    @OnClick(R.id.btn_act_bird_detail_add)
    public void onAddRecord() {
        //添加观鸟记录
        gotoAddRecordActivity();
    }

    @OnClick(R.id.btn_act_bird_detail_share)
    public void onShare() {
        //分享
        ShareDialog dialog = new ShareDialog(this);
        dialog.showShareDialog(shareContent);
    }

    @OnClick(R.id.tv_bird_detail_content_international_news)
    public void readNews() {
        //分享
        Bundle bundle = new Bundle();
        bundle.putString(I_SPECIES_ID, mBirdDetail.getSpeciesID());
        bundle.putString(BirdDetailActivity.I_SPECIES_NAME, mBirdDetail.getLocalName());
        readyGo(BirdNewsActivity.class, bundle);
    }

    @OnClick(R.id.btn_act_bird_detail_record)
    public void lookRecord() {
        //查看观鸟记录

        if (!isNetBirdData) {
            mPresenter.getDetail(mSpeciesId);
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(I_SPECIES_ID, mBirdDetail.getSpeciesID());
        bundle.putString(I_SPECIES_NAME, mBirdDetail.getLocalName());
        readyGo(BirdRecordListActivity.class, bundle);
    }

    @OnClick(R.id.btn_act_bird_detail_area)
    public void lookMap() {
        //查看地域图

        if (!isNetBirdData) {
            mPresenter.getDetail(mSpeciesId);
            return;
        }


        List<Image> images = new ArrayList<>();
        Image image = new Image(mBirdDetail.getDistribution_map(), "");
        images.add(image);


        GotoActivityUtil.gotoViewPictureActivity(this, null, 0, images);
    }

    private IBirdDetailPresenter mPresenter;
    private SpeciesInfo mBirdDetail;//鸟种详情
    /**
     * 鸟的id
     */
    private String mSpeciesId;
    /**
     * 是否已经获取到了鸟的数据
     */
    private boolean isNetBirdData = false;//是否网络数据
    SoundUtil mSoundUtil;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_bird_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();


        mSpeciesId = getIntent().getStringExtra(I_SPECIES_ID);
        //更新浏览信息
        WildbirdDbService.getInstance().updateReadInfo(mSpeciesId);

        mPresenter = new BirdDetailPresenterIml(this);

        //下拉刷新
        viRefresh.setColorSchemeColors(
                viRefresh.getResources().getColor(R.color.app_theme));
        viRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mPresenter.getDetail(mSpeciesId);
            }
        });

        mPresenter.getOfflineSpecies(mSpeciesId);
        mPresenter.getDetail(mSpeciesId);
//        viRefresh.setRefreshing(true);
    }

    /**
     * 更新数据
     * @param birdDetail 鸟种详情
     * @param fromNet 是否网络数据
     */
    public void updateDetail(SpeciesInfo birdDetail, boolean fromNet) {

        isNetBirdData = fromNet;

        mBirdDetail = birdDetail;
        updateViewContent();
    }

    public void setRefreshView(final boolean show) {
        viRefresh.post(new Runnable() {
            @Override
            public void run() {
                if (null != viRefresh)
                    viRefresh.setRefreshing(show);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareUtil.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();

        if (null != mSoundUtil) {
            mSoundUtil.stopPlay();
        }
        super.onDestroy();
    }

    /**
     * 绑定数据到控件
     */
    private void updateViewContent() {

        if (null == mBirdDetail) {
            return;
        }

        tvBirdDetailLocalName.setText(mBirdDetail.getLocalName());
        tvBirdDetailEngName.setText(mBirdDetail.getEngName());
        tvBirdDetailLatinName.setText(mBirdDetail.getLatinName());
        tvBirdDetailContentKind.setText(String.format(FORMAT_SPECIES_ID,
                mBirdDetail.getOrdo(), mBirdDetail.getFamilia(), mBirdDetail.getGenus()));

//        UiUtils.showView(viBirdDetailContent,isNetBirdData);
//        mBirdDetail.setAudio_file("http://abv.cn/music/光辉岁月.mp3");//测试数据

        UiUtils.showView(btnActBirdDetailShare, !TextUtils.isEmpty(mBirdDetail.getShare_url()));
        setShareContent();

        ivActBirdDetailSound.setEnabled(!TextUtils.isEmpty(mBirdDetail.getAudio_file()));
        if (!TextUtils.isEmpty(mBirdDetail.getAudio_file())) {
            mSoundUtil = new SoundUtil(this, mBirdDetail.getAudio_file());
        }

        initBirdPictures();

        tvBirdSound.setText(mBirdDetail.getCry());

        tvBirdDetailRecordQuantity.setText(mBirdDetail.getSpecies_record_count());
        tvBirdDetailContentB2w.setText(mBirdDetail.getB2w());
        tvBirdDetailContentCite.setText(mBirdDetail.getCites());
        tvBirdDetailContentRdb.setText(mBirdDetail.getRdb());
        tvBirdDetailContentProt.setText(mBirdDetail.getProt());
        tvBirdDetailContentDescription.setText(mBirdDetail.getDescription());
        tvBirdDetailContentColor.setText(mBirdDetail.getColor());
        tvBirdDetailContentRange.setText(mBirdDetail.getDist_range());
        tvBirdDetailContentDistributionSate.setText(mBirdDetail.getDist_candition());
        //是否有国际新闻
        UiUtils.showView(tvBirdDetailContentInternationalNews, "1".equals(mBirdDetail.getHas_news()));
    }

    /**
     * 设置图片
     */
    private void initBirdPictures() {

        if (mBirdDetail.getImages().isEmpty()) {
            Image image = new Image();
            image.setUrl("assets://" + mBirdDetail.getImage());
            mBirdDetail.addImage(image);
        }

        BirdPictureHelper pictureHelper = new BirdPictureHelper(vpBirdDetailPictures, indicatorBirdDetailPictures, tvBirdDetailPicturesSize);

        pictureHelper.initBirdPictures(this, mBirdDetail.getImages(), null);

    }

    /**
     * 跳转添加新的观鸟记录
     */
    private void gotoAddRecordActivity() {

        if (null == mBirdDetail) {
            return;
        }

        RecordEntity entity=new RecordEntity();
        entity.setSpeciesName(mBirdDetail.getLocalName());
        entity.setNumber(1);
        entity.setTime(System.currentTimeMillis()/1000);
        entity.setSpeciesID(Integer.valueOf(mBirdDetail.getSpeciesID()));

        GotoActivityUtil.gotoEditRecord(this,false,true,entity);
    }

    /**
     * 设置分享内容
     */
    private void setShareContent() {

        if (TextUtils.isEmpty(mBirdDetail.getShare_url())) {
            return;
        }

        shareContent = new ShareContent();
        shareContent.setUrl(mBirdDetail.getShare_url());
        shareContent.setHaveContent(true);

        shareContent.setTitle(String.format(strShareTitle, mBirdDetail.getLocalName()));
        shareContent.setContent(String.format(strShareContent, mBirdDetail.getLocalName(), mBirdDetail.getLatinName()));
        if (!mBirdDetail.getImages().isEmpty()) {
            shareContent.setImgUrl(mBirdDetail.getImages().get(0).getUrl());
        }
    }

}
