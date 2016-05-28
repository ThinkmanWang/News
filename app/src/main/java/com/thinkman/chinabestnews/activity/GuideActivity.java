package com.thinkman.chinabestnews.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.thinkman.chinabestnews.MainActivity;
import com.thinkman.chinabestnews.R;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorPagerAdapter;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorViewPagerAdapter;
import com.thinkman.chinabestnews.utils.SharedPreferencesUtil;

import java.util.Objects;


public class GuideActivity extends AppCompatActivity {

    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        if ((boolean)SharedPreferencesUtil.getData(this, "first_launch", true)) {

        } else {
            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.guide_viewPager);
        Indicator indicator = (Indicator) findViewById(R.id.guide_indicator);
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        inflate = LayoutInflater.from(getApplicationContext());
        indicatorViewPager.setAdapter(adapter);
    }

    private IndicatorPagerAdapter adapter = new IndicatorViewPagerAdapter() {
        private int[] images = { R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4 };

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.tab_guide, container, false);
            }
            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = new View(getApplicationContext());
                convertView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
            convertView.setBackgroundResource(images[position]);

            if (3 == position) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        SharedPreferencesUtil.saveData(GuideActivity.this, "first_launch", false);

                        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            return convertView;
        }

        @Override
        public int getCount() {
            return images.length;
        }
    };

}
