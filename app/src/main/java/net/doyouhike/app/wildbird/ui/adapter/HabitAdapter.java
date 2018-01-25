package net.doyouhike.app.wildbird.ui.adapter;

import net.doyouhike.app.wildbird.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HabitAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private String[] list;
	private boolean[] checked, chosen;
	private int from;
	private int count = 0;

	public HabitAdapter(Context context, String[] list, int from) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		this.from = from;
		refresh(list);
	}
	
	public void refresh(String[] list){
		this.list = list;
		refresh(0);
		notifyDataSetChanged();
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.habit_item, parent, false);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.habit_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text.setText(list[position]);
		holder.text.setSelected(checked[position]);
		if(from == 1){
			//搜索栏的生僻字 附近记录
			holder.text.setTextSize(14);
		}
		if (from == 4) {
			holder.text.setBackgroundResource(R.drawable.txt_bg_sel2);
		}
		if(from == 5){
			//身体特征
			holder.text.setBackgroundResource(R.drawable.txt_bg_sel3);
			holder.text.setMinLines(2);
		}
		return convertView;
	}
	
	public void setSelect(int position, boolean check){
		checked[position] = check;
		notifyDataSetChanged();
	}

	public void setSelect(int position) {
		if (checked[position]) {
			count--;
		} else {
			if (from == 1) {
				if(++count > 1){
					count = 1;
					return;
				}
			}else{
				count++;
			}
		}
		checked[position] = !checked[position];
		notifyDataSetChanged();
	}
	
	public boolean isEmpty(){
		if(count > 0)return false;
		return true;
	}
	
	public void init(){
		count = 0;
		for (int i = 0; i < list.length; i++) {
			checked[i] = chosen[i];
			if(chosen[i])count++;
		}
		notifyDataSetChanged();
	}
	
	public void makeSure(){
		count = 0;
		for (int i = 0; i < list.length; i++) {
			chosen[i] = checked[i];
			if(chosen[i])count++;
		}
		notifyDataSetChanged();
	}

	/**
	 * @return 选中的字符
	 */
	public String getSelect() {
		String sel = "%";
		for(int i = 0; i < chosen.length; i++){
			if(chosen[i]){
				sel += "%"+list[i].replaceAll("\n", "")+"%";
			}
		}
		return sel;
	}

	class ViewHolder {
		TextView text;
	}

	public void refresh(int id) {
		// TODO Auto-generated method stub
		checked = new boolean[list.length];
		if(id == 0)chosen = new boolean[list.length];
		for (int i = 0; i < list.length; i++) {
			checked[i] = false;
			if(id == 0)chosen[i] = false;
		}
	}

	public String getSelectText(int position) {
		// TODO Auto-generated method stub
		return list[position];
	}

	public boolean isCheck(int position) {
		// TODO Auto-generated method stub
		return checked[position];
	}

	public void clear(String value) {
		// TODO Auto-generated method stub
		int index = -1;
		for(int i = 0; i < list.length; i++){
			String content=list[i].replace("\n", "");
			if(value.equals(content)){
				index = i;
				break;
			}
		}
		if(index != -1){
			Log.i("info", "clear");
			setSelect(index);
		}
	}
}
