package com.thinkman.chinabestnews.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

            NewsModel news = (NewsModel) getItem(position);

            title.setText(news.getTitle());

            if (news != null && false == TextUtils.isEmpty(news.getPicUrl())) {
                int nPicSize = mContext.getResources().getDimensionPixelSize(R.dimen.news_image_size);
                Glide.with(mContext)
                        .load(news.getPicUrl())
                        .override(nPicSize, nPicSize)
                        .into(image);
            }

        return convertView;
    }

}

/*存放控件 的ViewHolder*/



