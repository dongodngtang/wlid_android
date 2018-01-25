package net.doyouhike.app.wildbird.ui.adapter;

import net.doyouhike.app.wildbird.R;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ConditionAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private LayoutParams lp;
	private int[] colorImage = new int[]{R.drawable.black, R.drawable.white, R.drawable.red, R.drawable.gray,
			R.drawable.orange, R.drawable.green, R.drawable.yellow, R.drawable.blue,R.drawable.brown};
	private String[] colorText = new String[]{"黑色","白色","红色","灰色","橙色","绿色","黄色","蓝色","褐色"};
	private boolean[] colorChosen, chosen;
	private int count = 0;
	
	public ConditionAdapter(Activity context) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int w = metric.widthPixels;
		refresh();
		lp = new LayoutParams((w-36)/2, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return colorImage.length;
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
		if(convertView == null){
			convertView = inflater.inflate(R.layout.color_item, parent, false);
			holder = new ViewHolder();
			holder.color_linear = (LinearLayout) convertView.findViewById(R.id.color_linear);
			holder.color_relate = (RelativeLayout) convertView.findViewById(R.id.color_relate);
			holder.color_image = (ImageView) convertView.findViewById(R.id.color_image);
			holder.color_chosen = (ImageView) convertView.findViewById(R.id.color_chosen);
			holder.color_name = (TextView) convertView.findViewById(R.id.color_name);
			holder.color_linear.setLayoutParams(lp);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.color_image.setImageResource(colorImage[position]);
		holder.color_name.setText(colorText[position]);
		if(chosen[position])holder.color_chosen.setImageResource(R.drawable.chosen);
		else holder.color_chosen.setImageResource(0);
		return convertView;
	}
	
	public void setSelect(int position){
		if(chosen[position]){
			count--;
		}else{
			if(++count > 3){
				count = 3;return;
			}
		}
		chosen[position] = !chosen[position];
		notifyDataSetChanged();
	}
	
	public void init(){
		count = 0;
		for(int i = 0; i < colorImage.length; i++){
			chosen[i] = colorChosen[i];
			if(chosen[i])count++;
		}
		notifyDataSetChanged();
	}
	
	public void makeSure(){
		count = 0;
		for(int i = 0; i < colorImage.length; i++){
			colorChosen[i] = chosen[i];
			if(chosen[i])count++;
		}
		notifyDataSetChanged();
	}
	
	public String getSelect(){
		String sel = "%";
		for(int i = 0; i < colorChosen.length; i++){
			if(colorChosen[i]){
				sel += "%"+colorText[i]+"%";
			}
		}
		return sel;
	}
	
	class ViewHolder{
		LinearLayout color_linear;
		RelativeLayout color_relate;
		ImageView color_image, color_chosen;
		TextView color_name;
	}

	public void refresh() {
		// TODO Auto-generated method stub
		count = 0;
		colorChosen = new boolean[colorImage.length];
		chosen = new boolean[colorImage.length];
		for(int i = 0; i < colorImage.length; i++){
			colorChosen[i] = false;
			chosen[i] = false;
		}
	}
}
