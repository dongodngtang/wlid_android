package net.doyouhike.app.wildbird.ui.view;

import net.doyouhike.app.wildbird.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BodyView extends RelativeLayout{
	
	private ImageView body_image, body_chosen;
	private boolean checked = false;
	
	public BodyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		View.inflate(context, R.layout.body_view, this);
		body_image = (ImageView) this.findViewById(R.id.body_image);
		body_chosen = (ImageView) this.findViewById(R.id.body_chosen);
	}
	
	public void setImage(int id){
		if(id != R.drawable.body1){
			 LayoutParams params = (LayoutParams) body_image.getLayoutParams();
			 params.height = LayoutParams.MATCH_PARENT;
			 body_image.setLayoutParams(params);
		}
		body_image.setImageResource(id);
	}

	public void setChecked(){
		checked = !checked;
		if(checked)body_chosen.setImageResource(R.drawable.chosen);
		else body_chosen.setImageResource(0);
	}
	
	public void refresh(){
		checked = false;
	}
}
