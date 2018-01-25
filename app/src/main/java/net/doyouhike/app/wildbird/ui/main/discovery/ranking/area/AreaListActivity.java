package net.doyouhike.app.wildbird.ui.main.discovery.ranking.area;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.VolleyError;

import net.doyouhike.app.library.ui.netstatus.NetUtils;
import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.library.ui.widgets.XSwipeRefreshLayout;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;
import net.doyouhike.app.wildbird.biz.model.bean.PersonRank;
import net.doyouhike.app.wildbird.biz.model.bean.RankAreaItem;
import net.doyouhike.app.wildbird.biz.model.bean.ShareContent;
import net.doyouhike.app.wildbird.biz.model.bean.WbLocation;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.LeaderboardListHelper;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;
import net.doyouhike.app.wildbird.util.ShareUtil;
import net.doyouhike.app.wildbird.util.location.EnumLocation;
import net.doyouhike.app.wildbird.util.location.EventLocation;
import net.doyouhike.app.wildbird.util.location.LocationManager;
import net.doyouhike.app.wildbird.ui.view.ShareDialog;
import net.doyouhike.app.wildbird.ui.view.TitleView;
import net.doyouhike.app.wildbird.ui.view.scroll.RankingScrollListview;

import java.util.Calendar;
import java.util.List;

import butterknife.InjectView;

public class AreaListActivity extends BaseAppActivity implements TitleView.ClickListener
        , LeaderboardListHelper.DataHelperListener, RankingScrollListview.RankingViewInterface {


    private static final String TITLE = "区域榜";
    private final String strShareTitle = "全国观鸟记录分布";
    private final String strShareContent = "我在野鸟APP查询到全国各省份的观鸟记录排行榜";

    private ShareContent mShareContent;



    @InjectView(R.id.sl_act_leaderboard_content)
    RankingScrollListview slActLeaderboardContent;
    @InjectView(R.id.rv_act_leaderboard_refresh)
    XSwipeRefreshLayout rvActLeaderboardRefresh;
    @InjectView(R.id.titleview_leaderboard)
    TitleView titleviewLeaderboard;

    IAreaListPresenter mPresenter;
    LeaderboardListHelper mListDataHelper;

    private PageBase mTotalPage;
    private PageBase mYearPage;
    private PageBase mCurrentPage;
    private PersonRank mYearAreaRank;
    private PersonRank mTotalAreaRank;

    private String mStrLocation;
    private String mProvinceId;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_leaderboard;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected View getLoadingTargetView() {
        return slActLeaderboardContent.getPurLayMeList();
    }


    @Override
    public void clickLeft() {
        finish();
    }

    @Override
    public void clickRight() {

        if (null==mShareContent){
            return;
        }

        //分享
        ShareDialog dialog = new ShareDialog(this);
        dialog.showShareDialog(mShareContent);
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();

        // 定位初始化
        LocationManager.getInstance().start(this);

        titleviewLeaderboard.setTitle(TITLE);
        titleviewLeaderboard.setListener(this);
        titleviewLeaderboard.showRightImg(false);

        mPresenter = new RankAreaPersenterImp(this);
        mTotalPage = new TotalPage(this);
        mYearPage = new YearPage(this);

        mListDataHelper = new LeaderboardListHelper(slActLeaderboardContent.getPurLayMeList(), rvActLeaderboardRefresh, mTotalPage);
        mListDataHelper.setListener(this);

        slActLeaderboardContent.setListener(this);

        checkout(true);

    }

    @Override
    public void getListDate(BaseListGetParam param, boolean isRefresh) {

        if (isRefresh) {
            getStatisticsData();
        }


        if (mCurrentPage == mTotalPage) {
            mPresenter.getTotal(param);
        } else if (mCurrentPage == mYearPage) {
            mPresenter.getYear(param);
        }
    }


    @Override
    public void setViewState(UiState state,View.OnClickListener listener) {
        slActLeaderboardContent.setContentViewEmpty(mCurrentPage.getItems().isEmpty());
        updateView(state, listener);
    }

    private void setCurrentPage(PageBase page) {

        mCurrentPage = page;
        mListDataHelper.setPage(mCurrentPage);
    }

    @Override
    public void checkout(boolean left) {

        slActLeaderboardContent.updatePersonRank(left ? mTotalAreaRank : mYearAreaRank, mStrLocation);
        setCurrentPage(left ? mTotalPage : mYearPage);

        titleviewLeaderboard.setTitle(left?TITLE: Calendar.getInstance().get(Calendar.YEAR)+"年"+TITLE);
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
        if (type!= NetUtils.NetType.NONE){
            checkout(true);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        slActLeaderboardContent.updateUserInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareUtil.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void isRefreshViewEnable(boolean isEnable) {
        rvActLeaderboardRefresh.setEnabled(isEnable);
    }

    public void onGetPersonalTotalRank(PersonRank rank) {
        mTotalAreaRank = rank;
        updateAreaRankInfo();
    }

    public void onGetPersonalYearRank(PersonRank rank) {
        mYearAreaRank = rank;
        updateAreaRankInfo();
    }

    @Override
    protected void onDestroy() {
        LocationManager.getInstance().stop();
        mListDataHelper.setListener(null);
        mListDataHelper.onDestroy();
        mPresenter.onDestory();
        super.onDestroy();
    }

    public void onGetTotalErr(VolleyError msg) {

        mTotalPage.getDataErr(msg);

        if (mCurrentPage == mTotalPage) {
            mListDataHelper.onError(msg);
        }
    }

    public void onGetYearErr(VolleyError msg) {

        mYearPage.getDataErr(msg);

        if (mCurrentPage == mYearPage) {
            mListDataHelper.onError(msg);
        }
    }

    public void onGetYearList(List<RankAreaItem> yearItems, boolean isRefresh) {

        mYearPage.updateItem(yearItems, isRefresh);


        if (mCurrentPage == mYearPage)
            mListDataHelper.onResponse(isRefresh);

    }

    public void onGetTotalList(List<RankAreaItem> totalItems, boolean isRefresh) {

        mTotalPage.updateItem(totalItems, isRefresh);

        if (mCurrentPage == mTotalPage)
            mListDataHelper.onResponse(isRefresh);
    }

    private void updateAreaRankInfo() {
        slActLeaderboardContent.updatePersonRank(mCurrentPage == mTotalPage ? mTotalAreaRank : mYearAreaRank, mStrLocation);
    }


    /**
     * 获取统计数据
     */
    private void getStatisticsData() {
        if (null == mTotalAreaRank) {
            mPresenter.getPersonalTotalRank();
        }

        if (null == mYearAreaRank) {
            mPresenter.getPersonalYearRank();
        }
    }
    /**
     * 定位返回处理
     */
    public void onEventMainThread(EventLocation event) {


        EnumLocation enumLocation = event.getEnumLocation();


        switch (enumLocation) {
            case FAIL:
//                toast(event.getMsg());
                break;
            case PREVENT:
                toast(event.getMsg());
                break;
            case SUCCESS:
                WbLocation location = event.getLocation();
//
                mProvinceId = location.getProvinceCode();
//                if (!TextUtils.isEmpty(cityCode)) {
//                    entity.setCityID(Integer.parseInt(location.getCityCode()));
//                }

                mStrLocation=location.getCity();
                setShareContent();
                updateAreaRankInfo();
                getStatisticsData();
                break;
        }

    }

    private void setShareContent() {


        if (null== UserInfoSpUtil.getInstance().getUserInfo()){
            return ;
        }


        String strShareUrl=UserInfoSpUtil.getInstance().getUserInfo().getArea_rank_share_url();

        if (TextUtils.isEmpty(strShareUrl)) {
            return ;
        }

        if (TextUtils.isEmpty(mStrLocation)) {
            return ;
        }


        mShareContent = new ShareContent();
        mShareContent.setUrl(strShareUrl+mStrLocation);
        mShareContent.setHaveContent(true);

        mShareContent.setTitle(strShareTitle);
        mShareContent.setContent(strShareContent);

        titleviewLeaderboard.showRightImg(true);
    }
}
