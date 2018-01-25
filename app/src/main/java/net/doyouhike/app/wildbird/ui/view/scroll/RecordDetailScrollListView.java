package net.doyouhike.app.wildbird.ui.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.view.BaseScrollListView;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-12.
 */
public class RecordDetailScrollListView extends BaseScrollListView {
    public RecordDetailScrollListView(Context context) {
        super(context);
    }

    public RecordDetailScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getPaddingHeight() {
        return 0;
    }

    @Override
    protected void onListScroll(boolean isTop) {

    }

    @Override
    protected View getHideView() {
        return View.inflate(getContext(), R.layout.layout_bird_record_detail_content,null);
    }

    @Override
    protected View getStickyView() {
        return new View(getContext());
    }

    @Override
    protected int getStickyViewHeight() {
        return 0;
    }

    @Override
    protected int getHideViewHeight() {
        return LinearLayout.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected void initAddView() {

    }

    @Override
    protected void initialize() {
        setContentViewEmpty(false);
//        resetListViewHeight();
    }

    public void resetListViewHeight(){


        int listViewHeight=getPurLayMeList().getHeight();

        int fullScreenHeight=getContentViewHeight(false);

        if (listViewHeight<fullScreenHeight){
            setContentViewHeight(listViewHeight);
        }


    }
}
