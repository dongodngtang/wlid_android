package net.doyouhike.app.wildbird.ui.adapter;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.view.MyListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyArrayAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private MyListView listview;
	private String[] list;
	private int from;// 1-feature
	private boolean isCheck = true;
	
	public MyArrayAdapter(Context context, MyListView listview, String[] list, int from) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		this.listview = listview;
		this.list = list;
		this.from = from;
	}
	
	public boolean isCheck(){
		return isCheck;
	}
	
	public void setCheck(int pos, boolean isCheck){
		View v = listview.getChildAt(pos);
		ViewHolder holder = (ViewHolder) v.getTag();
		if(isCheck){
			holder.text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_left, 0);
			holder.text.setSelected(true);
		}else{
			holder.text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			holder.text.setSelected(false);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			if(from == 0)convertView = inflater.inflate(R.layout.conditions_item, parent, false);
			else convertView = inflater.inflate(R.layout.featureconds_item, parent, false);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.cond);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text.setText(list[position]);
		if(position == 0){
			holder.text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_left, 0);
			holder.text.setSelected(true);
		}else{
			holder.text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			holder.text.setSelected(false);
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView text;
	}
}
