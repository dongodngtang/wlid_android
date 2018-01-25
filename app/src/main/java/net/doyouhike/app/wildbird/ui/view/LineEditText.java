package net.doyouhike.app.wildbird.ui.view;

import net.doyouhike.app.wildbird.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class LineEditText extends EditText{
	
	private Drawable search_btn;
	private Drawable del_btn;
	private Context mContext;
	/**
	 * 手指抬起时的X坐标
	 */
	private int xUp = 0;
	private boolean isPW = false;
	
	public void setIsPW(boolean isPW){
		this.isPW = isPW;
	}
	public void showPassword(){
		if(this.isSelected()){
			setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}else{
			setInputType(InputType.TYPE_CLASS_TEXT);
		}
		this.setSelected(!this.isSelected());
	}
	public boolean IsShowPassword(){
		return this.isSelected();
	}
	
	private SearchListener listener = null;
	
	public void setListener(SearchListener listener) {
		this.listener = listener;
	}

	public interface SearchListener{
		public void search();
		public void showPW();
		public void clear();
		public void autoEdit();
	}

	@SuppressWarnings("deprecation")
	public LineEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lineEdittext);
		int left_src = typedArray.getResourceId(R.styleable.lineEdittext_drawableLeft, 0);
		if(left_src != 0)search_btn = mContext.getResources().getDrawable(left_src);
		int right_src = typedArray.getResourceId(R.styleable.lineEdittext_drawableRight, 0);
		if(right_src != 0)del_btn = mContext.getResources().getDrawable(right_src);
		typedArray.recycle();
		setCompoundDrawablesWithIntrinsicBounds(search_btn, null, null, null);
	}

	// 处理删除事件
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (del_btn != null && event.getAction() == MotionEvent.ACTION_UP) {
			// 获取点击时手指抬起的X坐标
			xUp = (int) event.getX();
			// 当点击的坐标到当前输入框右侧的距离小于等于getCompoundPaddingRight()的距离时，则认为是点击了删除图标
			if ((getWidth() - xUp) <= getCompoundPaddingRight()) {
				if(isPW){
					listener.showPW();
				}else{
					if (!TextUtils.isEmpty(getText().toString())) {
						setText("");
						if(listener != null){
							listener.clear();
							setCompoundDrawablesWithIntrinsicBounds(search_btn, null, null, null);
						}
					}
				}
			}else if((xUp) <= getCompoundPaddingLeft()){
				// 点击左侧图标
				listener.autoEdit();
			}
		} else if (del_btn != null && event.getAction() == MotionEvent.ACTION_DOWN && getText().length() != 0) {
			setCompoundDrawablesWithIntrinsicBounds(search_btn, null, del_btn, null);
		} else if (getText().length() != 0) {
			setCompoundDrawablesWithIntrinsicBounds(search_btn, null, del_btn, null);
		}
		return super.onTouchEvent(event);
	}
	public void setClear() {
		// TODO Auto-generated method stub
		setCompoundDrawablesWithIntrinsicBounds(search_btn, null, del_btn, null);
	}
	public void setGone() {
		// TODO Auto-generated method stub
		setCompoundDrawablesWithIntrinsicBounds(search_btn, null, null, null);
	}
}
