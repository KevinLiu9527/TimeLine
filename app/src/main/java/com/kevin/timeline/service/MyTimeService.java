package com.kevin.timeline.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 项目名称：Rosefinch
 *
 * @author KevinLiu  E-mail: 18356094131@163.com
 * @time 2016/10/13 14:55
 * 描述：地图实时定位Serivce
 */
public class MyTimeService extends Service {

    public static final String TIMING = "demo.com.myapplication_倒计时";
    public static final String LOCATION = "demo.com.myapplication_定位";
    public Timer mTimer;
    public TimerTask mTimerTask;
    public boolean isWhile = false;
    public int timing = 10;
    public int maxTiming = 10;
    public Intent timingIntent;
    public MyBinder myBinder;
    /*定义工作线程*/
    private WorkThreadHanlder mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        myBinder = new MyBinder();
        // 线程
        HandlerThread handlerThread = new HandlerThread("realLocService");
        handlerThread.start();
        // 使用工作线程
        mHandler = new WorkThreadHanlder(handlerThread.getLooper());
        Message msg = new Message();
        msg.what = 1;
        mHandler.sendMessage(msg);

    }

    /**
     * 开始定位定时器
     */
    public void StartLocationTimer() {
        mTimer = new Timer(true);
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                while (true) {
                    if (isWhile == false) {
                        continue;
                    }
                    timingIntent = new Intent();
                    timing = timing - 1;
                    if (0 == timing) {
                        timing = maxTiming;
                        timingIntent.setAction(LOCATION);
                    } else {
                        timingIntent.setAction(TIMING);
                    }
                    timingIntent.putExtra("timing", String.valueOf(timing));
                    sendBroadcast(timingIntent);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mTimer.schedule(mTimerTask, 1);
    }

    /**
     * 销毁定时器
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    /**
     * 停止定时器
     */
    private void stopTimer() {
        isWhile = false;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    /**
     * 耗时操作
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * 工作线程功能：实时定位
     */
    public class WorkThreadHanlder extends Handler {

        public WorkThreadHanlder(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    StartLocationTimer();
                    break;
            }
        }
    }

    /**
     * MyBinder
     */
    public class MyBinder extends Binder {
        public MyTimeService getService() {
            return MyTimeService.this;
        }

        // 开启或者终止 ture or false
        public void openorStop(boolean bl) {
            isWhile = bl;
        }

        // 设置倒计时数
        public void setMaxTiming(int max) {
            maxTiming = max;
            timing = max;
        }
    }
}
