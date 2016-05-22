package com.thinkman.chinabestnews.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.thinkman.chinabestnews.R;
import com.thinkman.chinabestnews.models.NewsModel;
import com.thinkman.chinabestnews.utils.FavoritesDbUtils;
import com.thinkman.chinabestnews.view.ProgressWebView;

public class NewsActivity extends AppCompatActivity {

    private ProgressWebView mWebView = null;
    FloatingActionButton m_fabFavorite = null;

    //for webview content
    private String mUrl = null;
    private String mTitle = null;

    private NewsModel mNews = new NewsModel();
    public static final String CTIME = "ctime";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String PIC_URL = "picUrl";
    public static final String URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mNews.setCtime(getIntent().getStringExtra(CTIME));
        mNews.setTitle(getIntent().getStringExtra(TITLE));
        mNews.setDescription(getIntent().getStringExtra(DESCRIPTION));
        mNews.setPicUrl(getIntent().getStringExtra(PIC_URL));
        mNews.setUrl(getIntent().getStringExtra(URL));

        mWebView = (ProgressWebView) findViewById(R.id.main_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        m_fabFavorite = (FloatingActionButton) findViewById(R.id.fab);
        m_fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long nRet = FavoritesDbUtils.insertFavorite(NewsActivity.this, mNews);
                if (-2 == nRet) {
                    Toast.makeText(NewsActivity.this, "收藏已经存在", Toast.LENGTH_SHORT).show();
                } else if (-1 == nRet || 0 == nRet) {
                    Toast.makeText(NewsActivity.this, "添加收藏失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewsActivity.this, "添加收藏成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mTitle = getIntent().getStringExtra(TITLE);
        mUrl = getIntent().getStringExtra(URL);

        if (false == TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        }
    }
}
