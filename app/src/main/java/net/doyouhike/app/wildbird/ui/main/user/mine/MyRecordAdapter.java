package net.doyouhike.app.wildbird.ui.main.user.mine;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordDetailItem;
import net.doyouhike.app.wildbird.biz.model.bean.MyRecord;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.biz.model.bean.RecordImage;
import net.doyouhike.app.wildbird.biz.model.bean.ShareContent;
import net.doyouhike.app.wildbird.biz.model.bean.UserInfo;
import net.doyouhike.app.wildbird.ui.main.birdinfo.record.detail.BirdRecordDetailActivity;
import net.doyouhike.app.wildbird.util.GotoActivityUtil;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;
import net.doyouhike.app.wildbird.util.UiUtils;
import net.doyouhike.app.wildbird.ui.view.ShareDialog;

import java.util.List;


/**
 * 功能：
 *
 * @author：曾江 日期：16-3-11.
 */
public class MyRecordAdapter extends BaseAdapter {

    private static final String BIRDINFO_FORMAT = "%s x%s";
    private static final String SHARE_URL_FORMAT = "%s%s";
    private static final String SHARE_TITLE_FORMAT = "%s %s的观鸟记录";
    private static final String SHARE_CONTENT_FORMAT = "我在野鸟APP创建了一系列的观鸟记录,将珍贵的鸟种记录分享给大家.";
    private static final int TYPE_TITLE_HEAD = 0;
    private static final int TYPE_TITLE = 1;
    private static final int TYPE_CONTENT = 2;

    private Context context;
    private MyRecordList items;
    private ViewHolder viewHolder;
    private String mShareUrl;
    private String mUserNm;
    private UserInfo mUserInfo;
    private boolean showShareBotton=false;

    public MyRecordAdapter(Context context, MyRecordList items) {
        this.context = context;
        this.items = items;
        updateUserInfo();
    }

    public void set(RecordEntity entity) {
        items.setItem(entity);
        notifyDataSetChanged();
    }

    public void updateUserInfo(UserInfo info) {

        if (null == info) {
            return;
        }

        mUserInfo = info;


        if (!TextUtils.isEmpty(mUserInfo.getUserId())&&!TextUtils.isEmpty(UserInfoSpUtil.getInstance().getUserId())){
            showShareBotton=mUserInfo.getUserId().equals(UserInfoSpUtil.getInstance().getUserId());
        }

        mShareUrl = info.getShare_url();

        mUserNm = info.getUserName();
        notifyDataSetChanged();
    }

    public void updateUserInfo() {
        mShareUrl = UserInfoSpUtil.getInstance().getShareUrl();
        mUserNm = UserInfoSpUtil.getInstance().getUserNm();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_TITLE_HEAD;
        }

        return items.get(position).isTitle() ? TYPE_TITLE : TYPE_CONTENT;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return !getItem(position).isTitle();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public MyRecord getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {

            if (getItemViewType(position) == TYPE_TITLE_HEAD) {

                convertView = LayoutInflater.from(context).inflate(R.layout.item_my_record_title_head, parent, false);
                viewHolder = new TitleViewHolder(convertView, position);

            } else if (getItemViewType(position) == TYPE_TITLE) {

                convertView = LayoutInflater.from(context).inflate(R.layout.item_my_record_title, parent, false);
                viewHolder = new TitleViewHolder(convertView, position);

            } else{
//            } else if (getItemViewType(position) == TYPE_CONTENT) {

                convertView = LayoutInflater.from(context).inflate(R.layout.item_my_record_content, parent, false);
                viewHolder = new ContentViewHolder(convertView);
            }

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setContent(getItem(position));

        return convertView;
    }


    class ContentViewHolder extends ViewHolder implements View.OnClickListener {
        TextView tvBirdInfo;
        TextView tvLocation;
        TextView tvTime;
        ImageView ivSmall;
        View parent;

        public ContentViewHolder(View view) {
            this.tvBirdInfo = (TextView) view.findViewById(R.id.tv_item_myrecord_bird_info);
            this.tvLocation = (TextView) view.findViewById(R.id.tv_item_myrecord_location);
            this.tvTime = (TextView) view.findViewById(R.id.tv_item_myrecord_time);
            this.parent = view.findViewById(R.id.ll_item_myrecord_content);
            this.ivSmall=(ImageView) view.findViewById(R.id.iv_item_myrecord_small_picture);
            parent.setOnClickListener(this);
        }

        @Override
        public void setContent(MyRecord record) {
            tvTime.setText(record.getDateTimeFormatItemContent());
            tvLocation.setText(record.getRecord().getCityName());
            UiUtils.showView(tvLocation, !TextUtils.isEmpty(record.getRecord().getCityName()));
            tvBirdInfo.setText(String.format(BIRDINFO_FORMAT, record.getRecord().getSpeciesName(), record.getRecord().getNumber()));
            parent.setTag(record);
            ImageLoaderUtil.getInstance().showImg(ivSmall,record.getRecord().getSmall_img());
        }

        @Override
        public void onClick(View v) {

            MyRecord record = (MyRecord) v.getTag();
            if (null == record) {
                return;
            }


            BirdRecordDetailItem item = new BirdRecordDetailItem(record);

            if (mUserInfo != null) {
                item.setUser_id(mUserInfo.getUserId());
                item.setUser_name(mUserInfo.getUserName());
                item.setAvatar(mUserInfo.getAvatar());
            }

            Intent intent = new Intent(v.getContext(), BirdRecordDetailActivity.class);
            intent.putExtra(BirdRecordDetailActivity.I_RECORD_ITEM, item);
            v.getContext().startActivity(intent);
        }
    }

    class TitleViewHolder extends ViewHolder implements View.OnClickListener {
        TextView tvDateTime;
        ImageView ivShare;

        public TitleViewHolder(View view, int position) {
            this.tvDateTime = (TextView) view.findViewById(R.id.tv_item_myrecord_date);
            this.ivShare = (ImageView) view.findViewById(R.id.iv_item_myrecord_share);
            this.ivShare.setOnClickListener(this);
            ivShare.setTag(position);
            UiUtils.showView(ivShare,showShareBotton);
        }

        @Override
        public void setContent(MyRecord record) {
            tvDateTime.setText(record.getDateTime());
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();

            ShareContent shareContent = getShareContent(position);
            ShareDialog dialog = new ShareDialog(v.getContext());
            dialog.showShareDialog(shareContent);
        }
    }

    abstract class ViewHolder {

        abstract void setContent(MyRecord record);
    }


    private ShareContent getShareContent(int position) {

        //获取第一条记录的图片
//        if (position+1<items.size()&&getItem(position).isTitle()){
//            position=position+1;
//        }


        MyRecord myRecord = getItem(position);
        ShareContent shareContent = new ShareContent();

        String shareUrl = String.format(SHARE_URL_FORMAT, mShareUrl, myRecord.getDateTime());
        shareContent.setUrl(shareUrl);
        String title = String.format(SHARE_TITLE_FORMAT, mUserNm, myRecord.getDateTimeFormatShareTitle());
        shareContent.setTitle(title);
        shareContent.setContent(SHARE_CONTENT_FORMAT);

        List<RecordImage> images = myRecord.getRecord().getList();
        if (!images.isEmpty()) {
            shareContent.setImgUrl(myRecord.getRecord().getSmall_img());
        }

        shareContent.setHaveContent(true);
        return shareContent;
    }

}
