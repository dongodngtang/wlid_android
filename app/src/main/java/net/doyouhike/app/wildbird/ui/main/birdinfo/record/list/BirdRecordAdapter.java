package net.doyouhike.app.wildbird.ui.main.birdinfo.record.list;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import net.doyouhike.app.library.ui.adapterutil.MultiItemCommonAdapter;
import net.doyouhike.app.library.ui.adapterutil.MultiItemTypeSupport;
import net.doyouhike.app.library.ui.adapterutil.ViewHolder;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordTotalItem;
import net.doyouhike.app.wildbird.biz.model.bean.ShareContent;
import net.doyouhike.app.wildbird.biz.model.bean.UserInfo;
import net.doyouhike.app.wildbird.ui.view.NestingExpandableListview;
import net.doyouhike.app.wildbird.ui.view.ShareDialog;
import net.doyouhike.app.wildbird.util.UiUtils;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-12.
 */
public class BirdRecordAdapter extends MultiItemCommonAdapter<BirdRecordTotalItem> {


    private static final String BIRDINFO_FORMAT = "%s x%s";
    private static final String SHARE_URL_FORMAT = "%s%s";
    private static final String SHARE_TITLE_FORMAT = "%s %s的观鸟记录";
    private static final String SHARE_CONTENT_FORMAT = "我在野鸟APP创建了一系列的观鸟记录,将珍贵的鸟种记录分享给大家.";


    private static final int TYPE_TITLE_HEAD = 0;
    private static final int TYPE_TITLE = 1;
    private UserInfo mUserInfo;
    private String mShareUrl;
    private String mUserNm;
    private boolean isLookMeRecord = false;

    public BirdRecordAdapter(Context context, List<BirdRecordTotalItem> datas) {
        super(context, datas, new MultiItemTypeSupport<BirdRecordTotalItem>() {
            @Override
            public int getLayoutId(int position, BirdRecordTotalItem birdRecordTotalItem) {

                if (getItemViewType(position, birdRecordTotalItem) == TYPE_TITLE_HEAD) {
                    return R.layout.item_bird_record_head;
                }
                return R.layout.item_bird_record;
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public int getItemViewType(int position, BirdRecordTotalItem birdRecordTotalItem) {
                return position == 0 ? TYPE_TITLE_HEAD : TYPE_TITLE;
            }
        });
    }

    public void updateUserInfo(UserInfo info) {

        if (null == info) {
            return;
        }

        mUserInfo = info;


        if (!TextUtils.isEmpty(mUserInfo.getUserId()) &&
                !TextUtils.isEmpty(UserInfoSpUtil.getInstance().getUserId())) {
            isLookMeRecord = mUserInfo.getUserId().equals(UserInfoSpUtil.getInstance().getUserId());
        }

        mShareUrl = info.getShare_url();

        mUserNm = info.getUserName();
        notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder holder, final BirdRecordTotalItem birdRecordTotalItem) {
        holder.setText(R.id.tv_item_bird_record_date, birdRecordTotalItem.getTime());

        UiUtils.showView(holder.getView(R.id.iv_item_myrecord_share), isLookMeRecord);

        NestingExpandableListview expandableListView = holder.getView(R.id.elv_item_bird_record_city);
        expandableListView.setGroupIndicator(null);
        //绑定二级列表适配器
        expandableListView.setAdapter(new BirdRecordCityAdapter(birdRecordTotalItem.getArea_list(),isLookMeRecord));

        holder.setOnClickListener(R.id.iv_item_myrecord_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareContent shareContent = getShareContent(birdRecordTotalItem);
                ShareDialog dialog = new ShareDialog(v.getContext());
                dialog.showShareDialog(shareContent);
            }
        });
    }


    private ShareContent getShareContent(BirdRecordTotalItem birdRecordTotalItem) {

        //获取第一条记录的图片
//        if (position+1<items.size()&&getItem(position).isTitle()){
//            position=position+1;
//        }
        String imgUrl = "";

        ShareContent shareContent = new ShareContent();

        String shareUrl = String.format(SHARE_URL_FORMAT, mShareUrl, birdRecordTotalItem.getTime());
        shareContent.setUrl(shareUrl);
        String title = String.format(SHARE_TITLE_FORMAT, mUserNm, birdRecordTotalItem.getTime());
        shareContent.setTitle(title);
        shareContent.setContent(SHARE_CONTENT_FORMAT);

        if (null != birdRecordTotalItem.getArea_list() && !birdRecordTotalItem.getArea_list().isEmpty()) {

            if (birdRecordTotalItem.getArea_list().get(0) != null) {

                if (birdRecordTotalItem.getArea_list().get(0).getRecord_list() != null &&
                        !birdRecordTotalItem.getArea_list().get(0).getRecord_list().isEmpty()) {


                    imgUrl = birdRecordTotalItem.getArea_list().get(0).getRecord_list().get(0).getSmall_img();
                }

            }


        }

        shareContent.setImgUrl(imgUrl);


        shareContent.setHaveContent(true);
        return shareContent;
    }

}
