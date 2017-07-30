package com.kevin.timeline;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebHistoryItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kevin.timeline.base.BaseActivity;
import com.kevin.timeline.fragment.LocatorFragment;
import com.kevin.timeline.fragment.LuatFragment;
import com.kevin.timeline.fragment.MacrocellFragment;
import com.kevin.timeline.fragment.RoadWellFragment;
import com.kevin.timeline.service.MyTimeService;
import com.squareup.haha.perflib.Main;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author：KevinLiu
 * @E-mail:KevinLiu9527@163.com
 * @time 2017/7/28 12:26
 * 备注： 主界面
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.sdv_device_header)
    SimpleDraweeView sdvDeviceHeader;
    @BindView(R.id.tab_left_rl)
    RelativeLayout tabLeftRl;
    @BindView(R.id.TV_title_name)
    TextView tabTitleName;
    @BindView(R.id.sdv_user_header)
    SimpleDraweeView sdvUserHeader;
    @BindView(R.id.main_right_rl)
    RelativeLayout mainRightRl;
    @BindView(R.id.all_layout)
    LinearLayout allLayout;
    @BindView(R.id.main_left_drawer_RL)
    RelativeLayout mLeftDrawerRL;
    @BindView(R.id.main_right_drawer_RL)
    RelativeLayout mRightDrawerRL;
    @BindView(R.id.main_drawer_DL)
    DrawerLayout mDrawerDL;

    //倒计时
    public ServiceConnection sc;
    public MyTimeService.MyBinder myBinder;
    public MyReceiver myReceiver;

    //屏幕宽度
    private int screenWidth = 0;
    //Frament管理器
    private FragmentManager fragmentManager;
    FragmentTransaction transaction;
    //9339
    private RoadWellFragment roadWellFragment;
    //9338
    private MacrocellFragment macrocellFragment;
    //5315s
    private LocatorFragment locatorFragment;
    //Luat
    private LuatFragment luatFragment;
    //默认界面
    private int defaultPage = 0;

    /**
     * view绘制监听，用来动态设置DrawerLayout子View
     */
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver
            .OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            DrawerLayout.LayoutParams leftParams = (DrawerLayout.LayoutParams)
                    mLeftDrawerRL.getLayoutParams();
            DrawerLayout.LayoutParams rightParams = (DrawerLayout.LayoutParams)
                    mRightDrawerRL.getLayoutParams();
            int width = 0;
            width = (screenWidth / 4) * 3;
            leftParams.width = width;
            rightParams.width = width;
            mLeftDrawerRL.setLayoutParams(leftParams);
            mRightDrawerRL.setLayoutParams(rightParams);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mLeftDrawerRL.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * 初始化
     */
    private void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        screenWidth = dm.widthPixels;
        // 规避DrawerLayout显示出来 底层的点击事件会被触发
        mLeftDrawerRL.setOnClickListener(null);
        mRightDrawerRL.setOnClickListener(null);
        mDrawerDL.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        initReciver();
        //Fragment切换
        fragmentManager = getSupportFragmentManager();
        checkedFragment(defaultPage);
    }

    /**
     * 初始化广播
     */
    private void initReciver() {
        myReceiver = new MyReceiver();
        sc = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                myBinder = (MyTimeService.MyBinder) iBinder;
                myBinder.openorStop(true);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };
        bindService(new Intent(MainActivity.this, MyTimeService.class), sc, Context.BIND_AUTO_CREATE);
    }


    /**
     * fragment
     */
    private void checkedFragment(int viewId) {
        // 开启Fragment事务
        transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragments(transaction);
        switch (viewId) {
            case 0://9339项目
                if (null == roadWellFragment) {
                    roadWellFragment = RoadWellFragment.newInstance();
                    transaction.add(R.id.frameLayout, roadWellFragment);
                } else {
                    transaction.show(roadWellFragment);
                }
                break;
            case 1://9338项目

                if (null == macrocellFragment) {
                    macrocellFragment = MacrocellFragment.newInstance();
                    transaction.add(R.id.frameLayout, macrocellFragment);
                } else {
                    transaction.show(macrocellFragment);
                }
                break;
            case 2://Luat项目

                if (null == luatFragment) {
                    luatFragment = LuatFragment.newInstance();
                    transaction.add(R.id.frameLayout, luatFragment);
                } else {
                    transaction.show(luatFragment);
                }
                break;
            default: //5315项目
                if (null == locatorFragment) {
                    locatorFragment = LocatorFragment.newInstance();
                    transaction.add(R.id.frameLayout, locatorFragment);
                } else {
                    transaction.show(locatorFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 隐藏所有Fragment
     */
    private void hideFragments(FragmentTransaction transaction) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (null != fragments) {
            for (int i = 0; i < fragments.size(); i++) {
                transaction.hide(fragments.get(i));
            }
        }
    }

    /**
     * 显示左右侧栏 关闭与显示：配件购买
     */
    public void initData() {
        mDrawerDL.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /**
     * 开关左侧弹出
     */
    public void switchOpenLeftLayout() {
        if (mDrawerDL.isDrawerOpen(mLeftDrawerRL)) {
            mDrawerDL.closeDrawer(mLeftDrawerRL);
        } else {
            mDrawerDL.openDrawer(mLeftDrawerRL);
        }
    }

    /**
     * 开关右侧弹出
     */
    public void switchOpenRightLayout() {
        if (mDrawerDL.isDrawerOpen(mRightDrawerRL)) {
            mDrawerDL.closeDrawer(mRightDrawerRL);
        } else {
            mDrawerDL.openDrawer(mRightDrawerRL);
        }
    }


    /**
     * 接口请求
     */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, registerFilter());
    }

    /**
     * 意图接收者
     */
    private IntentFilter registerFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyTimeService.TIMING);
        intentFilter.addAction(MyTimeService.LOCATION);
        return intentFilter;
    }


    /**
     * onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    /**
     * 销毁
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != sc) {
            unbindService(sc);
        }
    }

    /**
     * 接收广播
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                // 倒计时
                case MyTimeService.TIMING:
                    String timing = intent.getStringExtra("timing");
                    tabTitleName.setText("倒计时：" + timing);
                    break;
                // 定位
                case MyTimeService.LOCATION:
                    tabTitleName.setText("定位");
                    checkedFragment(defaultPage);
                    break;
            }
        }
    }

    @OnClick({R.id.tab_left_rl, R.id.main_right_rl, R.id.TV_title_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //左边
            case R.id.tab_left_rl:
                break;
            //右边
            case R.id.main_right_rl:
                break;
            //标题
            case R.id.TV_title_name:
                final String[] arrayFruit = new String[]{"苹果9339", "橘子9338", "草莓Luat", "香蕉5315"};

                Dialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("设备")
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(arrayFruit, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                defaultPage = which;
                                switch (which) {
                                    case 0:
                                        myBinder.setMaxTiming(20);
                                        checkedFragment(defaultPage);
                                        break;
                                    case 1:
                                        myBinder.setMaxTiming(30);
                                        checkedFragment(defaultPage);
                                        break;
                                    case 2:
                                        myBinder.setMaxTiming(50);
                                        checkedFragment(defaultPage);
                                        break;
                                    case 3:
                                        myBinder.setMaxTiming(60);
                                        checkedFragment(defaultPage);
                                        break;
                                }

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.show();

                break;
        }
    }
}
