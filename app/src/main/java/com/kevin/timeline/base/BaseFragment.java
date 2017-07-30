package com.kevin.timeline.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

/**
 * @author：KevinLiu
 * @E-mail:KevinLiu9527@163.com
 * @time 2017/7/5 10:51
 * 备注： Fragment父类
 */
public class BaseFragment extends Fragment {
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
//        loadingDialog = AppDialog.gifLoadingForDialog(getActivity(), "");
        requestQueue = NoHttp.newRequestQueue();
    }


    @Override
    public void onDestroyView() {
        requestQueue.stop();
        super.onDestroyView();
        AppContext.getRefWatcher(getActivity()).watch(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {
            requestQueue.cancelBySign(object);
        }
    }

    /**
     * http请求
     *
     * @param what
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
//        ToastTools.showToast(getActivity(), msg, Toast.LENGTH_SHORT);
    }

    /**
     * 长时间吐司
     *
     * @param msg
     */
    public void showToastLong(String msg) {
//        ToastTools.showToast(getActivity(), msg, Toast.LENGTH_LONG);
    }

    /**
     * 由此act跳转指定的act
     *
     * @param act
     */
    public void startToAty(Class<?> act) {
        Intent intent = new Intent(getActivity(), act);
        startActivity(intent);
    }

    /**
     * 由此act跳转指定的act
     *
     * @param act
     */
    public void startToAty(Class<?> act, Intent intent) {
        intent.setClass(getActivity(), act);
        startActivity(intent);
    }
}
