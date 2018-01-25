package net.doyouhike.app.wildbird.ui.main.birdinfo.record.detail;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.base.BaseListActivity;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.bean.BirdPictureUserInfo;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordDetailCommentItem;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordDetailItem;
import net.doyouhike.app.wildbird.biz.model.response.GetBirdRecordCommentResponse;
import net.doyouhike.app.wildbird.biz.model.response.GetRecordDetailResp;
import net.doyouhike.app.wildbird.ui.main.birdinfo.common.BirdPictureHelper;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;
import net.doyouhike.app.wildbird.util.GetCityIDUtils;
import net.doyouhike.app.wildbird.util.GotoActivityUtil;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;
import net.doyouhike.app.wildbird.util.TimeUtil;
import net.doyouhike.app.wildbird.util.UiUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.relex.circleindicator.CircleIndicator;

/**
 * 观鸟记录详情
 */
public class BirdRecordDetailActivity extends BaseListActivity<BirdRecordDetailCommentItem> implements View.OnClickListener {

    public static final String I_RECORD_ITEM = "ParamRecordItem";

    /**
     * 添加评论
     */
    @InjectView(R.id.edt_bird_record_detail_add_comment)
    EditText edtBirdRecordDetailAddComment;
    /**
     * 点赞
     */
    @InjectView(R.id.tv_bird_record_detail_add_zan)
    TextView tvBirdRecordDetailAddZan;


    /**
     * 图片装载器
     */
    ViewPager vpBirdDetailPictures;
    /**
     * 圆点指示
     */
    CircleIndicator indicatorBirdDetailPictures;
    /**
     * 图片数量
     */
    TextView tvBirdDetailPicturesSize;
    /**
     * 鸟名 x 3
     */
    TextView tvBirdRecordDetailNameCount;
    /**
     * 位置
     */
    TextView tvBirdRecordDetailLocation;
    /**
     * 头像
     */
    ImageView ivBirdRecordDetailAvatar;
    /**
     * 用户名字
     */
    TextView tvBirdRecordDetailUserName;
    /**
     * 时间
     */
    TextView tvBirdRecordDetailTime;
    /**
     * 观鸟记录
     */
    TextView tvBirdRecordDetailContent;
    /**
     * 评论数量
     */
    TextView tvBirdRecordDetailCommentCount;
    /**
     * 观鸟记录概况,由上级菜单传入
     */
    private BirdRecordDetailItem mRecordDetailItem;
    /**
     * 观鸟记录详情
     */
    private GetRecordDetailResp mRecordDetail;

    IRecordDetailPresenter mPresenter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_bird_record_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        //获取观鸟记录概况
        mRecordDetailItem = (BirdRecordDetailItem) getIntent().getSerializableExtra(I_RECORD_ITEM);

        if (null == mRecordDetailItem) {
            updateView(UiState.ERROR.setMsg("获取记录失败."));
            return;
        }
        //绑定观鸟记录详情,listview为评论信息,而观鸟记录信息为listview的头部
        View headView = View.inflate(this, R.layout.layout_bird_record_detail_content, null);
        initView(headView);
        //设置标题
        mTitleView.setTitle(mRecordDetailItem.getUser_name() + "的观鸟记录");

        lvLoadMore.addHeaderView(headView);

        mPresenter = new RecordDetailPresenterImp(this);

        //放在后面为了先完成初始化,再自动请求评论列表
        super.initViewsAndEvents();
        //展示个人信息
        ImageLoaderUtil.getInstance().showAvatar(ivBirdRecordDetailAvatar, mRecordDetailItem.getAvatar());
        UiUtils.setText(tvBirdRecordDetailUserName, mRecordDetailItem.getUser_name());
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    private void initView(View headView) {

        vpBirdDetailPictures = ButterKnife.findById(headView, R.id.vp_bird_detail_pictures);
        indicatorBirdDetailPictures = ButterKnife.findById(headView, R.id.indicator_bird_detail_pictures);
        tvBirdDetailPicturesSize = ButterKnife.findById(headView, R.id.tv_bird_detail_pictures_size);
        tvBirdRecordDetailNameCount = ButterKnife.findById(headView, R.id.tv_bird_record_detail_name_count);
        tvBirdRecordDetailLocation = ButterKnife.findById(headView, R.id.tv_bird_record_detail_location);
        ivBirdRecordDetailAvatar = ButterKnife.findById(headView, R.id.iv_bird_record_detail_avatar);

        tvBirdRecordDetailUserName = ButterKnife.findById(headView, R.id.tv_bird_record_detail_user_name);
        tvBirdRecordDetailTime = ButterKnife.findById(headView, R.id.tv_bird_record_detail_time);
        tvBirdRecordDetailContent = ButterKnife.findById(headView, R.id.tv_bird_record_detail_content);
        tvBirdRecordDetailCommentCount = ButterKnife.findById(headView, R.id.tv_bird_record_detail_comment_count);

        tvBirdRecordDetailAddZan.setOnClickListener(this);
        ivBirdRecordDetailAvatar.setOnClickListener(this);
        tvBirdRecordDetailUserName.setOnClickListener(this);


        //留言编辑发送
        edtBirdRecordDetailAddComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String content = edtBirdRecordDetailAddComment.getText().toString();
                    if (TextUtils.isEmpty(content)) {
                        //空值判断
                        return false;
                    }


                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            edtBirdRecordDetailAddComment.getWindowToken(), 0);

                    //发送评论
                    mPresenter.sendComment(mRecordDetailItem.getRecord_id()+"", content);
                    return true;

                }


                return false;
            }
        });

    }


    @Override
    public void updateView(UiState state, View.OnClickListener onClickListener) {

        //加载中\正常视图不屏蔽,错误视图,且鸟记录详情为空不屏蔽,采用app默认样式
        if (state == UiState.LOADING_DIALOG || state == UiState.NORMAL || state == UiState.LOADING ||
                (state == UiState.ERROR||state == UiState.NETERR&&null==mRecordDetail) ){
            super.updateView(state, onClickListener);
            return;
        }


        if (null == state.getMsg()) {
            return;
        }
        //评论列表为空状态,不提醒
        if  (state == UiState.EMPTY&&mListHelper.getItems().isEmpty()){
            return;
        }
        //最后情况是,评论列表加载出错,提示
        showToast(state.getMsg()[0]);


    }

    /**
     * 评论成功回调
     * @param item 评论内容
     */
    public void onAddCommentSuc(BirdRecordDetailCommentItem item) {

        if (mRecordDetail != null) {
            mRecordDetail.setComment_count(mRecordDetail.getComment_count() + 1);
            setRecordDetail(mRecordDetail);
        }

        edtBirdRecordDetailAddComment.setText(null);
        mListHelper.getItems().add(0, item);
        mListHelper.getAdapter().notifyDataSetChanged();
    }

    /**
     * 点赞成功回调
     */
    public void onAddZanSuc() {

        if (mRecordDetail != null) {
            mRecordDetail.setLike_count(mRecordDetail.getLike_count() + 1);
            setRecordDetail(mRecordDetail);
            updateLikeState(true);
        }
    }

    /**
     * 绑定详情数据到控件
     *
     * @param detail 记录详情
     */
    public void setRecordDetail(GetRecordDetailResp detail) {

        mRecordDetail = detail;

        if (null == mRecordDetail) {
            return;
        }


        String cityName = GetCityIDUtils.getCity(detail.getLocation().getCityID());

        //设置图片信息
        BirdPictureHelper pictureHelper = new BirdPictureHelper(
                vpBirdDetailPictures,
                indicatorBirdDetailPictures,
                tvBirdDetailPicturesSize);

        //设置图片详情的用户信息
        BirdPictureUserInfo userInfo = new BirdPictureUserInfo();
        userInfo.setAvatar(mRecordDetailItem.getAvatar());
        userInfo.setLocation(cityName);
        userInfo.setUserName(mRecordDetailItem.getUser_name());
        userInfo.setTime(mRecordDetail.getTime());
        //绑定图片列表到图片viewpager
        pictureHelper.initBirdPictures(this, pictureHelper.toImags(detail.getImages()), userInfo);

        //图片数量
        tvBirdDetailPicturesSize.setText(String.valueOf(detail.getImages().size()));
        //鸟名 x 数量
        tvBirdRecordDetailNameCount.setText(String.format("%s x %s", detail.getSpeciesName(), detail.getNumber()));
        //城市
        tvBirdRecordDetailLocation.setText(cityName);
        //城市为空不显示,主要是为了屏蔽图标
        UiUtils.showView(tvBirdRecordDetailLocation,!TextUtils.isEmpty(cityName));


        //  显示头像
        ImageLoaderUtil.getInstance().showAvatar(ivBirdRecordDetailAvatar, mRecordDetailItem.getAvatar());
        tvBirdRecordDetailUserName.setText(mRecordDetailItem.getUser_name());
        tvBirdRecordDetailTime.setText(TimeUtil.getFormatTimeFromTimestamp(detail.getTime() * 1000, "yyyy-MM-dd"));
        tvBirdRecordDetailContent.setText(detail.getDescription());
        tvBirdRecordDetailCommentCount.setText(String.format("留言%d条", detail.getComment_count()));
        tvBirdRecordDetailAddZan.setText(detail.getLike_count() + "");
        //更新点赞状态,大于0为已经点赞
       updateLikeState(detail.getIs_like() > 0);
    }

    /**
     * @param isLike true为已点赞
     */
    public void updateLikeState(boolean isLike){
        tvBirdRecordDetailAddZan.setSelected(isLike);
    }

    @Override
    protected PageBase<BirdRecordDetailCommentItem> getPage(Context context) {
        BirdRecordDetailCommentPage page = new BirdRecordDetailCommentPage(context);
        page.getRequestParam().setRecord_id(mRecordDetailItem.getRecord_id()+"");
        return page;
    }

    @Override
    public void onRefresh() {
        mPresenter.getRecordDetail(mRecordDetailItem.getRecord_id()+"");
    }

    @Override
    protected List<BirdRecordDetailCommentItem> responseToItems(BaseResponse response) {

        return ((CommonResponse<GetBirdRecordCommentResponse>) response).getT().getComment_list();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bird_record_detail_add_zan:

                if (tvBirdRecordDetailAddZan.isSelected()) {
                    return;
                }
                mPresenter.doLike(mRecordDetailItem.getRecord_id()+"");
                break;
            case R.id.iv_bird_record_detail_avatar:
            case R.id.tv_bird_record_detail_user_name:
                GotoActivityUtil.gotoOtherActivity(this, mRecordDetailItem.getUser_id(), mRecordDetailItem.getUser_name());
                break;
        }
    }

}
