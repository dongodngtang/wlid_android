package net.doyouhike.app.wildbird.ui.view.scroll;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.bean.AreaRank;
import net.doyouhike.app.wildbird.biz.model.bean.PersonRank;
import net.doyouhike.app.wildbird.biz.model.bean.UserInfo;
import net.doyouhike.app.wildbird.ui.login.LoginActivity;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;
import net.doyouhike.app.wildbird.util.UiUtils;
import net.doyouhike.app.wildbird.ui.view.BaseScrollListView;

import butterknife.InjectView;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-7.
 */
public class RankingScrollListview extends BaseScrollListView implements View.OnClickListener {


    public interface RankingViewInterface {
        void checkout(boolean left);

        void isRefreshViewEnable(boolean isEnable);
    }

    @InjectView(R.id.vi_ranking_navigation_index_left)
    View viRankingNavigationIndexLeft;
    @InjectView(R.id.tv_ranking_navigation_title_left)
    TextView tvRankingNavigationTitleLeft;
    @InjectView(R.id.vi_ranking_navigation_index_right)
    View viRankingNavigationIndexRight;
    @InjectView(R.id.tv_ranking_navigation_title_right)
    TextView tvRankingNavigationTitleRight;
    @InjectView(R.id.iv_ranking_statistical_avatar)
    ImageView ivAvatar;
    @InjectView(R.id.tv_ranking_statistical_name)
    TextView tvRankingStatisticalName;
    @InjectView(R.id.tv_ranking_statistical_location)
    TextView tvRankingStatisticalLocation;
    @InjectView(R.id.tv_ranking_statistical_rank)
    TextView tvRankingStatisticalRank;
    @InjectView(R.id.tv_ranking_statistical_kind)
    TextView tvRankingStatisticalKind;


    private RankingViewInterface listener;


    public RankingScrollListview(Context context) {
        super(context);
    }

    public RankingScrollListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListener(RankingViewInterface listener) {
        this.listener = listener;
    }

    @Override
    protected int getPaddingHeight() {
        return 0;
    }

    @Override
    protected void onListScroll(boolean isTop) {
        listener.isRefreshViewEnable(isTop);
    }

    @Override
    protected View getHideView() {
        return View.inflate(getContext(), R.layout.layout_ranking_statistical, null);
    }

    @Override
    protected View getStickyView() {
        return View.inflate(getContext(), R.layout.layout_ranking_navigation, null);
    }

    @Override
    protected int getStickyViewHeight() {
        return UiUtils.getIntFromDimens(getContext(), R.dimen.ranking_navigation_height);
    }

    @Override
    protected int getHideViewHeight() {
        return UiUtils.getIntFromDimens(getContext(), R.dimen.ranking_statistical_height);
    }

    @Override
    protected void initAddView() {

    }

    @Override
    protected void initialize() {
        tvRankingNavigationTitleLeft.setOnClickListener(this);
        tvRankingNavigationTitleRight.setOnClickListener(this);

        setNaviBarSelected(true);
    }

    public void updatePersonRank(PersonRank rank, String location) {

        UiUtils.showView(tvRankingStatisticalLocation, !TextUtils.isEmpty(location));
        tvRankingStatisticalLocation.setText(location);

        updatePersonRank(rank);
    }
    public void updatePersonRank(PersonRank rank) {


        UiUtils.showView(tvRankingStatisticalLocation, false);


        updateUserInfo();


        if (null == rank) {
            tvRankingStatisticalRank.setText("-");
            tvRankingStatisticalKind.setText("-");
            return;
        }
        tvRankingStatisticalRank.setText(rank.getRank());
        tvRankingStatisticalKind.setText(rank.getSpecies_count());

    }

    /**
     * 更新用户信息
     */
    public void updateUserInfo() {
        UserInfo userInfo = UserInfoSpUtil.getInstance().getUserInfo();

        if (null != userInfo) {
            ImageLoaderUtil.getInstance().showAvatar(ivAvatar, userInfo.getAvatar());
            tvRankingStatisticalName.setText(userInfo.getUserName());
            ivAvatar.setOnClickListener(null);
            tvRankingStatisticalName.setOnClickListener(null);
        }else {
            ivAvatar.setOnClickListener(this);
            tvRankingStatisticalName.setOnClickListener(this);
        }
    }

    /**
     * 更新区域排行榜
     * @param rank
     * @param location
     */
    public void updateAreaRank(AreaRank rank, String location) {

        UiUtils.showView(tvRankingStatisticalLocation, !TextUtils.isEmpty(location));


        tvRankingStatisticalLocation.setText(location);

        updateUserInfo();


        if (null == rank) {
            return;
        }

        tvRankingStatisticalRank.setText(rank.getRank());
        tvRankingStatisticalKind.setText(rank.getSpecies_count());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ranking_navigation_title_left:
                setNaviBarSelected(true);
                listener.checkout(true);
                break;
            case R.id.tv_ranking_navigation_title_right:
                setNaviBarSelected(false);
                listener.checkout(false);
                break;
            case R.id.iv_ranking_statistical_avatar:
            case R.id.tv_ranking_statistical_name:
                v.getContext().startActivity(new Intent(v.getContext(), LoginActivity.class));
                break;
        }
    }


    private void setNaviBarSelected(boolean isLeft) {

        getPurLayMeList().setSelection(0);

        tvRankingNavigationTitleLeft.setSelected(isLeft);
        viRankingNavigationIndexLeft.setSelected(isLeft);


        viRankingNavigationIndexRight.setSelected(!isLeft);
        tvRankingNavigationTitleRight.setSelected(!isLeft);
    }
}
