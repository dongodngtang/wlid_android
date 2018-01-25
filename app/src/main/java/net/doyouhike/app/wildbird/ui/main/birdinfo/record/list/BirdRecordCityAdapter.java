package net.doyouhike.app.wildbird.ui.main.birdinfo.record.list;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordCityItem;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordDetailItem;
import net.doyouhike.app.wildbird.biz.model.event.DelRecordEvent;
import net.doyouhike.app.wildbird.ui.main.birdinfo.record.detail.BirdRecordDetailActivity;
import net.doyouhike.app.wildbird.ui.view.MyAlertDialogUtil;
import net.doyouhike.app.wildbird.util.GotoActivityUtil;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 功能：鸟的城市列表适配器
 *
 * @author：曾江 日期：16-4-12.
 */
public class BirdRecordCityAdapter extends BaseExpandableListAdapter {

    private static final int TYPE_TITLE_HEAD = 0;
    private static final int TYPE_TITLE = 1;

    private static final String FORMAT_COUT = "观察%d只";

    List<BirdRecordCityItem> items;
    GroupViewHolder mGroupViewHolder;
    ChildViewHolder mChildViewHolder;
    boolean isLookMeRecord;

    public BirdRecordCityAdapter(List<BirdRecordCityItem> items,boolean isLookMeRecord) {
        this.items = items;
        this.isLookMeRecord=isLookMeRecord;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    @Override
    public int getGroupType(int groupPosition) {
        return groupPosition == 0 ? TYPE_TITLE_HEAD : TYPE_TITLE;
    }

    @Override
    public int getGroupTypeCount() {
        return 2;
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroup(groupPosition).getRecord_list().size();
    }

    @Override
    public BirdRecordCityItem getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public BirdRecordDetailItem getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).getRecord_list().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //城市列表
        if (null == convertView) {

            if (getGroupType(groupPosition) == TYPE_TITLE_HEAD) {
                convertView = View.inflate(parent.getContext(), R.layout.item_bird_record_city_head, null);
            } else {
                convertView = View.inflate(parent.getContext(), R.layout.item_bird_record_city, null);
            }
            mGroupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(mGroupViewHolder);
        } else {
            mGroupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        mGroupViewHolder.setCity(getGroup(groupPosition).getCity_name());
        mGroupViewHolder.isExpanded(isExpanded);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //具体的观鸟记录

        if (null == convertView) {
            convertView = View.inflate(parent.getContext(), R.layout.item_bird_record_detail, null);
            mChildViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(mChildViewHolder);
        } else {
            mChildViewHolder = (ChildViewHolder) convertView.getTag();
        }

        mChildViewHolder.setContent(getChild(groupPosition, childPosition));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    /**
     * 城市
     */
    static class GroupViewHolder {
        ImageView ivIndicate;
        TextView city;

        public GroupViewHolder(View convertView) {
            city = (TextView) convertView.findViewById(R.id.tv_item_bird_record_city);
            ivIndicate = (ImageView) convertView.findViewById(R.id.iv_item_item_bird_record_indicate);
        }

        public void setCity(String string) {
            city.setText(string);
        }

        public void isExpanded(boolean isExpanded) {

            ivIndicate.setImageDrawable(isExpanded ?
                    ivIndicate.getContext().getResources().getDrawable(R.drawable.ic_bird_detail_content_indicate_up) :
                    ivIndicate.getContext().getResources().getDrawable(R.drawable.ic_bird_detail_content_indicate_down));
        }
    }

    /**
     * 观鸟记录
     */
    class ChildViewHolder implements View.OnClickListener,View.OnLongClickListener {
        View parent;
        TextView userNm;
        TextView count;
        ImageView ivAvatar;
        ImageView ivBirdImg;

        public ChildViewHolder(View convertView) {
            userNm = (TextView) convertView.findViewById(R.id.tv_bird_record_name);
            count = (TextView) convertView.findViewById(R.id.tv_bird_record_count);
            ivAvatar = (ImageView) convertView.findViewById(R.id.iv_bird_record_avatar);
            ivBirdImg = (ImageView) convertView.findViewById(R.id.iv_bird_record_bird_img);
            parent = convertView.findViewById(R.id.vi_bird_detail_parent);
            parent.setOnClickListener(this);
            parent.setOnLongClickListener(this);
            ivAvatar.setOnClickListener(this);
            userNm.setOnClickListener(this);
        }

        public void setContent(BirdRecordDetailItem item) {
            userNm.setText(item.getUser_name());
            count.setText(String.format(FORMAT_COUT, item.getBird_num()));
            ImageLoaderUtil.getInstance().showAvatar(ivAvatar, item.getAvatar());
            ImageLoaderUtil.getInstance().showImg(ivBirdImg, item.getSmall_img());
            parent.setTag(item);
            ivAvatar.setTag(item);
            userNm.setTag(item);
        }

        @Override
        public void onClick(View v) {
            BirdRecordDetailItem item = (BirdRecordDetailItem) v.getTag();

            if(null== item){
                return;
            }

            Intent intent = new Intent();

            switch (v.getId()){
                case R.id.vi_bird_detail_parent:
                    intent.setClass(v.getContext(), BirdRecordDetailActivity.class);
                    intent.putExtra(BirdRecordDetailActivity.I_RECORD_ITEM, item);
                    v.getContext().startActivity(intent);
                    break;

                case R.id.iv_bird_record_avatar:
                case R.id.tv_bird_record_name:
                    GotoActivityUtil.gotoOtherActivity(v.getContext(),item.getUser_id(),item.getUser_name());
                    break;
            }


        }

        @Override
        public boolean onLongClick(View v) {


            BirdRecordDetailItem item = (BirdRecordDetailItem) v.getTag();

            if(null== item){
                return false;
            }


            if (isLookMeRecord){

                showDialog(v,item);


                return true;
            }






            return false;
        }
    }



    private void showDialog(View view, final BirdRecordDetailItem item) {
        // TODO Auto-generated method stub
      final Dialog recordItemDialog = MyAlertDialogUtil.showDialog(view.getContext());
        recordItemDialog.show();
        recordItemDialog.findViewById(R.id.mine_relate).setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (v.getId() != R.id.control) {
                    recordItemDialog.dismiss();
                }
                return false;
            }
        });
        TextView edit = (TextView) recordItemDialog.findViewById(R.id.edit);
        TextView delete = (TextView) recordItemDialog.findViewById(R.id.delete);
        delete.setBackgroundResource(R.drawable.choose_sel2);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoActivityUtil.gotoEditRecord(v.getContext(),true,item.toRecordEntity());
                recordItemDialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DelRecordEvent(item));
                recordItemDialog.dismiss();
            }
        });
    }
}
