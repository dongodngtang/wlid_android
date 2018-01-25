package net.doyouhike.app.wildbird.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.app.MyApplication;
import net.doyouhike.app.wildbird.biz.dao.net.NetException;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.bean.Comment;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.biz.model.request.post.LikeCommentParam;
import net.doyouhike.app.wildbird.biz.service.database.WildbirdDbService;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.ui.login.LoginActivity;
import net.doyouhike.app.wildbird.util.CheckNetWork;
import net.doyouhike.app.wildbird.util.CircleBitmapDisplayer;
import net.doyouhike.app.wildbird.util.LocalSharePreferences;

import java.util.List;

@SuppressWarnings("deprecation")
public class CommentAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Comment> list;
    private Activity activity;
    private String[] username = new String[]{"Rhea", "Coracias", "Vulture", "Nightjar", "Hornbill"};
    private int[] image = new int[]{R.drawable.rhea, R.drawable.coracias, R.drawable.vulture,
            R.drawable.nightjar, R.drawable.hornbill};

    private DisplayImageOptions options;

    public CommentAdapter(Activity context, List<Comment> commentList) {
        // TODO Auto-generated constructor stub
        inflater = LayoutInflater.from(context);
        list = commentList;
        activity = context;
         options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.avatar)
                .showImageOnFail(R.drawable.avatar)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new CircleBitmapDisplayer())
                .build();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    ViewHolder holder = null;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.bird_comment_item, parent, false);
            holder = new ViewHolder();
            holder.user_avatar = (ImageView) convertView.findViewById(R.id.user_avatar);
            holder.commentor = (TextView) convertView.findViewById(R.id.commentor);
            holder.comment = (TextView) convertView.findViewById(R.id.comment);
            holder.favour = (TextView) convertView.findViewById(R.id.favour);
            holder.user_avatar.setScaleType(ScaleType.FIT_XY);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.comment.setText(list.get(position).getContent());
        holder.user_avatar.setImageResource(image[position % 5]);
        if (list.get(position).getAvatar() != null && !list.get(position).getAvatar().equals("")) {

            ImageLoader.getInstance().displayImage(list.get(position).getAvatar(), holder.user_avatar, options);
        } else {
            holder.user_avatar.setImageResource(image[position % 5]);
        }

        if (!CheckNetWork.isNetworkAvailable(activity)) {
            holder.commentor.setText("");
        } else {
            if (list.get(position).getUserName() != null && !list.get(position).getUserName().equals("")) {
                holder.commentor.setText(list.get(position).getUserName());
            } else {
                holder.commentor.setText(username[position % 5]);
                holder.user_avatar.setImageResource(image[position % 5]);
            }
        }
        if (list.get(position).getIsLike() == 0) {
            holder.favour.setSelected(false);
            holder.favour.setEnabled(true);
            holder.favour.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (!LocalSharePreferences.getValue(activity, "userId").equals("")) {
                        if (CheckNetWork.isNetworkAvailable(activity)) {
                            list.get(position).setIsLike(1);
                            list.get(position).setLikeNum(list.get(position).getLikeNum() + 1);
                            notifyDataSetChanged();
                            doLike(position);
                        } else {
                            Toast.makeText(activity, "请设置好网络再评论。", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "请先登录再继续评论。", Toast.LENGTH_SHORT).show();
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                    }
                }
            });
        } else {
            holder.favour.setSelected(true);
            holder.favour.setEnabled(false);
        }
        holder.favour.setText("" + list.get(position).getLikeNum());
        return convertView;
    }

    protected void doLike(final int position) {


        LikeCommentParam param = new LikeCommentParam();
        param.setCommentID(list.get(position).getCommentID());

        param.setTag(position);

        ApiReq.doPost(param, Content.REQ_likeComment, doLikeSuc, doLikeErr);
    }

    class ViewHolder {
        ImageView user_avatar;
        TextView commentor, comment, favour;
    }

    //      获取野鸟评论成功回调
    private Response.Listener<BaseResponse> doLikeSuc =
            new Response.Listener<BaseResponse>() {

                @Override
                public void onResponse(BaseResponse response) {

                    Toast.makeText(activity, "点赞成功", Toast.LENGTH_SHORT).show();

                    int position = (Integer) response.getTag();

                    if (list.size() > position && position > 0) {

                        WildbirdDbService.getInstance().updateCommentLikeNum(list.get(position).getCommentID(), list.get(position).getLikeNum());

                    }

                }
            };


    //获取野鸟评论失败回调
    private Response.ErrorListener doLikeErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {


            String errMsg = error.getMessage();

            if (null!=errMsg&&
                    !errMsg.contains(MyApplication.getInstance().getString(R.string.tip_net_error))){
                Toast.makeText(activity, errMsg, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(activity, "很遗憾！没有点赞成功", Toast.LENGTH_SHORT).show();
            }
            int position = -1;

            if (error instanceof NetException) {
                position = (Integer) ((NetException) error).getTag();
            }


            if (list.size() > position && position >= 0) {

                list.get(position).setIsLike(0);
                list.get(position).setLikeNum(list.get(position).getLikeNum() - 1);
                notifyDataSetChanged();
                WildbirdDbService.getInstance().updateCommentLikeNum(list.get(position).getCommentID(), list.get(position).getLikeNum());

            }
        }
    };


}
