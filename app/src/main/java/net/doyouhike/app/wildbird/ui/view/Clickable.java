package net.doyouhike.app.wildbird.ui.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by zaitu on 15-11-9.
 */
public class Clickable extends ClickableSpan implements View.OnClickListener {

    private final View.OnClickListener mListener;

    public Clickable(View.OnClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.BLUE);
        ds.setFlags(Paint. UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View view) {
        mListener.onClick(view);
    }
}
