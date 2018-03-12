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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    ViewPager pager;
    List<View> list;
    String[] titles = {"一", "二", "三", "四", "五", "六"};
    RadioGroup continer;

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

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                RadioButton button = (RadioButton) continer.getChildAt(position);
                button.toggle();
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

        public void transformPage(View view, float position) {
            int width = view.getWidth();
            int pivotX = 0;
            if (position <= 1 && position > 0) {// right scrolling
                pivotX = 0;
            } else if (position == 0) {

            } else if (position < 0 && position >= -1) {// left scrolling
                pivotX = width;
            }
            //设置x轴的锚点
            view.setPivotX(pivotX);
            //设置绕Y轴旋转的角度
            view.setRotationY(90f * position);
        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

        public void transformPage(View page, float position) {
            /*https://www.2cto.com/kf/201608/540650.html*/
            Log.e("TAG", page + " , " + position + "");
            int width = page.getWidth();
            int height = page.getHeight();

            //這裏只對右邊的view做了操作
            if (position > 0 && position <= 1) {//right scorlling
                //position是1.0->0,但是沒有等於0
                float minScale = 0.8f;
                position = minScale < position ? minScale : position;
                Log.e("TAG", "right----position====" + position);
                //設置該view的X軸不動
//                page.setTranslationX(-width * position);
                //設置縮放中心點在該view的正中心
                page.setPivotX(width / 2);
                page.setPivotY(height / 2);
                //設置縮放比例（0.0，1.0]
                page.setScaleX(1 - position);
                page.setScaleY(1 - position);
                page.setAlpha(1 - position);
            } else if (position >= -1 && position < 0) {//left scrolling

            } else {//center

            }
        }
    }


}
