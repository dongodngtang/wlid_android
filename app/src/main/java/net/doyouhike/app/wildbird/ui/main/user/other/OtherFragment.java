package net.doyouhike.app.wildbird.ui.main.user.other;

import android.content.Context;
import android.os.Bundle;

import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordTotalItem;
import net.doyouhike.app.wildbird.biz.model.bean.MyRecord;
import net.doyouhike.app.wildbird.ui.main.birdinfo.record.list.BirdRecordPage;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;
import net.doyouhike.app.wildbird.ui.main.user.UserFragment;

/**
 * 功能：他人他人主页
 *
 * @author：曾江 日期：16-4-14.
 */
public class OtherFragment extends UserFragment {

    /**
     * 用户id
     */
    private final static String PARAM_USER_ID = "params1";
    /**
     * 用户名字
     */
    private final static String PARAM_USER_NAME = "params2";

    IOtherPresenter mPresenter;
    private String mUserId;
    private String mUserName;

    public static OtherFragment  getInstance(String userId, String userName) {

        OtherFragment otherFragment = new OtherFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_USER_ID, userId);
        bundle.putString(PARAM_USER_NAME, userName);
        otherFragment.setArguments(bundle);
        return otherFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserId = getArguments().getString(PARAM_USER_ID);
        mUserName = getArguments().getString(PARAM_USER_NAME);
    }

    @Override
    protected void initViewsAndEvents() {
        mPresenter = new OtherPresenterImp(this);
        super.initViewsAndEvents();
        mTitleView.setTitle(mUserName);
        mPresenter.getUserInfo(mUserId);
        mListHelper.getData(true);
    }


    @Override
    public void onDestroyView() {
        mPresenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.getUserInfo(mUserId);
    }


    @Override
    protected PageBase<BirdRecordTotalItem> getPage(Context context) {
        return new OtherPage(context,mUserId);
    }

}
