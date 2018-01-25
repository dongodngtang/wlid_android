package net.doyouhike.app.wildbird.ui.view;

import net.doyouhike.app.wildbird.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;

public class MyAlertDialogUtil{
	
	public static Dialog showDialog(Context context){
		Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.mine_record_click);
		return dialog;
	}
	
	public static ProgressDialog showDialog(Activity activity, String msg){
		ProgressDialog dialog = new ProgressDialog(activity);
		dialog.setMessage(msg);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		dialog.show();
		return dialog;
	}
	
	public interface DialogListener{
		void dialogClick(int pos);
	}
	
	public static void showUploadDownloadDialog(Activity activity){
		final DialogListener listener = (DialogListener) activity;
		Builder builder = new Builder(activity);
		builder.setMessage("发现新的特征数据包，立即下载？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				listener.dialogClick(0);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				listener.dialogClick(1);
			}
		});
		builder.create().show();
	}

	public static void showDialog(final DialogListener listener1,Context context, int from){
		final DialogListener listener =  listener1;
		Builder builder = new Builder(context);
		builder.setMessage("您尚未连接网络！");
		builder.setPositiveButton("立即设置网络", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				listener.dialogClick(0);
			}
		});
		String str = "保存至草稿箱";
		if(from == 1) str = "取消";
		builder.setNegativeButton(str, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				listener.dialogClick(1);
			}
		});
		builder.create().show();
	}
	
	public static void showLoginDialog(final DialogListener listener,Context context, int from){

		Builder builder = new Builder(context);
		builder.setMessage("您尚未登录！");
		builder.setPositiveButton("立即登录", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				listener.dialogClick(2);
			}
		});
		String str = "保存至草稿箱";
		if(from == 1) str = "取消";
		builder.setNegativeButton(str, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				listener.dialogClick(3);
			}
		});
		builder.create().show();
	}
}
