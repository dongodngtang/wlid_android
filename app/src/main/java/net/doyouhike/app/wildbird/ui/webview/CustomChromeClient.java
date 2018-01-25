package net.doyouhike.app.wildbird.ui.webview;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebView;

import net.doyouhike.app.wildbird.biz.web.InjectedChromeClient;

/**
 * Created by zaitu on 15-11-11.
 */
public class CustomChromeClient extends InjectedChromeClient {

    public CustomChromeClient (String injectedName, Class injectedCls) {
        super(injectedName, injectedCls);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        // to do your work
        // ...
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public void onProgressChanged (WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        // to do your work
        // ...
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        // to do your work
        // ...
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
}