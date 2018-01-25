package net.doyouhike.app.wildbird.ui.view.popupwin.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.PopupWindow;

/**
 * 功能：
 *
 * @author：曾江 日期：16-3-7.
 */
public abstract class BasePopupWin {
    protected Context context;
    protected PopupWindow popupWindow;
    PopupWinListener listener;

    public BasePopupWin(Context context) {
        this.context=context;
        init(context);
    }

    public void init(Context context) {

        // 触摸弹出框的灰色部分，关闭自己

        popupWindow = new PopupWindow(context);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
        popupWindow.setContentView(getContentView(context));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (listener != null) {
                    listener.onDismiss();
                }
            }
        });
        popupWindow.setFocusable(true);
        initialize();

    }

    public void show(View view){
        popupWindow.showAsDropDown(view);
    }

    protected abstract View getContentView(Context context);
    protected abstract void initialize();
}
