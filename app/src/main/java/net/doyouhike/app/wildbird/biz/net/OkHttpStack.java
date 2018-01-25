package net.doyouhike.app.wildbird.biz.net;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ${luochangdong} on 15-12-4.
 */
public class OkHttpStack extends HurlStack {
    private final OkUrlFactory okUrlFactory;

    public OkHttpStack() {
        this(new OkUrlFactory(new OkHttpClient()));
    }

    public OkHttpStack(OkUrlFactory okUrlFactory) {
        if (okUrlFactory == null) {
            throw new NullPointerException("Client must not be null.");
        }
        this.okUrlFactory = okUrlFactory;
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return okUrlFactory.open(url);
    }
}

