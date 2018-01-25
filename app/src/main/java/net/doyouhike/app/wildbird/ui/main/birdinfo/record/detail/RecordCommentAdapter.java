package net.doyouhike.app.wildbird.ui.main.birdinfo.record.detail;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.doyouhike.app.library.ui.adapterutil.CommonAdapter;
import net.doyouhike.app.library.ui.adapterutil.ViewHolder;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordDetailCommentItem;
import net.doyouhike.app.wildbird.util.GotoActivityUtil;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;

import java.util.List;

/**
 * 功能：观鸟记录适配器
 *
 * @author：曾江 日期：16-4-12.
 */
public class RecordCommentAdapter extends CommonAdapter<BirdRecordDetailCommentItem> implements View.OnClickListener{

    public RecordCommentAdapter(Context context, List<BirdRecordDetailCommentItem> datas) {
        super(context, datas, R.layout.item_bird_record_comment);
    }

    @Override
    public void convert(ViewHolder holder, BirdRecordDetailCommentItem birdRecordDetailCommentItem) {
        holder.setText(R.id.tv_bird_record_comment_user_name, birdRecordDetailCommentItem.getUser_name());
        //内容
        holder.setText(R.id.tv_bird_record_comment_content, birdRecordDetailCommentItem.getContent());

        //设置头像
        ImageView ivAvatar = holder.getView(R.id.iv_bird_record_comment_user_avatar);
        ivAvatar.setOnClickListener(this);
        ivAvatar.setTag(birdRecordDetailCommentItem);
        ImageLoaderUtil.getInstance().showAvatar(ivAvatar, birdRecordDetailCommentItem.getAvatar());
        //设置人名
        TextView tvUserName=  holder.getView(R.id.tv_bird_record_comment_user_name);
        tvUserName.setOnClickListener(this);
        tvUserName.setTag(birdRecordDetailCommentItem);

    }

    /**
     * 头像 人名 点击调整他人主页
     * @param v
     */
    @Override
    public void onClick(View v) {

        BirdRecordDetailCommentItem item=(BirdRecordDetailCommentItem)v.getTag();

        if (null==item){
            return;
        }
        //查看他人主页
        GotoActivityUtil.gotoOtherActivity(v.getContext(),item.getUser_id(),item.getUser_name());

    }
}
