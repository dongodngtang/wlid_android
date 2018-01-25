package net.doyouhike.app.library.ui.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;

import net.doyouhike.app.library.ui.R;

/**
 * Created by terry on 4/5/16.
 */
public class AsyncAlertDialog {

    private AlertDialog dialog = null;

    private Context context;


    public AsyncAlertDialog(Context context) {
        this.context = context;
    }

    public void show() {
        try {
            if (null == dialog) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View v = inflater.inflate(R.layout.dialog_loading_image, null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    dialog = new AlertDialog.Builder(context,
                            R.style.LoadingDialog).create();
                }else {
                    dialog = new AlertDialog.Builder(context).create();
                }

                dialog.setCanceledOnTouchOutside(false);//只能按返回键取消对话框
                dialog.show();
                dialog.getWindow().setContentView(v);
                dialog.getWindow().findViewById(R.id.rel_bigimage_layout)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                dialog.dismiss();
                            }
                        });
            } else if (!dialog.isShowing())
                dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        if (null != dialog && dialog.isShowing())
            dialog.dismiss();
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }


}
