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

import me.maxwin.view.XListView;

public class NewsFragment extends LazyFragment implements XListView.IXListViewListener {
	private int tabIndex;
	public static final String INTENT_INT_INDEX = "intent_int_index";

	private XListView mListView = null;
	private NewsAdapter mAdapter = null;

	private Handler mHandler = null;

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.fragment_tabmain_item);

		tabIndex = getArguments().getInt(INTENT_INT_INDEX);

		mHandler = new Handler();
		initView();
	}

	private void initView() {
		mListView = (XListView) this.findViewById(R.id.main_list);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		mAdapter = new NewsAdapter(this.getActivity());
		mListView.setAdapter(mAdapter);
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
