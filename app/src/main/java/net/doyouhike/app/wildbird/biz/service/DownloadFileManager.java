package net.doyouhike.app.wildbird.biz.service;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import net.doyouhike.app.wildbird.util.LogUtil;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-26.
 */
public class DownloadFileManager {

    DownloadManager downloadManager;
    DownloadManager.Request request;
    private long myDownloadReference = -1;

    public DownloadFileManager(Context context) {
        this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(receiver, filter);
    }

    public void startDownload(String strUrl, String title, String fileName) {

        Uri uri = Uri.parse(strUrl);
        request = new DownloadManager.Request(uri);
        request.setTitle(title)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        myDownloadReference = downloadManager.enqueue(request);

    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (myDownloadReference == reference) {

                String mimeType=downloadManager.getMimeTypeForDownloadedFile(myDownloadReference);


                if (mimeType.equals("application/pdf")){

                    new AlertDialog.Builder(context).
                            setTitle("下载成功,是否现在查看").
                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = getPdfFileIntent(downloadManager.getUriForDownloadedFile(myDownloadReference));
                                    context.startActivity(intent);
                                }
                            }).
                            setNegativeButton("取消", null).
                            create().
                            show();
                }


            }
        }
    };


    public static Intent getPdfFileIntent(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

}
