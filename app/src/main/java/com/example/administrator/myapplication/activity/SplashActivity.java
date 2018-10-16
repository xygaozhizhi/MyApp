package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.adapter.ViewPageAdapter;
import com.example.administrator.myapplication.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {
    private ViewPager mPager;
    private Button mBtnStart;
    private TextView mText;
    private LinearLayout ll_point_container;
    private List<View> mViewList = new ArrayList<>();
    private ViewPageAdapter mPageAdapter;
    //设置当前 第几个图片 被选中
    private int autoCurrIndex = 0;
    private static final int UPTATE_VIEWPAGER = 0;
    private ImageView point;
    private int recLen = 5;//跳过倒计时提示5秒
    Timer timer = new Timer();
    private Handler handler;
    private int[] imgRes = new int[]{
            R.mipmap.sp1, R.mipmap.sp2,
            R.mipmap.sp3, R.mipmap.sp4};
    // 文本描述
    String[] strings = new String[]{
            "美女1",
            "美女2",
            "美女3",
            "美女4"
    };
    //定时轮播图片，需要在主线程里面修改 UI
    /*private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPTATE_VIEWPAGER:
                    if (msg.arg1 != 0) {
                        mPager.setCurrentItem(msg.arg1);
                    } else {
                        //false 当从末页调到首页是，不显示翻页动画效果，
                        mPager.setCurrentItem(msg.arg1, false);
                    }
                    break;
            }
            return false;
        }
    });*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏
        initViews();
        initDatas();
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() { // UI thread
                @Override
                public void run() {
                    recLen--;
                    mBtnStart.setText("开始体验 " + recLen);
                    if (recLen < 0) {
                        timer.cancel();
                        mBtnStart.setVisibility(View.GONE);//倒计时到0隐藏字体
                    }
                }
            });
        }
    };

    private void initViews() {
        mPager = findViewById(R.id.vp_splash);
        mBtnStart = findViewById(R.id.guide_btn_start);
        ll_point_container = findViewById(R.id.ll_points);
        mText = findViewById(R.id.tv_title);
        handler = new Handler();
    }

    ImageView[] points = new ImageView[imgRes.length];

    private void initDatas() {
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < imgRes.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.item_splash, null);
            ImageView imageView = view.findViewById(R.id.iv_item_vp_splash);
            imageView.setBackgroundResource(imgRes[i]);
            mViewList.add(view);
            // 加小圆点, 指示器
            point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_selector);
            layoutParams = new LinearLayout.LayoutParams(15, 15);
            layoutParams.setMargins(5, 0, 5, 0);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.point_red);
            } else {
                point.setBackgroundResource(R.drawable.point_white);
            }
            points[i] = point;
            ll_point_container.addView(point, layoutParams);
        }
        mPageAdapter = new ViewPageAdapter(mViewList);
        mPager.setAdapter(mPageAdapter);
        mText.setText(strings[0]);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //设置文本
                mText.setText(strings[position]);
                // 一定几个图片，几个圆点，但注意是从0开始的
                int total = imgRes.length;
                for (int j = 0; j < total; j++) {
                    if (j == position) {
                        points[j].setBackgroundResource(R.drawable.point_red);
                    } else {
                        points[j].setBackgroundResource(R.drawable.point_white);
                    }
                }

                //设置全局变量，currentIndex为选中图标的 index
                autoCurrIndex = position;
                //最后一页
                if (position == mViewList.size() - 1) {
                    mBtnStart.setVisibility(View.VISIBLE);
                    mBtnStart.setText("开始体验 " + recLen);
                    /**
                     * 正常情况下不点击跳过
                     */
                    timer.schedule(task, 1000, 1000);//等待时间一秒，停顿时间一秒
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //从闪屏界面跳转到首界面
                            openActivity(MainActivity.class);
                            finish();
                        }
                    }, 5000);//延迟5S后发送handler信息
                    mBtnStart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openActivity(MainActivity.class);
                            finish();
                        }
                    });
                } else {
                    mBtnStart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 设置自动轮播图片，5s后执行，周期是5s
        /*timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = UPTATE_VIEWPAGER;
                if (autoCurrIndex == imgRes.length - 1) {
                    autoCurrIndex = -1;
                }
                message.arg1 = autoCurrIndex + 1;
                handler.sendMessage(message);
            }
        }, 1000, 1000);*/
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        //页面销毁的时候取消定时器
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }
}
