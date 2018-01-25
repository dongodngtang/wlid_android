package net.doyouhike.app.wildbird.ui.view;

import net.doyouhike.app.wildbird.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends ImageView {

	private Resources r;
	private static Drawable[] draws = new Drawable[8];
	private static int[] images = new int[] { R.drawable.feature, R.drawable.color1,
			R.drawable.color2, R.drawable.color3, R.drawable.color4,
			R.drawable.color5, R.drawable.color6, R.drawable.color7 };
	private static int[] images2 = new int[] { R.drawable.feature,
			R.drawable.color_blank, R.drawable.color_blank,
			R.drawable.color_blank, R.drawable.color_blank,
			R.drawable.color_blank, R.drawable.color_blank,
			R.drawable.color_blank };
	private static int[] images3 = new int[] { R.drawable.feature,
			R.drawable.color_blank, R.drawable.color_blank,
			R.drawable.color_blank, R.drawable.color_blank,
			R.drawable.color_blank, R.drawable.color_blank,
			R.drawable.color_blank };

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		r = context.getResources();
		this.setImageDrawable(refresh());
	}
	
	public boolean[] getShow(){
		boolean[] show = new boolean[images3.length-1];
		for(int i = 1; i < images3.length; i++){
			if(images3[i] != R.drawable.color_blank)show[i-1] = true;
			else show[i-1] = false;
		}
		return show;
	}
	
	public void init(){
		for(int i = 0; i < images2.length; i++){
			images2[i] = images3[i];
		}
	}
	
	public void makeSure(){
		for(int i = 0; i < images2.length; i++){
			images3[i] = images2[i];
		}
	}

	public LayerDrawable setPosition(int position) {
		if (images2[position] == R.drawable.color_blank) {
			images2[position] = images[position];
		} else {
			images2[position] = R.drawable.color_blank;
		}
		for (int i = 0; i < images.length; i++) {
			draws[i] = r.getDrawable(images2[i]);
		}
		LayerDrawable layer = new LayerDrawable(draws);
		return layer;
	}

	public LayerDrawable setPosition(int position, boolean isEmpty) {
		if(isEmpty){
			images2[position] = images[position];
		}else{
			images2[position] = R.drawable.color_blank;
		}
		for (int i = 0; i < images.length; i++) {
			draws[i] = r.getDrawable(images2[i]);
		}
		LayerDrawable layer = new LayerDrawable(draws);
		return layer;
	}
	
	public void refreshImage(){
		images2 = new int[] { R.drawable.feature,
				R.drawable.color_blank, R.drawable.color_blank,
				R.drawable.color_blank, R.drawable.color_blank,
				R.drawable.color_blank, R.drawable.color_blank,
				R.drawable.color_blank };
//		images3 = new int[] { R.drawable.feature,
//				R.drawable.color_blank, R.drawable.color_blank,
//				R.drawable.color_blank, R.drawable.color_blank,
//				R.drawable.color_blank, R.drawable.color_blank,
//				R.drawable.color_blank };
	}

	public LayerDrawable refresh() {
		// TODO Auto-generated method stub
		for (int i = 0; i < images.length; i++) {
			draws[i] = r.getDrawable(images2[i]);
		}
		LayerDrawable layer = new LayerDrawable(draws);
		return layer;
	}
}
