package net.doyouhike.app.wildbird.ui.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.event.BaseEvent;
import net.doyouhike.app.wildbird.biz.model.event.IObserver;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.util.LogUtil;
import net.doyouhike.app.wildbird.biz.web.InjectedChromeClient;

import java.util.Timer;

public class WebActivity extends BaseAppActivity implements IObserver {

    public static final String I_USER_NM = "params0";
    public static final String I_URL = "params1";

    private static final String TAG = WebActivity.class.getSimpleName();

    private WebView mWebView;
    private RelativeLayout mRootView;
    private View mRetryView;
    private ProgressBar mProgressBar;
    private String url;
    private Timer timer;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_web;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();

        //注入js监听事件
        HostJsScope.addObserver(this);

        setHeaderTitle(getString(R.string.title_activity_web));
        mRootView = (RelativeLayout) findViewById(R.id.vi_web_act_root);
        mWebView = (WebView) findViewById(R.id.wv_act_web_content);
        mRetryView = findViewById(R.id.vi_retry);
        mProgressBar=(ProgressBar)findViewById(R.id.pb_act_web_index);

        //支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(false);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebChromeClient(
                new InjectedChromeClient("HostApp", HostJsScope.class){

                    @Override
                    public void onProgressChanged (WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);

                        if (newProgress == 100) {
                            mProgressBar.setVisibility(View.GONE);
                        } else {
                            mProgressBar.setVisibility(View.VISIBLE);
                            mProgressBar.setProgress(newProgress);
                        }
                    }
                }
        );

        mWebView.setWebViewClient(new InfoDetailWebViewClient());

        url = getIntent().getStringExtra(I_URL);
        mWebView.loadUrl(url);

        timer=new Timer();
    }

    @Override
    protected void onDestroy() {
        HostJsScope.removedObserver(this);//js监听事件解耦

        mRootView.removeView(mWebView);
        mWebView.destroy();
        mWebView = null;
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public void onWbEvent(BaseEvent event) {
        final String userNm = event.getStrContent();

        LogUtil.d("onWbEvent", "activateSuccess  userNm:" + userNm);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                if (!TextUtils.isEmpty(userNm)) {
                    Intent intent = new Intent();
                    intent.putExtra(I_USER_NM, userNm);
                    setResult(RESULT_OK, intent);
                }
                WebActivity.this.finish();

            }
        });
    }

    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_retry:
                mWebView.loadUrl(url);
                setNormalVi();
                break;
        }
    }

    class InfoDetailWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.d(TAG, url);
            if (url.startsWith("tel:")) {
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                WebActivity.this.startActivity(intent);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            LogUtil.d(TAG,"onPageStarted:"+url+"  progress:"+view.getProgress());
        }

        @Override
        public void onPageFinished(WebView webView, String url) {

            super.onPageFinished(webView, url);

            LogUtil.d(TAG, "onPageFinished");
//            cancelTmTask();//取消超时计时

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//            super.onReceivedError(view, errorCode, description, failingUrl);
            LogUtil.d(TAG, "onReceivedError:" + errorCode);
            setErrVi();
        }

    }


    private void setErrVi() {
        mWebView.loadUrl("about:blank");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRetryView.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
            }
        });
        mProgressBar.setVisibility(View.GONE);
    }

    private void setNormalVi() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRetryView.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
            }
        });
    }

}
