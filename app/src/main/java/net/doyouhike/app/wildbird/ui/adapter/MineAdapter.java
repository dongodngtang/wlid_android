package net.doyouhike.app.wildbird.ui.adapter;

import java.util.Date;
import java.util.List;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MineAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private List<RecordEntity> list;
	private int from;

	public MineAdapter(Context context, List<RecordEntity> list, int from) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.from = from;
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

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.record_list_item, parent, false);
			holder = new ViewHolder();
			holder.record_relate = (RelativeLayout) convertView.findViewById(R.id.record_relate);
			holder.record_bird = (TextView) convertView.findViewById(R.id.record_bird);
			holder.record_pic = (ImageView) convertView.findViewById(R.id.record_pic);
//			holder.map = (ImageView) convertView.findViewById(R.id.map);
			holder.record_addr = (TextView) convertView.findViewById(R.id.record_addr);
			holder.record_time = (TextView) convertView.findViewById(R.id.record_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(from == 1)holder.record_relate.setPadding(20, 5, 20, 5);
		if(list.get(position).getNumber() > 1){
			holder.record_bird.setText(list.get(position).getSpeciesName()+" x "+list.get(position).getNumber());
		}else{
			holder.record_bird.setText(list.get(position).getSpeciesName());
		}
		if(list.get(position).getLocation() == null || list.get(position).getLocation().equals("") ||
				list.get(position).getLocation().equals("NULL")){
//			holder.map.setVisibility(View.GONE);
			holder.record_addr.setText("");
		}else{
//			holder.map.setVisibility(View.VISIBLE);
			holder.record_addr.setText(list.get(position).getLocation());
		}
		if(list.get(position).getHasImage() == 0){
			holder.record_pic.setVisibility(View.GONE);
		}else{
			holder.record_pic.setVisibility(View.VISIBLE);
		}
		long dateTaken = list.get(position).getTime() * 1000;
		Date date = new Date(dateTaken);
		String datetime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
		holder.record_time.setText(datetime);
		return convertView;
	}

	class ViewHolder {
		RelativeLayout record_relate;
		TextView record_addr;
		TextView record_bird, record_time;
		ImageView record_pic;//, map;
	}

	public void remove(int position) {
		// TODO Auto-generated method stub
		list.remove(position);
		notifyDataSetChanged();
	}

	public void set(int pos, RecordEntity serializableExtra) {
		// TODO Auto-generated method stub
		list.set(pos, serializableExtra);
		notifyDataSetChanged();
	}
}
