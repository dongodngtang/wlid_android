package net.doyouhike.app.wildbird.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.reflect.Field;

public class UiUtils {
	
	public static final boolean isShowToast = false;
	/**
	 * 把value的对应类型值（TypedValue.COMPLEX_UNIT_PX、TypedValue.COMPLEX_UNIT_DIP、
	 * TypedValue.COMPLEX_UNIT_SP、
	 * TypedValue.COMPLEX_UNIT_PT、TypedValue.COMPLEX_UNIT_IN
	 * 、TypedValue.COMPLEX_UNIT_MM）转变为px值
	 * 
	 * @param context
	 *            上下文
	 * @param unit
	 *            TypedValue类型
	 * @param value
	 *            要转变为px的值
	 * @return
	 */
	public static float getRawSize(Context context, int unit, float value) {
		Resources res = context.getResources();
		return TypedValue.applyDimension(unit, value, res.getDisplayMetrics());
	}

	/**
	 * 把values中dimens值（如：R.dimen.img_height）转变为px值
	 * 
	 * @param context
	 *            上下文
	 * @param index
	 *            id值（如：R.dimen.img_height）
	 * @return
	 */
	public static int getIntFromDimens(Context context, int index) {
		int result = context.getResources().getDimensionPixelSize(index);
		return result;
	}

	/**
	 * 判断view是否在屏幕上显示
	 * 
	 * @param view
	 * @return
	 */
	public static boolean isViewCovered(final View view) {

		if (view == null) {
			return false;
		}

		View currentView = view;

		Rect currentViewRect = new Rect();
		boolean partVisible = currentView.getGlobalVisibleRect(currentViewRect);
		boolean totalHeightVisible = (currentViewRect.bottom - currentViewRect.top) >= view
				.getMeasuredHeight();
		boolean totalWidthVisible = (currentViewRect.right - currentViewRect.left) >= view
				.getMeasuredWidth();
		boolean totalViewVisible = partVisible && totalHeightVisible
				&& totalWidthVisible;
		if (!totalViewVisible)// if any part of the view is clipped by any of
								// its parents,return true
			return true;

		while (currentView.getParent() instanceof ViewGroup) {
			ViewGroup currentParent = (ViewGroup) currentView.getParent();
			if (currentParent.getVisibility() != View.VISIBLE)// if the parent
																// of view is
																// not
																// visible,return
																// true
				return true;

			int start = indexOfViewInParent(currentView, currentParent);
			for (int i = start + 1; i < currentParent.getChildCount(); i++) {
				Rect viewRect = new Rect();
				view.getGlobalVisibleRect(viewRect);
				View otherView = currentParent.getChildAt(i);
				Rect otherViewRect = new Rect();
				otherView.getGlobalVisibleRect(otherViewRect);
				if (Rect.intersects(viewRect, otherViewRect))// if view
																// intersects
																// its older
																// brother(covered),return
																// true
					return true;
			}
			currentView = currentParent;
		}
		return false;
	}

	private static int indexOfViewInParent(View view, ViewGroup parent) {
		int index;
		for (index = 0; index < parent.getChildCount(); index++) {
			if (parent.getChildAt(index) == view)
				break;
		}
		return index;
	}

	/**
	 * 获取屏幕有效高度
	 * 
	 * @param context
	 * @return 屏幕有效高度，为-1时表示无法获取屏幕高度
	 */
	public static int getValidHeight(Context context) {
		DisplayMetrics metrics = getMetrics(context);
		if (metrics != null) {
			return metrics.heightPixels;
		}
		return -1;
	}

	/**
	 * 获取屏幕有效宽度
	 * 
	 * @param context
	 * @return 屏幕有效宽度，为-1时表示无法获取屏幕宽度
	 */
	public static int getValidWidth(Context context) {
		DisplayMetrics metrics = getMetrics(context);
		if (metrics != null) {
			return metrics.widthPixels;
		}
		return -1;
	}

	/** 获取状态栏高度
	 * @param activity 所在的activity
	 * @return
	 */
	//获取手机状态栏高度
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
        return statusBarHeight;
    }

	private static DisplayMetrics getMetrics(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}

	public static void showView(View view,boolean isShow){
		view.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}

	public static void setText(TextView textView, String content) {
		textView.setText(content);
	}
}
