package net.doyouhike.app.wildbird.ui.main.discovery.ranking.leaderboar;

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
import net.doyouhike.app.wildbird.biz.model.bean.LeaderboardItem;
import net.doyouhike.app.wildbird.biz.model.bean.PersonRank;
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

public class LeaderboardActivity extends BaseAppActivity implements ILeaderboardView
        , LeaderboardListHelper.DataHelperListener, RankingScrollListview.RankingViewInterface, TitleView.ClickListener {

    private static final String TITLE = "排行榜";
    private final String strShareTitle = "观鸟记录排行榜";
    private final String strShareContent = "我在野鸟APP的观鸟记录在全国排行第%s名了！";
    private String mCityName;
    private ShareContent mShareContent;

    @InjectView(R.id.sl_act_leaderboard_content)
    RankingScrollListview slActLeaderboardContent;
    @InjectView(R.id.rv_act_leaderboard_refresh)
    XSwipeRefreshLayout rvActLeaderboardRefresh;
    @InjectView(R.id.titleview_leaderboard)
    TitleView titleviewLeaderboard;

    ILeaderboardPresenter mPresenter;
    LeaderboardListHelper mListDataHelper;

    private PageBase mTotalPage;
    private PageBase mYearPage;
    private PageBase mCurrentPage;
    private PersonRank mYearPersonRank;
    private PersonRank mTotalPersonRank;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_leaderboard;
    }

    @Override
    protected View getLoadingTargetView() {
        return slActLeaderboardContent.getPurLayMeList();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
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
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();

        // 定位初始化
        LocationManager.getInstance().start(this);

        mPresenter = new LeaderboardPresenterImp(this);
        mTotalPage = new TotalPage(this);
        mYearPage = new YearPage(this);


        //设置不显示按钮
        titleviewLeaderboard.showRightImg(false);
        titleviewLeaderboard.setListener(this);

        mListDataHelper = new LeaderboardListHelper(slActLeaderboardContent.getPurLayMeList(), rvActLeaderboardRefresh,mTotalPage);
        mListDataHelper.setListener(this);

        slActLeaderboardContent.setListener(this);

        checkout(true);
    }

    @Override
    public void getListDate(BaseListGetParam param, boolean isRefresh) {

        if (isRefresh){
            if (null==mTotalPersonRank){
                mPresenter.getPersonalTotalRank();
            }

            if (null==mYearPersonRank){
                mPresenter.getPersonalYearRank();
            }
        }


        if (mCurrentPage ==mTotalPage){
            mPresenter.getTotal(param);
        }else if (mCurrentPage == mYearPage){
            mPresenter.getYear(param);
        }

    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestory();
        mListDataHelper.setListener(null);
        mListDataHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setViewState(UiState state,View.OnClickListener listener) {
        slActLeaderboardContent.setContentViewEmpty(mCurrentPage.getItems().isEmpty());
        updateView(state, listener);
    }

    @Override
    public void clickLeft() {
        finish();
    }

    @Override
    public void clickRight() {
        //分享
        if(null==mShareContent){
            return;
        }
        ShareDialog dialog = new ShareDialog(this);
        dialog.showShareDialog(mShareContent);
    }
    private void setCurrentPage(PageBase page) {

        mCurrentPage = page;
        mListDataHelper.setPage(mCurrentPage);
    }

    @Override
    public void checkout(boolean left) {

        slActLeaderboardContent.updatePersonRank(left ? mTotalPersonRank : mYearPersonRank);

        titleviewLeaderboard.setTitle(left ? TITLE : Calendar.getInstance().get(Calendar.YEAR) + "年" + TITLE);
        setCurrentPage(left ? mTotalPage : mYearPage);
    }

    @Override
    public void isRefreshViewEnable(boolean isEnable) {
        rvActLeaderboardRefresh.setEnabled(isEnable);
    }

    @Override
    public void onGetPersonalTotalRank(PersonRank rank) {
        mTotalPersonRank=rank;
        updateUserInfo();
        setShareContent();
    }

    @Override
    public void onGetPersonalYearRank(PersonRank rank) {
        mYearPersonRank=rank;
        updateUserInfo();
    }

    @Override
    public void onGetTotalErr(VolleyError msg) {

        mTotalPage.getDataErr(msg);

        if (mCurrentPage == mTotalPage) {
            mListDataHelper.onError(msg);
        }

    }

    @Override
    public void onGetYearErr(VolleyError msg) {

        mYearPage.getDataErr(msg);

        if (mCurrentPage == mYearPage) {
            mListDataHelper.onError(msg);
        }
    }

    @Override
    public void onGetYearList(List<LeaderboardItem> yearItems, boolean isRefresh) {

        mYearPage.updateItem(yearItems, isRefresh);


        if (mCurrentPage == mYearPage)
            mListDataHelper.onResponse(isRefresh);

    }

    @Override
    public void onGetTotalList(List<LeaderboardItem> totalItems, boolean isRefresh) {

        mTotalPage.updateItem(totalItems, isRefresh);

        if (mCurrentPage == mTotalPage)
            mListDataHelper.onResponse(isRefresh);
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
                mCityName=location.getCity();
                setShareContent();
                break;
        }

    }

    private void updateUserInfo(){

        slActLeaderboardContent.updatePersonRank(mCurrentPage == mTotalPage ? mTotalPersonRank : mYearPersonRank);
    }


    /**
     * 设置分享内容
     * @return
     */

    private void setShareContent() {


        if (null== UserInfoSpUtil.getInstance().getUserInfo()){
            return ;
        }


        String strShareUrl=UserInfoSpUtil.getInstance().getUserInfo().getPerson_rank_share_url();

        if (TextUtils.isEmpty(strShareUrl)) {
            return ;
        }

        if (TextUtils.isEmpty(mCityName)) {
            return ;
        }

        if (null==mTotalPersonRank){
            return;
        }


        mShareContent = new ShareContent();
        mShareContent.setUrl(strShareUrl+mCityName);
        mShareContent.setHaveContent(true);

        mShareContent.setTitle(strShareTitle);
        mShareContent.setContent(String.format(strShareContent,mTotalPersonRank.getRank()));

        titleviewLeaderboard.showRightImg(true);
    }
}
