package net.doyouhike.app.wildbird.ui.adapter;

import java.util.List;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.AlbumInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FolderAdapter extends BaseAdapter{

	private final DisplayImageOptions options;
	private List<AlbumInfo> list;
	private LayoutInflater inflater;
	
	public FolderAdapter(Activity context, List<AlbumInfo> list) {
		// TODO Auto-generated constructor stub
		inflater  = LayoutInflater.from(context);
		this.list = list;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.u114)
				.showImageOnFail(R.drawable.u114)
				.cacheInMemory(false)
				.cacheOnDisk(false)
				.bitmapConfig(Bitmap.Config.RGB_565)
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.folder_item, parent, false);
			holder.image = (ImageView) convertView.findViewById(R.id.folder_image);
			holder.folder = (TextView) convertView.findViewById(R.id.folder_album);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.folder.setText(list.get(position).getName_album()+
				"("+list.get(position).getList().size()+")");
		if(list.get(position).getList().size() > 0){
			ImageLoader.getInstance().displayImage(list.get(position).getList().get(0).getPath_file(),
					holder.image, options);
		}else{
			holder.image.setImageResource(R.drawable.u114);
		}
		return convertView;
	}
	
	class ViewHolder{
		ImageView image;
		TextView folder;
	}
}
