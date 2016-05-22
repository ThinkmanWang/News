package com.thinkman.chinabestnews.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.thinkman.chinabestnews.R;
import com.thinkman.chinabestnews.adapter.NewsAdapter;
import com.thinkman.chinabestnews.models.NewsModel;
import com.thinkman.chinabestnews.utils.FavoritesDbUtils;

import java.util.List;

import me.maxwin.view.XListView;

public class FavoriteListActivity extends AppCompatActivity
        implements XListView.IXListViewListener{

    private XListView mListView = null;
    private NewsAdapter mAdapter = null;

    private static final int PAGE_SIZE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        initView();
        initData();
    }

    private void initView() {
        mListView = (XListView) this.findViewById(R.id.main_list);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsModel news = mAdapter.getItem(position - 1);

                Intent intent = new Intent(FavoriteListActivity.this, NewsActivity.class);
                intent.putExtra(NewsActivity.CTIME, news.getCtime());
                intent.putExtra(NewsActivity.TITLE, news.getTitle());
                intent.putExtra(NewsActivity.DESCRIPTION, news.getDescription());
                intent.putExtra(NewsActivity.PIC_URL, news.getPicUrl());
                intent.putExtra(NewsActivity.URL, news.getUrl());

                intent.putExtra(NewsActivity.SHOW_FAVORITE, false);
                FavoriteListActivity.this.startActivity(intent);
            }
        });
        mAdapter = new NewsAdapter(FavoriteListActivity.this);
        mListView.setAdapter(mAdapter);
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<NewsModel> listNews = FavoritesDbUtils.getFavorite(FavoriteListActivity.this, 0, PAGE_SIZE);
                if (null == listNews || 0 == listNews.size()) {
                    return;
                }

                FavoriteListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        mAdapter.addAll(listNews);

                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime("2016-05-15");
                    }
                });
            }
        }).start();

    }

    public void onRefresh() {
        initData();
    }

    public void onLoadMore() {
        initData();
    }
}
