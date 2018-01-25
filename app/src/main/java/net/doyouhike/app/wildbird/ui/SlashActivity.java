package net.doyouhike.app.wildbird.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import net.doyouhike.app.wildbird.BuildConfig;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.dao.net.ErrState;
import net.doyouhike.app.wildbird.biz.dao.net.NetException;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.bean.Banner;
import net.doyouhike.app.wildbird.biz.model.request.get.CheckDataUpdateGetParam;
import net.doyouhike.app.wildbird.biz.model.response.CheckDataUpdSucRepo;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.ui.main.MainActivity;
import net.doyouhike.app.wildbird.ui.view.MyAlertDialogUtil;
import net.doyouhike.app.wildbird.ui.view.MyAlertDialogUtil.DialogListener;
import net.doyouhike.app.wildbird.util.CheckNetWork;
import net.doyouhike.app.wildbird.util.CopyDatabaseUtil;
import net.doyouhike.app.wildbird.util.FileUtil;
import net.doyouhike.app.wildbird.util.GotoActivityUtil;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;
import net.doyouhike.app.wildbird.util.LocalSharePreferences;
import net.doyouhike.app.wildbird.util.LocalSpManager;
import net.doyouhike.app.wildbird.util.PermissionUtil;
import net.doyouhike.app.wildbird.util.PhotoUtil;
import net.doyouhike.app.wildbird.util.UiUtils;
import net.doyouhike.app.wildbird.util.UmengEventUtil;
import net.doyouhike.app.wildbird.util.XMLParseUtil;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

import butterknife.InjectView;
import butterknife.OnClick;

@SuppressWarnings("deprecation")
@SuppressLint("HandlerLeak")
public class SlashActivity extends BaseAppActivity implements DialogListener {

    /**
     * 广告图片
     */
    @InjectView(R.id.iv_activity_slash_branner)
    ImageView ivActivitySlashBranner;
    /**
     * 广告图片
     */
    @InjectView(R.id.btn_activity_slash_slip)
    View btnSlip;

    /**
     * 跳过广告
     */
    @OnClick(R.id.btn_activity_slash_slip)
    public void onSlipAdvertisement() {
        handler.sendEmptyMessage(1);//进入主页
    }



    private ProgressDialog dialog = null;
    private boolean start = false;

    private String dataVersion;

    private boolean go = true;
    private long firstTime;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (!start) {
                        start = true;
                        startActivity(new Intent(SlashActivity.this, MainActivity.class));
                    }
                    finish();
                    break;
                case 2:
                    handler.sendEmptyMessage(1);
                    break;
            }
        }

        ;
    };

    @Override
    public void dialogClick(int pos) {
        // TODO Auto-generated method stub
        switch (pos) {
            case 0:// 下载
                dialog = MyAlertDialogUtil.showDialog(this, "正在下载野鸟离线包，请稍候");
                String uri = LocalSharePreferences.getValue(getApplicationContext(), "downUrl");
                LocalSharePreferences.commit(getApplicationContext(), "needUpdate", "");
                new ImageAsyncTask(uri, 0).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case 1://
                handler.sendEmptyMessage(1);//进入主页
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_slash;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();

        MobclickAgent.enableEncrypt(true);
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.onEvent(SlashActivity.this, "startApp");
        MobclickAgent.setCatchUncaughtExceptions(false);

//		生成本地wildbird.db文件 2 写入基础数据 3 写入特征数据
//        new ImageAsyncTask("", 2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        //展示广告
        showBanner();


        boolean haveDbSymbel = LocalSharePreferences.getValue(getApplicationContext(), Content.SP_COPY_DB_SYMBOL, false);

        if (!haveDbSymbel) {
            //隐藏跳过按钮
            UiUtils.showView(btnSlip,false);
            //没有检测到复制数据库的标志，执行复制打包的数据库到本地
            new ImageAsyncTask("", 1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return;
        }

        if (PermissionUtil.getInstance().checkStoragePermission(this)) {
            initData();
        } else {
//            gotoMain();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PermissionUtil.getInstance().onRequestStorageionsResult(requestCode, permissions, grantResults)) {

            initData();
        } else {
            gotoMain();
        }

    }


    private void initData() {


        PhotoUtil.getInstance(getApplicationContext());


        if (CheckNetWork.isNetworkAvailable(getApplicationContext())) {
            checkDataUpd();
        } else {
            gotoMain();
        }
    }


    private void gotoMain() {
        handler.sendEmptyMessageDelayed(2, 5000);
    }

    private void checkDataUpd() {

        String ver = LocalSharePreferences.getValue(this, Content.SP_DATABASE_VERSION);

        if (TextUtils.isEmpty(ver)) {
            ver = Content.DATABASE_VERSION_CODE;
        }

        CheckDataUpdateGetParam param = new CheckDataUpdateGetParam();
        param.setDataVer(ver);

        ApiReq.doGet(param, Content.REQ_CHECK_DATAUPDATE, uploadRecordSuc, uploadRecordErr);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                firstTime = System.currentTimeMillis();
//                while (go && System.currentTimeMillis() - firstTime < 6000) {
//                }
//                RequestUtil.getInstance().cancelAllRequests(Content.REQ_CHECK_DATAUPDATE);
//                if (go) {
//                    handler.sendEmptyMessage(2);
//                }
//            }
//        }).start();
    }


    private class ImageAsyncTask extends AsyncTask<String, Integer, String> {

        private String uri = "";
        private int from;

        public ImageAsyncTask(String uri, int from) {
            // TODO Auto-generated constructor stub
            this.uri = uri;
            this.from = from;
        }

        @Override
        protected String doInBackground(String... params) {
            if (from == 0) {
                //检查特征数据更新
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 取得inputStream，并进行读取
                    InputStream input = conn.getInputStream();
                    String path = Content.FILE_PATH_PARENT_OFFLINE_DATA;
                    if (new File(path).exists()) {
                        new File(path).delete();
                    }
                    FileOutputStream fos = new FileOutputStream(path);
                    byte[] data = new byte[4096];
                    int count = -1;
                    while ((count = input.read(data)) > 0) {
                        fos.write(data, 0, count);
                    }
                    fos.close();
                    input.close();
                    File file = new File(XMLParseUtil.FEATURE_PATH);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    XMLParseUtil.upZipFile(SlashActivity.this, new File(path), XMLParseUtil.FEATURE_PATH);
                    List<Element> childList = new XMLParseUtil(SlashActivity.this).getFeatures();
                    for (int i = 0; i < childList.size(); i++) {
                        new XMLParseUtil(SlashActivity.this).getFeature(childList.get(i));
                        publishProgress((int) (((i + 1) / (float) childList.size()) * 100));
                    }
                    if (!TextUtils.isEmpty(dataVersion))
                        //默认dataVer=5.3
                        LocalSharePreferences.commit(getApplicationContext(), Content.SP_DATABASE_VERSION, dataVersion);

                    //将特征文件包删除
                    FileUtil.deleteFile(file);

                    Log.i("info", "success");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (from == 1) {
                //没有检测到已复制数据库文件标志，将wildbird.db文件复制到数据库文件夹

                CopyDatabaseUtil.copyDataBase(SlashActivity.this);

                handler.sendEmptyMessageDelayed(2, 3000);

            } else if (from == 2) {
                //将基础数据生成wildbird.db
                try {
                    new XMLParseUtil(getApplicationContext()).getBirds();
                } catch (DocumentException e) {
                    // TODO Auto-generated catch block
                    Log.i("info", e.getMessage());
                }
                Log.i("info", "from == 2 end");
//                handler.sendEmptyMessage(1);
            } else if (from == 3) {
                //将特征数据写入wildbird.db文件
                try {
                    List<Element> childList = new XMLParseUtil(SlashActivity.this).getFeatures();
                    for (int i = 0; i < childList.size(); i++) {
                        Log.i("info", "parse " + i);
                        new XMLParseUtil(SlashActivity.this).getFeature(childList.get(i));
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.i("info", "from == 3 end");
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            if (dialog != null) dialog.setMessage("正在解析数据包 " + values[0] + "%");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (from == 0) {
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                handler.sendEmptyMessage(1);
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
        handler.removeCallbacksAndMessages(null);
    }


    //     获取离线数据失败
    private Response.ErrorListener uploadRecordErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            if (null != error && error instanceof NetException) {
                if (((NetException) error).getState() == ErrState.TIMEOUT) {
                    //超时立即,跳转主页
                    handler.sendEmptyMessage(2);
                    return;
                }
            }

            //5s后跳转主页
            handler.sendEmptyMessageDelayed(2, 5000);

        }
    };

    //      获取离线数据成功
    private Response.Listener<CommonResponse<CheckDataUpdSucRepo>> uploadRecordSuc =
            new Response.Listener<CommonResponse<CheckDataUpdSucRepo>>() {

                @Override
                public void onResponse(CommonResponse<CheckDataUpdSucRepo> response) {
                    CheckDataUpdSucRepo checkDataUpd = response.getT();
                    int needUpdate = checkDataUpd.getNeedUpdate();

                    if (needUpdate > 0) {
                        //需要更新
                        if (!isFinishing()) {
                            LocalSharePreferences.commit(getApplicationContext(), Content.SP_NEED_UPDATE,
                                    needUpdate + "");
                            dataVersion = checkDataUpd.getCurDataVer();

                            LocalSharePreferences.commit(getApplicationContext(), Content.SP_DOWN_URL,
                                    checkDataUpd.getDownUrl());
                            go = false;
                            MyAlertDialogUtil.showUploadDownloadDialog(SlashActivity.this);
                        }
                    } else {
                        //不需要更新离线包,4s后跳转主页
                        handler.sendEmptyMessageDelayed(2, 4000);
                    }
                }
            };


    /**
     * 显示广告图片
     */
    private void showBanner() {

        //获取本地存储的广告列表
        List<Banner> banners = LocalSpManager.getBanner();


        if (banners.isEmpty()) {
            return;
        }

//        在广告列表范围内随机显示广告
        Random random = new Random();
        int index = random.nextInt(banners.size());
        Banner banner = banners.get(index);

        if (TextUtils.isEmpty(banner.getImage_url())) {
            return;
        }

        //显示广告图片
        ImageLoaderUtil.getInstance().showBanner(ivActivitySlashBranner, banner.getImage_url());


        UiUtils.showView(btnSlip,true);

//        设置广告的点击事件
        ivActivitySlashBranner.setTag(banner.getHttp_url());
        ivActivitySlashBranner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                友盟广告点击统计
                UmengEventUtil.onBannerEvent((String) v.getTag());
                //跳转浏览器
                GotoActivityUtil.gotoBrowser(v.getContext(), (String) v.getTag());
            }
        });

    }


    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

}
