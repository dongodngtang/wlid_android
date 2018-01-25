package net.doyouhike.app.wildbird.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;

public class AutoEditText extends AutoCompleteTextView{
	
	public AutoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public AutoEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public AutoEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private int xUp = 0;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_UP){
			xUp = (int) event.getX();
			if ((getWidth() - xUp) <= getCompoundPaddingRight()) {
				if (!TextUtils.isEmpty(getText().toString())) {
					setText("");
				}
			}
		}
		return super.onTouchEvent(event);
	}

}
