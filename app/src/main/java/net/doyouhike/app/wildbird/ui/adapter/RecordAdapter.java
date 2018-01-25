package net.doyouhike.app.wildbird.ui.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.Species;
import net.doyouhike.app.wildbird.biz.model.bean.SpeciesInfo;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;

import java.util.List;

@SuppressWarnings("deprecation")
public class RecordAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private LayoutParams lp;
    private int from;
    private List<SpeciesInfo> birdlist;
    private List<Species> list;
    private Activity activity;

    public RecordAdapter(Activity context, List<SpeciesInfo> birdlist) {
        // TODO Auto-generated constructor stub
        activity = context;
        from = 0;
        this.birdlist = birdlist;
        inflater = LayoutInflater.from(context);
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        lp = new LayoutParams((w - 15) / 2, ((w - 15) * 3 / 8));
    }

    public RecordAdapter(Activity context, int from, List<Species> list) {
        // TODO Auto-generated constructor stub
        activity = context;
        this.from = from;
        this.list = list;
        inflater = LayoutInflater.from(context);
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        lp = new LayoutParams((w - 15) / 2, ((w - 15) * 3 / 8));
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (from == 0) return birdlist.size();
        else return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (from == 0) return birdlist.get(position);
        else return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_item, parent, false);
            holder = new ViewHolder();
            holder.records_item = (LinearLayout) convertView.findViewById(R.id.records_item);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.record_count = (TextView) convertView.findViewById(R.id.record_count);
            holder.my_bird = (TextView) convertView.findViewById(R.id.my_bird);
            holder.image.setLayoutParams(lp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setScaleType(ScaleType.FIT_XY);
        if (from == 0) {
            holder.record_count.setVisibility(View.GONE);
            holder.my_bird.setText(birdlist.get(position).getLocalName());


            String image = birdlist.get(position).getImage();


            if (!TextUtils.isEmpty(image)){

                ImageLoaderUtil.getInstance().showImg(holder.image, "assets://" + image);

            }else  if (birdlist.get(position).getImages().size() > 0){

                image = birdlist.get(position).getImages().get(0).getUrl();

                if (image.startsWith("http")){
                    ImageLoaderUtil.getInstance().showImg(holder.image, image);
                }else {
                    ImageLoaderUtil.getInstance().showImg(holder.image, "assets://" + image);
                }

            }else {
                holder.image.setImageResource(R.drawable.u114);
            }

        } else {
            holder.record_count.setText("被记录：" + list.get(position).getRecord_count() + "次");
            holder.my_bird.setText(list.get(position).getSpeciesName().replace("'", ""));
            if (!list.get(position).getImageUrl().equals("")) {
                ImageLoaderUtil.getInstance().showImg(holder.image, list.get(position).getImageUrl());
            }else {

            }
        }

        return convertView;
    }

    class ViewHolder {
        LinearLayout records_item;
        ImageView image;
        TextView my_bird, record_count;
    }
}
