package net.doyouhike.app.wildbird.ui.adapter;

import java.util.List;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.PhotoInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class PhotoAdapter extends BaseAdapter{
	
	private GridView gridView;
	private List<PhotoInfo> list;
	private LayoutInflater inflater;
	private int width;
	private DisplayImageOptions options;

	@SuppressWarnings("deprecation")
	public PhotoAdapter(Activity context, List<PhotoInfo> list, GridView photos) {
		// TODO Auto-generated constructor stub
		gridView = photos;
		inflater = LayoutInflater.from(context);
		this.list = list;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.u114)
				.showImageOnFail(R.drawable.u114)
				.cacheInMemory(false)
				.cacheOnDisk(false)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		width = (context.getWindowManager().getDefaultDisplay().getWidth()-8)/3;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public PhotoInfo getItem(int position) {
		return list.get(position);
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
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.photo_item, parent, false);
			holder.image = (ImageView) convertView.findViewById(R.id.photo_image);
			holder.chosen = (ImageView) convertView.findViewById(R.id.photo_choose);
			LayoutParams params = (LayoutParams) holder.image.getLayoutParams();
			params.height = width;
			params.width = width;
			holder.image.setLayoutParams(params);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage(list.get(position).getPath_file(), holder.image, options);
		if(getItem(position).isChoose()){
			holder.chosen.setImageResource(R.drawable.chosen);
		}else{
			holder.chosen.setImageResource(0);
		}
		return convertView;
	}

	class ViewHolder{
		ImageView image, chosen;
	}
	
	public boolean getCheck(int position){
		return getItem(position).isChoose();
	}

	/**
	 * @param position 点击图片
	 */
	public void check(int position) {
		// TODO Auto-generated method stub

		boolean state=getCheck(position);
		getItem(position).setChoose(!state);

		int visiblePos = gridView.getFirstVisiblePosition();
		View view = gridView.getChildAt(position-visiblePos);


		if (null==view){
			return;
		}

		ViewHolder holder = (ViewHolder)view.getTag();

		if(getCheck(position)){
			holder.chosen.setImageResource(R.drawable.chosen);
		}else{
			holder.chosen.setImageResource(0);
		}


	}
}
