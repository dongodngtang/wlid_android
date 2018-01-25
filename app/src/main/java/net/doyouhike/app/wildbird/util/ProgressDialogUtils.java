package net.doyouhike.app.wildbird.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.KeyEvent;

import net.doyouhike.app.wildbird.R;

/**
 * Created by zaitu on 15-11-11.
 */
public class ProgressDialogUtils {
    private Context context;
    private ProgressDialog dialog;
    private Handler handler;
    private boolean cancelable=true;
    private String strNotify;

    public ProgressDialogUtils(Context context, Handler handler) {
        this.context = context;
        this.handler=handler;
    }


    public void showProgressDialog(String strNotify){
        createDialog();
        dialog.setMessage(strNotify);
        dialog.show();
    }
    public void setProgress(int progress){
        createDialog();
        dialog.setProgress(progress);
    }
    public void dismissDialog(){
        if (null!=dialog&&dialog.isShowing())
            dialog.dismiss();
    }

    public boolean isDialogShowing(){
        if (null!=dialog){
            return dialog.isShowing();
        }
        return false;
    }
    public void onDestroy(){
        if (isDialogShowing()){
            dialog.dismiss();
            dialog=null;
        }
    }

    private void createDialog() {
        if (dialog==null){
            dialog=new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnKeyListener(onKeyListener);
        }
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    private DialogInterface.OnKeyListener onKeyListener=new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (cancelable)
                    dismissDialog();
                if (null!=handler){
                    handler.sendEmptyMessage(R.id.handle_cancel_progress_dialog);
                }
            }
            return false;
        }

    };

}
