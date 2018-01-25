package net.doyouhike.app.wildbird.ui.view;

import java.util.ArrayList;
import java.util.List;

import net.doyouhike.app.wildbird.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class BodyLinearLayout extends LinearLayout {

	private int[] bodyImage = new int[] { R.drawable.body1, R.drawable.between, R.drawable.body2, R.drawable.between,
			R.drawable.body3 };
	private String[] shape = new String[] { "小于等于麻雀", "麻雀与斑鸠之间", "相进珠颈斑鸠", "斑鸠与鸬鹚之间", "大于等于普通鸬鹚" };
	private List<BodyView> views;
	private static boolean[] checked = new boolean[]{false,false,false,false,false}, sure = new boolean[]{false,false,false,false,false};
	private static int count = 0;
	private static int pos = -1;
	
	public BodyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		View.inflate(context, R.layout.shapeview, this);
		views = new ArrayList<BodyView>();
		BodyView body1 = (BodyView) this.findViewById(R.id.body1);
		views.add(body1);
		BodyView body11 = (BodyView) this.findViewById(R.id.body12);
		views.add(body11);
		BodyView body2 = (BodyView) this.findViewById(R.id.body2);
		views.add(body2);
		BodyView body23 = (BodyView) this.findViewById(R.id.body23);
		views.add(body23);
		BodyView body3 = (BodyView) this.findViewById(R.id.body3);
		views.add(body3);
		
		for (int i = 0; i < bodyImage.length; i++) {
			BodyView body = views.get(i);
			body.setTag(i);
			body.setImage(bodyImage[i]);
			body.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(count == 0){
						count = 1;
					}else{
						views.get(pos).setChecked();
						checked[pos] = false;
					}
					((BodyView)v).setChecked();
					pos = (Integer) v.getTag();
					checked[pos] = true;
				}
			});
		}
	}

	public void init() {
		count = 0;
		for(int i = 0; i < sure.length; i++){
			checked[i] = sure[i];
			if(sure[i]){
				pos = i;
				count = 1;
				views.get(i).setChecked();
			}
		}
	}

	public void makeSure() {
		count = 0;
		for(int i = 0; i < checked.length; i++){
			sure[i] = checked[i];
			if(sure[i]){
				count = 1;
				pos = i;
			}
		}
	}

	public void refresh() {
		for(int i = 0; i < checked.length; i ++){
			sure[i] = false;
			checked[i] = false;
		}
	}

	public String getChecked() {
		for (int i = 0; i < sure.length; i++) {
			if (sure[i]) {
				return shape[i];
			}
		}
		return "%";
	}
}
