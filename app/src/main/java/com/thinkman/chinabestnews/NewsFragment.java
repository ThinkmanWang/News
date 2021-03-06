package com.thinkman.chinabestnews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shizhefei.fragment.LazyFragment;
import com.thinkman.chinabestnews.activity.NewsActivity;
import com.thinkman.chinabestnews.adapter.NewsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import me.maxwin.view.XListView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;

import com.thinkman.chinabestnews.models.*;

public class NewsFragment extends LazyFragment
		implements
		XListView.IXListViewListener {
	public static final String INTENT_INT_INDEX = "intent_int_index";

	private XListView mListView = null;
	private NewsAdapter mAdapter = null;

	private Handler mHandler = null;

	private int mTabIndex = 0;
	private int m_nCurrentPage = 1;

	//for OKHttp
	OkHttpClient mOkHttpClient = new OkHttpClient();

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.fragment_tabmain_item);

		mTabIndex = getArguments().getInt(INTENT_INT_INDEX);

		mHandler = new Handler();
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

				Intent intent = new Intent(NewsFragment.this.getActivity(), NewsActivity.class);
				intent.putExtra(NewsActivity.CTIME, news.getCtime());
				intent.putExtra(NewsActivity.TITLE, news.getTitle());
				intent.putExtra(NewsActivity.DESCRIPTION, news.getDescription());
				intent.putExtra(NewsActivity.PIC_URL, news.getPicUrl());
				intent.putExtra(NewsActivity.URL, news.getUrl());

				NewsFragment.this.getActivity().startActivity(intent);
			}
		});
		mAdapter = new NewsAdapter(this.getActivity());
		mListView.setAdapter(mAdapter);
	}

	private void loadMore() {
		final int nLoadPage = m_nCurrentPage + 1;

		Request.Builder requestBuilder = new Request.Builder().url(Contant.URLS[mTabIndex]
				.replaceAll("\\(\\$1\\)", Contant.APPKEY)
				.replaceAll("\\(\\$2\\)", ""+Contant.PAGE_SIZE)
				.replaceAll("\\(\\$3\\)", ""+nLoadPage));

		Request request = requestBuilder.build();
		Call httpCall= mOkHttpClient.newCall(request);

		httpCall.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (null != response.cacheResponse()) {
					String str = response.cacheResponse().toString();
				} else {
					final String szJson = response.body().string();
					//String str = response.networkResponse().toString();

					try {
						JSONObject jsonNewsList = new JSONObject(szJson);

						if (200 != jsonNewsList.getInt("code")) {
							return;
						}

						Gson gson = new Gson();
						final NewsListModel newsList = gson.fromJson(szJson, NewsListModel.class);
						mHandler.post(new Runnable() {
							@Override
							public void run() {

								m_nCurrentPage +=1;

								mAdapter.addAll(newsList.getNewslist());
								mHandler.sendEmptyMessageDelayed(1, 1000);

								mListView.stopRefresh();
								mListView.stopLoadMore();
								mListView.setRefreshTime("2016-05-15");

							}
						});
					} catch (JSONException ex) {

					}
				}


			}
		});
	}

	private void initData() {
		m_nCurrentPage = 1;
		Request.Builder requestBuilder = new Request.Builder().url(Contant.URLS[mTabIndex]
				.replaceAll("\\(\\$1\\)", Contant.APPKEY)
				.replaceAll("\\(\\$2\\)", ""+Contant.PAGE_SIZE)
				.replaceAll("\\(\\$3\\)", ""+m_nCurrentPage));


		Request request = requestBuilder.build();
		Call httpCall= mOkHttpClient.newCall(request);

		httpCall.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (null != response.cacheResponse()) {
					String str = response.cacheResponse().toString();
				} else {
					final String szJson = response.body().string();
					//String str = response.networkResponse().toString();

					try {
						JSONObject jsonNewsList = new JSONObject(szJson);

						if (200 != jsonNewsList.getInt("code")) {
							return;
						}

						Gson gson = new Gson();
						final NewsListModel newsList = gson.fromJson(szJson, NewsListModel.class);
						mHandler.post(new Runnable() {
							@Override
							public void run() {

								mAdapter.clear();
								mAdapter.addAll(newsList.getNewslist());
								mHandler.sendEmptyMessageDelayed(1, 1000);

								mListView.stopRefresh();
								mListView.stopLoadMore();
								mListView.setRefreshTime("2016-05-15");

							}
						});
					} catch (JSONException ex) {

					}
				}


			}
		});
	}

	@Override
	public void onDestroyViewLazy() {
		super.onDestroyViewLazy();
	}

	public void onRefresh() {
		initData();


	}

	public void onLoadMore() {
		loadMore();
	}
}
