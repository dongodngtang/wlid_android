package net.doyouhike.app.wildbird.ui.adapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import net.doyouhike.app.wildbird.ui.MultiChoosePhotoActivity;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.RecordImage;
import net.doyouhike.app.wildbird.ui.main.add.AddPictureAdapter;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;

import java.util.List;

public class AddNewPicAdapter extends RecyclerView.Adapter<AddNewPicAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<RecordImage> list;
    private int from = 0;// 0-addnew  1-choosephoto
    private Activity activity;

    public AddNewPicAdapter(Activity context, List<RecordImage> list2, int from) {
        // TODO Auto-generated constructor stub
        activity = context;
        inflater = LayoutInflater.from(context);
        this.list = list2;
        this.from = from;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.addnewpic_item, parent,
                false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String url = list.get(position).getImageUri();

        ImageLoaderUtil.getInstance().showLocalImg(holder.new_pic,url);

        if (!url.equals("")) {
            holder.del.setVisibility(View.VISIBLE);
        } else {
            holder.new_pic.setImageResource(R.drawable.add_pic);
            holder.del.setVisibility(View.GONE);
        }
        holder.del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                remove(position);
            }
        });
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView new_pic;
        ImageView del;

        public ViewHolder(View itemView) {
            super(itemView);

            new_pic = (ImageView) itemView.findViewById(R.id.new_pic);
            del = (ImageView) itemView.findViewById(R.id.del);
        }
    }



    private deletePhoto listener;

    public void setListener(deletePhoto lis) {
        listener = lis;
    }

    public interface deletePhoto {
        void delete(int imageID);
    }

    protected void remove(int position) {


        if (null != list && list.get(position).getImageID() != 0) {
            listener.delete(list.get(position).getImageID());
        }


        if (from == 1) {
            //浏览相册
            Intent intent = new Intent(MultiChoosePhotoActivity.DELETE_PHOTO);
            intent.putExtra("photo", list.get(position).getImageUri());
            activity.sendBroadcast(intent);
        }
        list.remove(position);
        if (list.size() > 0 && !list.get(list.size() - 1).getImageUri().equals("") && from == 0) {
            list.add(new RecordImage());
        }
        notifyDataSetChanged();
    }

}
