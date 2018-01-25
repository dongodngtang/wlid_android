package net.doyouhike.app.library.ui.proccess;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.doyouhike.app.library.ui.R;

/**
 * Filework:
 * Author: luochangdong
 * Date:16-3-29
 */
public class LoadingDialog extends Dialog {

    private TextView tvLoading;

    public LoadingDialog(Context context) {
        super(context,R.style.loadingDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        tvLoading = (TextView)findViewById(R.id.tv);
        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);
    }

    public void setTvLoading(String text){
        tvLoading.setText(text);
    }


}
