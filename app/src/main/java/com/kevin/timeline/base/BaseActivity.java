package com.kevin.timeline.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

/**
 * 项目名称：
 * 创建人：KevinLiu   E-mail:KevinLiu9527@163.com
 * 创建时间：2017/7/5 10:35
 * 描述：父类activity
 */
public class BaseActivity extends FragmentActivity {
//    private Dialog loadingDialog;

    /**
     * 请求队列。
     */
    private RequestQueue requestQueue;
    /**
     * 用来标记取消。
     */
    private Object object = new Object();

    public Gson gson = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        BaseActivityStack.create().addActivity(this);
//        loadingDialog = AppDialog.gifLoadingForDialog(this, "");
        requestQueue = NoHttp.newRequestQueue();
    }

    @Override
    protected void onDestroy() {
        // 退出时取消这个队列中的所有请求
        requestQueue.cancelBySign(object);
        // 退出activity时停止队列。
        requestQueue.stop();
//        BaseActivityStack.create().finishActivity(this);
        super.onDestroy();
    }

    /**
     * http请求
     *
     * @param what     用来表示http请求
     * @param request  请求对象
     * @param listener 监听
     * @param <T>      想请求到的数据类型
     */
    public <T> void httpRequest(int what, Request<T> request, OnResponseListener<T> listener) {
        request.setCancelSign(object);
        requestQueue.add(what, request, listener);
    }

    /**
     * 关闭加载框
     */
    public void dismissLoadingDialog() {
//        if (null != loadingDialog) {
//            if (loadingDialog.isShowing()) {
//                loadingDialog.dismiss();
//            }
//        }
    }

    /**
     * 显示加载框
     */
    public void showLoadingDialog() {
//        if (null != loadingDialog) {
//            if (!loadingDialog.isShowing()) {
//                loadingDialog.show();
//            }
//        }
    }

    /**
     * 短时间吐司
     *
     * @param msg
     */
    public void showToast(String msg) {
//        ToastTools.showToast(this, msg, Toast.LENGTH_SHORT);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间吐司
     *
     * @param msg
     */
    public void showToastLong(String msg) {

//        ToastTools.showToast(this, msg, Toast.LENGTH_LONG);
    }

    /**
     * 由此act跳转指定的act
     *
     * @param act
     */
    public void startToAty(Class<?> act) {
        Intent intent = new Intent(this, act);
        startActivity(intent);
    }

    /**
     * 由此act跳转指定的act
     *
     * @param act
     */
    public void startToAty(Class<?> act, Intent intent) {
        intent.setClass(this, act);
        startActivity(intent);
    }

    /**
     * 由此act跳转指定的act，并且牺牲自己
     *
     * @param act
     * @param intent
     * @param activity
     */
    public void startToAtyFinishThis(Class<?> act, Intent intent, Activity activity) {
        startToAty(act, intent);
        activity.finish();
    }

    /**
     * 由此act跳转指定的act，并且牺牲自己
     *
     * @param act
     * @param activity
     */
    public void startToAtyFinishThis(Class<?> act, Activity activity) {
        startToAty(act);
        activity.finish();
    }
}