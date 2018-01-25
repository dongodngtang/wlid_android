package net.doyouhike.app.wildbird.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import net.doyouhike.app.wildbird.R;

import java.util.ArrayList;
import java.util.List;

public class CitySelectAdapter extends BaseAdapter {
	private List<String> wheelStrList;
	private List<View> itemViewList = new ArrayList<View>();
	
	@SuppressLint("InflateParams")
	public CitySelectAdapter(Context context, List<String> wheelStrList) {
		this.wheelStrList = wheelStrList;
		for (int i=0; i<wheelStrList.size(); i++){
			View itemView = LayoutInflater.from(context).inflate(R.layout.item_city_select_dialog, null);
			itemViewList.add(itemView);
		}
	}
	
	@Override
	public int getCount() {
		return wheelStrList.size();
	}

	@Override
	public Object getItem(int position) {
		return wheelStrList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position < itemViewList.size()) {
			convertView = itemViewList.get(position);
			
			TextView wheelTextTv = (TextView) convertView.findViewById(R.id.tv_wheel_text);
			wheelTextTv.setText(wheelStrList.get(position));
			
			return convertView;
		}
		return null;
	}
	
	public View getItemView(int position){
		if (position < wheelStrList.size()) {
			return (View)itemViewList.get(position);
		}
		return null;
	}
	
	public void setDataList(List<String> wheelStrList) {
		this.wheelStrList = wheelStrList;
	}
}