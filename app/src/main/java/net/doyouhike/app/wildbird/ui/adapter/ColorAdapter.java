package net.doyouhike.app.wildbird.ui.adapter;

import net.doyouhike.app.wildbird.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ColorAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private int[] colorImage = new int[]{R.drawable.black, R.drawable.white, R.drawable.red, R.drawable.gray,
			R.drawable.orange, R.drawable.green, R.drawable.yellow, R.drawable.blue,R.drawable.brown};
	private String[] colorText = new String[]{"黑色","白色","红色","灰色","橙色","绿色","黄色","蓝色","褐色"};
	
	private int[] bodyImage = new int[] { R.drawable.body1, R.drawable.between, R.drawable.body2, 
			R.drawable.between, R.drawable.body3 };
	private String[] shapeText = new String[] { "小于等于麻雀", "麻雀与斑鸠之间", "相进珠颈斑鸠", "斑鸠与鸬鹚之间", "大于等于普通鸬鹚" };
	private String[] name = new String[]{"麻雀", "两者之间", "斑鸠", "两者之间", "鸬鹚"};
	private int pos = -1;
	
	private int from;// 0-color, 1-shape
	private int length = 0;
	private boolean[] choose, chosen;
	private int count = 0;// color:最多选择三种颜色,shape:只能选择一种体型
	
	public ColorAdapter(Activity context, int from) {
		// TODO Auto-generated constructor stub
		this.from = from;
		inflater = LayoutInflater.from(context);
		refresh(0);
	}
	/**
	 * 初始化布尔数组
	 */
	public void refresh(int id) {
		// TODO Auto-generated method stub
		count = 0;
		length = colorImage.length;
		if(from == 1)length = bodyImage.length;
		if(id == 0)choose = new boolean[length];
		chosen = new boolean[length];
		for(int i = 0; i < length; i++){
			if(id == 0)choose[i] = false;
			chosen[i] = false;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(from == 1)return bodyImage.length;
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
			holder.shape = (TextView) convertView.findViewById(R.id.shape);
			holder.shape2 = (TextView) convertView.findViewById(R.id.shape2);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(from == 1){
			holder.color_name.setVisibility(View.GONE);
			if(name[position].contains("之间")){
				holder.shape.setVisibility(View.GONE);
				holder.shape2.setVisibility(View.VISIBLE);
				holder.shape2.setText(name[position]);
			}else{
				holder.shape.setVisibility(View.VISIBLE);
				holder.shape2.setVisibility(View.GONE);
				holder.shape.setText(name[position]);
			}
			holder.color_image.setImageResource(bodyImage[position]);
		}else{
			holder.color_image.setImageResource(colorImage[position]);
			holder.color_name.setText(colorText[position]);
		}
		
		if(chosen[position])holder.color_chosen.setImageResource(R.drawable.chosen);
		else holder.color_chosen.setImageResource(0);
		return convertView;
	}
	/**
	 * 未选择：打勾，已选择：去勾
	 * @param position
	 */
	public void setSelect(int position){
		if(chosen[position]){
			count--;
		}else{
			if(from == 0){
				if(++count > 3){
					count = 3;return;
				}
			}
			else{
				if(count < 1){
					count++;
				}else{
					chosen[pos] = !chosen[pos];
				}
				pos = position;
			}
		}
		chosen[position] = !chosen[position];
		notifyDataSetChanged();
	}
	/**
	 * 对布尔数组赋值为已选择的值
	 */
	public void init(){
		count = 0;
		for(int i = 0; i < length; i++){
			chosen[i] = choose[i];
			if(chosen[i])count++;
		}
		notifyDataSetChanged();
	}
	/**
	 * 确认选择
	 */
	public void makeSure(){
		count = 0;
		for(int i = 0; i < length; i++){
			choose[i] = chosen[i];
			if(chosen[i])count++;
		}
		notifyDataSetChanged();
	}
	/**
	 * 取得选择的数值
	 * @return String
	 */
	public String getSelect(){
		String sel = "%";
		for(int i = 0; i < length; i++){
			if(choose[i]){
				if(from == 0)sel += "%"+colorText[i]+"%";
				else sel = shapeText[i];
			}
		}
		return sel;
	}
	
	class ViewHolder{
		LinearLayout color_linear;
		RelativeLayout color_relate;
		ImageView color_image, color_chosen;
		TextView color_name, shape, shape2;
	}

	public String getSelectText(int position) {
		// TODO Auto-generated method stub
		if(from == 0)return colorText[position];
		return shapeText[position];
	}
	public boolean isCheck(int position) {
		// TODO Auto-generated method stub
		return chosen[position];
	}
	public void clear(int from, String value) {
		// TODO Auto-generated method stub
		if(from == 0){
			int index = -1;
			for(int i = 0; i < colorText.length; i++){
				if(value.equals(colorText[i])){
					index = i;
					break;
				}
			}
			if(index != -1){
				setSelect(index);
			}
		}else{
			int index = -1;
			for(int i = 0; i < shapeText.length; i++){
				if(value.equals(shapeText[i])){
					index = i;
					break;
				}
			}
			if(index != -1){
				setSelect(index);
			}
		}
	}
}
