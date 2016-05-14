package com.thinkman.chinabestnews;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shizhefei.fragment.LazyFragment;

public class NewsFragment extends LazyFragment {
	private TextView textView;
	private int tabIndex;
	public static final String INTENT_INT_INDEX = "intent_int_index";

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.fragment_tabmain_item);

		tabIndex = getArguments().getInt(INTENT_INT_INDEX);
		textView = (TextView) findViewById(R.id.fragment_mainTab_item_textView);
		textView.setText("界面" + " " + tabIndex + " 加载完毕");
	}

	@Override
	public void onDestroyViewLazy() {
		super.onDestroyViewLazy();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			textView.setVisibility(View.VISIBLE);
		}
	};
}
