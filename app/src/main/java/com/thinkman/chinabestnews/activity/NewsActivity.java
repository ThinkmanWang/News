package com.thinkman.chinabestnews.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.thinkman.chinabestnews.R;

public class NewsActivity extends AppCompatActivity {

    private WebView mWebView = null;

    //for webview content
    private String mUrl = null;
    private String mTitle = null;

    public static final String TITLE = "title";
    public static final String URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mWebView = (WebView) findViewById(R.id.main_webview);
        mTitle = getIntent().getStringExtra(TITLE);
        mUrl = getIntent().getStringExtra(URL);

        if (false == TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        }
    }
}
