package net.doyouhike.app.wildbird.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-12.
 */
public class NestingExpandableListview extends ExpandableListView {
    public NestingExpandableListview(Context context) {
        super(context);
    }

    public NestingExpandableListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
