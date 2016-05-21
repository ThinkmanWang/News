package com.thinkman.chinabestnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.thinkman.chinabestnews.R;
import com.thinkman.chinabestnews.models.NewsModel;

/**
 * Created by wangx on 2016/5/15.
 */
public class NewsAdapter extends ThinkBaseAdapter<NewsModel> {

    public NewsAdapter(Context context) {
        super(context);
    }

        @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem, parent, false);
        }

        ImageView image = ViewHolder.get(convertView, R.id.news_image);
        TextView title = ViewHolder.get(convertView, R.id.news_title);

            NewsModel data = (NewsModel) getItem(position);

            title.setText(data.getTitle());

        return convertView;
    }

}

/*存放控件 的ViewHolder*/



