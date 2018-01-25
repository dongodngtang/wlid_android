package net.doyouhike.app.wildbird.ui.view.popupwin;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.util.UiUtils;
import net.doyouhike.app.wildbird.ui.view.popupwin.base.BasePopupWin;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 功能：
 *
 * @author：曾江 日期：16-3-7.
 */
public class BirdInfoMorePopWin extends BasePopupWin {

    public interface onItemClickListener {
        void onAddRecord();

        void onShare();
    }

    @OnClick(R.id.item_bird_info_add_record)
    public void onAddRecord() {
        listener.onAddRecord();
        popupWindow.dismiss();
    }
    @OnClick(R.id.item_bird_info_share)
    public void onShare() {
        listener.onShare();
        popupWindow.dismiss();
    }


    onItemClickListener listener;

    public BirdInfoMorePopWin(Context context) {
        super(context);
    }

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void show(View view) {

        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.RIGHT, UiUtils.getIntFromDimens(context, R.dimen.size_bird_info_popup_padding_right), getDeltaY(view));
    }

    @Override
    protected View getContentView(Context context) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_bird_info_popup_menu, null);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    protected void initialize() {
        popupWindow.setWidth(UiUtils.getIntFromDimens(context, R.dimen.size_bird_info_popup_width));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

    }


    private int getDeltaY(View view) {

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        return view.getHeight() + location[1];
    }
}
