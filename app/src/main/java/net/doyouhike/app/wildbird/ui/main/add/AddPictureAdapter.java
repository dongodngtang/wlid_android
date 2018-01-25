package net.doyouhike.app.wildbird.ui.main.add;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.Image;
import net.doyouhike.app.wildbird.biz.model.bean.RecordImage;
import net.doyouhike.app.wildbird.util.GotoActivityUtil;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by zengjiang on 16/6/2.
 */
public class AddPictureAdapter extends RecyclerView.Adapter<AddPictureAdapter.ImageViewHolder> {

    List<RecordImage> mItems;

    public AddPictureAdapter(List<RecordImage> items) {
        this.mItems = items;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_add_record_image, parent,
                false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        ImageLoaderUtil.getInstance().showImg(holder.imageView, mItems.get(position).getImageUri());


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoActivityUtil.gotoViewPictureActivity(v.getContext(),null,position,toImages());
            }
        });

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItems.remove(position);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton btnDel;

        ImageViewHolder(View view) {
            super(view);

            imageView = ButterKnife.findById(view, R.id.iv_item_add_record_image);
            btnDel = ButterKnife.findById(view, R.id.iv_item_add_record_del);


        }
    }


    public List<Image> toImages(){
        List<Image> images=new ArrayList<>();


        for (RecordImage recordImage:mItems){
            Image image=new Image(recordImage.getImageUri(),"");
            images.add(image);
        }

        return images;

    }
}
