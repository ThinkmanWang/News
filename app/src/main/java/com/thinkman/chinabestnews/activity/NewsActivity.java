package com.thinkman.chinabestnews.activity;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;

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

    public static final String SHOW_FAVORITE = "show_favorite";

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
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long nRet = FavoritesDbUtils.insertFavorite(NewsActivity.this, mNews);
                        if (-2 == nRet) {
                            mHandler.sendEmptyMessage(MSG_FAVORITE_ALREADY_EXISTS);
                        } else if (-1 == nRet || 0 == nRet) {
                            mHandler.sendEmptyMessage(MSG_ADD_FAVORITE_FAILED);
                        } else {
                            mHandler.sendEmptyMessage(MSG_ADD_FAVORITE_SUCCESS);
                        }
                    }
                }).start();

                UMImage image = new UMImage(NewsActivity.this, mNews.getPicUrl());
                new ShareAction(NewsActivity.this).setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL)
                        .withText(mNews.getUrl())
                        .withMedia(image)
                        .setCallback(umShareListener)
                        .open();

            }
        });

        boolean bShowFavorite = getIntent().getBooleanExtra(SHOW_FAVORITE, true);
        if (bShowFavorite) {
            m_fabFavorite.setVisibility(View.VISIBLE);
        } else {
            m_fabFavorite.setVisibility(View.GONE);
        }

        mTitle = getIntent().getStringExtra(TITLE);
        mUrl = getIntent().getStringExtra(URL);

        if (false == TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(NewsActivity.this,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(NewsActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(NewsActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(NewsActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    public static final int MSG_ADD_FAVORITE_SUCCESS = 1;
    public static final int MSG_ADD_FAVORITE_FAILED = 2;
    public static final int MSG_FAVORITE_ALREADY_EXISTS = 3;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ADD_FAVORITE_SUCCESS:
                    Snackbar.make(m_fabFavorite, "Add to Favorites success!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;
                case MSG_ADD_FAVORITE_FAILED:
                    Snackbar.make(m_fabFavorite, "Add to Favorites failed!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;
                case MSG_FAVORITE_ALREADY_EXISTS:
                    Snackbar.make(m_fabFavorite, "Favorites already exists", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;
            }
        }
    };
}
