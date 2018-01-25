package net.doyouhike.app.wildbird.util;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import net.doyouhike.app.wildbird.BuildConfig;
import net.doyouhike.app.wildbird.app.MyApplication;
import net.doyouhike.app.wildbird.biz.model.bean.Version;

import java.io.File;

/**
 * 版本检查
 * Created by zengjiang on 16/6/14.
 */
public class CheckVersionUtil {
    private static final String TAG = CheckVersionUtil.class.getSimpleName();
    Context context;
    private long downId;
    private DownloadManager downloadManager;

    public CheckVersionUtil(Context context) {
        this.context = context;
    }


    public boolean isNewest() {

        Version version = LocalSpManager.getVersion();

        if (null != version) {
            double newestVersionCode = version.getVersion();

            double localVersionCode = BuildConfig.VERSION_CODE;


            return localVersionCode >= newestVersionCode;

        }

        return true;
    }
    public String getVersionCode() {


        if (isNewest()){

            return String.valueOf(BuildConfig.VERSION_CODE);
        }

        Version version = LocalSpManager.getVersion();

        if (null != version) {
            double newestVersionCode = version.getVersion();
            return String.valueOf(newestVersionCode);

        }

        return "";

    }


    public void showUpdateDialog() {


        final Version version = LocalSpManager.getVersion();

        if (null == version) {
            return;
        }


        if (isNewest()) {
            return;
        }


        if (TextUtils.isEmpty(version.getUrl())) {
            return;
        }

        //是否强制更新
        final boolean isNeedForce = version.getForce() == 1;

        new AlertDialog.Builder(context)
                .setMessage(version.getMessage())
                .setTitle("更新提醒")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downNewApk(version.getUrl());

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isNeedForce) {
                    MyApplication.getInstance().finishApplication();
                }
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (isNeedForce) {
                    MyApplication.getInstance().finishApplication();
                }
            }
        }).create().show();


    }


//使用downloadmanager下载apk的方法.

    private void downNewApk(String downUri) {

        if (downloadManager != null) {
            downloadManager.remove(downId);
        }

        this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);


//初始化下载的request
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downUri));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);
        //设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downUri));
        request.setMimeType(mimeString);
        //在通知栏中显示
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setShowRunningNotification(true);
//        request.setVisibleInDownloadsUi(true);
        //sdcard的目录下的download文件夹

//设置下载目录到sdcard download文件夹,自定义文件名,注意如果有同名文件系统会自动加_n的后缀.
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, BuildConfig.APPLICATION_ID + ".apk");
        request.setTitle("下载更新");

//插入下载队列.返回下载对象Id

        downId = downloadManager.enqueue(request);

//注册广播,下载完成后通知.

        regReceiver();

    }


//注册广播.文件下载完成后通知.

    private void regReceiver() {
        Log.d(TAG, String.format("receiver ACTION_MEDIA_MOUNTED :%1$s", "ok"));
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, String.format("regReceiver intent:%1$s", intent));
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

//下载完成的文件是不是我们要的.

                if (downId != id) {
                    return;
                }

//通过游标取得下载真实文件名称
                Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(id));
                cursor.moveToFirst();
                String localFileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));

//启动安装.

                Intent fileIntent = new Intent(Intent.ACTION_VIEW);
                File apkfile = new File(localFileName);
                Log.d(TAG, "filepath=" + apkfile.toString() + "  " + apkfile.getPath());
                fileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                fileIntent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
                context.startActivity(fileIntent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


}
