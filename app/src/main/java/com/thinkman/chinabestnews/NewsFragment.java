package com.thinkman.chinabestnews;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shizhefei.fragment.LazyFragment;
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

public class NewsFragment extends LazyFragment implements XListView.IXListViewListener {
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
		mAdapter = new NewsAdapter(this.getActivity());
		mListView.setAdapter(mAdapter);
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
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mListView.stopRefresh();
				mListView.stopLoadMore();
				mListView.setRefreshTime("2016-05-15");
			}
		}, 2000);
	}

	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mListView.stopRefresh();
				mListView.stopLoadMore();
				mListView.setRefreshTime("2016-05-15");
			}
		}, 2000);
	}
}
