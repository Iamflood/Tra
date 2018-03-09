package com.example.mrflood.viewpagerdemo;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    ViewPager pager;
    List<View> list;
    String[] titles = {"一", "二", "三", "四", "五", "六"};
    LinearLayout continer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypager);
        pager = findViewById(R.id.mypager);
        continer = findViewById(R.id.continer);
        LayoutInflater inflater = LayoutInflater.from(this);
        list = new ArrayList<>();
        list.add(inflater.inflate(R.layout.layout1, null));
        list.add(inflater.inflate(R.layout.layout2, null));
        list.add(inflater.inflate(R.layout.layout3, null));
        list.add(inflater.inflate(R.layout.layout4, null));
        list.add(inflater.inflate(R.layout.layout5, null));
        list.add(inflater.inflate(R.layout.layout6, null));
        pager.setAdapter(new MyPager());
        pager.setPageTransformer(true, new DepthPageTransformer());
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = position - 1; i <= position + 1; i++) {
                    Log.d("问", "onPageSelected: " + i);
                    LinearLayout view = (LinearLayout) continer.getChildAt(i);
                    if (view == null) continue;
                    if (i == position) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
                        params.rightMargin = 35;
                        view.setLayoutParams(params);
                        view.setBackgroundResource(R.drawable.white);
                    } else {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(35, 35);
                        params.rightMargin = 35;
                        view.setLayoutParams(params);
                        view.setBackgroundResource(R.drawable.black);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public class MyPager extends PagerAdapter {
        String TAG = "MyPager";

        @Override
        public int getCount() {
            Log.d(TAG, "getCount() returned: " + list.size());
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            Log.d(TAG, "isViewFromObject() called with: view = [" + view + "], object = [" + object + "] " +
                    "returned: " + (view == object));
            return view == list.get((Integer) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            Log.d(TAG, "instantiateItem() called with: container = [" + container + "], " +
                    "position = [" + position + "] returned: " + list.get(position));
            return position;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            /*super.destroyItem(container, position, object);*/
            container.removeView(list.get(position));
            Log.d(TAG, "destroyItem() called with: container = [" + container + "], position = [" + position + "], object = [" + object + "]");
        }

        /*@Override
        public CharSequence getPageTitle(int position) {
            Log.d(TAG, "getPageTitle() called with: position = [" + position + "] returned: " + titles[position]);
            return titles[position];
        }*/
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            Log.d("DepthPageTransformer", view.getTag() + " , " + position + "");
            int pageWidth = view.getWidth();
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);
            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);
            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);
                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);
                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        boolean isget = false;
        int width;
        int height;

        public void transformPage(View view, float position) {
            /*https://www.2cto.com/kf/201608/540650.html*/
            Log.e("TAG", view + " , " + position + "");
            if ((position < 0 && position > -1) || (position > 0 && position < 1)) {
                if (!isget) {
                    width = view.getWidth();
                    height = view.getHeight();
                    isget = true;
                }
            } else {
                view.setX(width/2);
                view.setY(height/2);
                float scaleFactor = 1 - Math.abs(position);
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            }
        }
    }


}
