package net.doyouhike.app.wildbird.ui.main.user;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.doyouhike.app.library.ui.uistate.UiStateController;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordTotalItem;
import net.doyouhike.app.wildbird.biz.model.bean.MyRecord;
import net.doyouhike.app.wildbird.biz.model.bean.RecStatsEntity;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.biz.model.bean.UserInfo;
import net.doyouhike.app.wildbird.biz.model.response.GetBirdRecordResponse;
import net.doyouhike.app.wildbird.biz.model.response.GetMyRecordResp;
import net.doyouhike.app.wildbird.ui.base.BaseListFragment;
import net.doyouhike.app.wildbird.ui.main.birdinfo.record.list.BirdRecordAdapter;
import net.doyouhike.app.wildbird.ui.main.user.mine.MePage;
import net.doyouhike.app.wildbird.ui.main.user.mine.MyRecordAdapter;
import net.doyouhike.app.wildbird.ui.main.user.mine.MyRecordList;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;

import java.util.Collections;
import java.util.List;

import butterknife.InjectView;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-13.
 */
public abstract class UserFragment extends BaseListFragment<BirdRecordTotalItem> {

    @InjectView(R.id.pb_frag_mine_refresh)
    ProgressBar pbFragMineRefresh;
    /**
     * 头像
     */
    @InjectView(R.id.avatar)
    protected ImageView ivAvatar;
    /**
     * 点击这里登录
     */
    @InjectView(R.id.notlogin)
   protected TextView notlogin;
    /**
     * 总记录
     */
    @InjectView(R.id.all_record)
    TextView allRecord;
    /**
     * 总鸟种
     */
    @InjectView(R.id.all_birds)
    TextView allBirds;
    /**
     * 今年-次-种
     */
    @InjectView(R.id.this_year)
    TextView thisYear;
    /**
     * 排行榜
     */
    @InjectView(R.id.tv_me_ranking)
    protected TextView tvRanking;
    /**
     * 观鸟信息
     */
    @InjectView(R.id.mine_info)
    protected LinearLayout mineInfo;

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

        lvLoadMore.setBackgroundResource(R.color.white);
        mListHelper = getListHelper(lvLoadMore, viRefresh, this, getPage(getContext()));
        uiStateController = new UiStateController(new UserUiHandler(lvLoadMore, mListHelper.getAdapter()));

    }

    @Override
    protected View getHeadView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_frag_mine_info, null);
    }




    public void updateUserInfo(UserInfo info) {


        //info == null ? "drawable://" + R.drawable.avatar 为了切换账号时,重置默认头像
        ImageLoaderUtil.getInstance().showAvatar(ivAvatar, info == null ? "drawable://" + R.drawable.avatar : info.getAvatar());

        if (info == null) {
            return;
        }


        mTitleView.setTitle(info.getUserName());

        ((UserRecordAdapter) mListHelper.getAdapter()).updateUserInfo(info);

    }

    public BirdRecordAdapter getAdapter(){
        return (BirdRecordAdapter)mListHelper.getAdapter();
    }


    public List<BirdRecordTotalItem> getItems() {
        return mListHelper.getItems();
    }

//    @Override
//    protected MyRecordList responseToItems(BaseResponse response) {
//        List<RecordEntity> items = ((CommonResponse<GetMyRecordResp>) response).getT().getRecords();
//        return transformToMyRecordList(items);
//    }


    /**
     * @param response 网络响应
     * @return 将网络响应转换为 列表
     */
    @Override
    protected List<BirdRecordTotalItem> responseToItems(BaseResponse response) {

        return ((CommonResponse<GetBirdRecordResponse>)response).getT().getSpecies_records();

    }

    private MyRecordList transformToMyRecordList(List<RecordEntity> entities) {

        Collections.sort(entities, Collections.reverseOrder());

        MyRecordList myRecords = new MyRecordList();

        for (RecordEntity entity : entities) {
            myRecords.addItem(entity);
        }

        return myRecords;

    }


    public void updateRecordCount(RecStatsEntity recStatsEntity) {

        String thisYearRecordNum = recStatsEntity.getThisYearRecordNum() + "";
        String thisYearSpeciesNum = "" + recStatsEntity.getThisYearSpeciesNum();

        Spannable WordtoSpan = new SpannableString("今年" + thisYearRecordNum + "次" + thisYearSpeciesNum + "种");
        WordtoSpan.setSpan(new AbsoluteSizeSpan(50), 2, (2 + thisYearRecordNum.length()),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        WordtoSpan.setSpan(new AbsoluteSizeSpan(50), (2 + thisYearRecordNum.length() + 1),
                (2 + thisYearRecordNum.length() + 1 + thisYearSpeciesNum.length()),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        thisYear.setText(WordtoSpan);
        allRecord.setText("总记录" + recStatsEntity.getRecordNum());
        allBirds.setText("总鸟种" + recStatsEntity.getSpeciesNum());

    }

    public void updateRecordCountErr() {
        resetRecordCount();
    }




    protected void resetRecordCount() {
        String thisYearRecordNum = "-";
        String thisYearSpeciesNum = "-";
        Spannable WordtoSpan = new SpannableString("今年" + thisYearRecordNum + "次" + thisYearSpeciesNum + "种");
        WordtoSpan.setSpan(new AbsoluteSizeSpan(50), 2, (2 + thisYearRecordNum.length()),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        WordtoSpan.setSpan(new AbsoluteSizeSpan(50), (2 + thisYearRecordNum.length() + 1),
                (2 + thisYearRecordNum.length() + 1 + thisYearSpeciesNum.length()),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        thisYear.setText(WordtoSpan);
        allRecord.setText("总记录-");
        allBirds.setText("总鸟种-");
    }
}
