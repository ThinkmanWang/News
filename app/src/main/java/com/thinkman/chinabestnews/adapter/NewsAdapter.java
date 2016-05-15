package com.thinkman.chinabestnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.thinkman.chinabestnews.R;

/**
 * Created by wangx on 2016/5/15.
 */
public class NewsAdapter extends BaseAdapter {
    private LayoutInflater mInflater; //得到一个LayoutInfalter对象用来导入布局

    public NewsAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 30;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem, null);
            holder = new ViewHolder();
                /*得到各个控件的对象*/
            holder.title = (TextView) convertView.findViewById(R.id.news_title);
//            holder.text = (TextView) convertView.findViewById(R.id.ItemText);
//            holder.bt = (Button) convertView.findViewById(R.id.ItemButton); // to ItemButton

            convertView.setTag(holder); //绑定ViewHolder对象
        }
        else {
            holder = (ViewHolder) convertView.getTag(); //取出ViewHolder对象
        }

            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
        holder.title.setText("新闻标题"+position);

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    class ViewHolder {
        public TextView title;
    }

}

/*存放控件 的ViewHolder*/



