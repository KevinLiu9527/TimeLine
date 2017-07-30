package com.kevin.timeline.base;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;

import cn.jpush.android.api.JPushInterface;

/**
 * 项目名称：Robins
 * 创建人：KevinLiu   E-mail:KevinLiu9527@163.com
 * 创建时间：2017/7/5 10:28
 * 描述：管理应用
 */
public class AppContext extends Application {

    private boolean isDebug = false;
    private RefWatcher refWatcher;
    public MapView mapView = null;
    public BaiduMap baiduMap = null;
    //    public LocationTool locationTool;
    private AppContext appContext = null;

    //当前设备信息
//    public static GetDevicelist.DataBean.DevicesBean dBean;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
//        locationTool = new LocationTool(this);
        SDKInitializer.initialize(getApplicationContext());
        Fresco.initialize(this);
//        // 设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(false);
//        // 初始化 JPush
        JPushInterface.init(this);
        refWatcher = LeakCanary.install(this);
        // 网络请求
        initNohttp();
        // 是否开启debug
        setAppDebug();
//        //设置固定值
//        dBean = new GetDevicelist.DataBean.DevicesBean();
//        dBean.setId("713");
//        dBean.setImei("862809030063037");

    }

    /**
     * 初始化noHttp
     */
    private void initNohttp() {
        InitializationConfig config = InitializationConfig.newBuilder(appContext)
                // 全局连接服务器超时时间，单位毫秒，默认10s。
                .connectionTimeout(60 * 1000)
                // 全局等待服务器响应超时时间，单位毫秒，默认10s。
                .readTimeout(60 * 1000)
                // 不使用缓存
                .cacheStore(new DBCacheStore(appContext).setEnable(false))
                // 默认保存数据库DBCookieStore 设置不维护它
                .cookieStore(new DBCookieStore(appContext).setEnable(false))
                // 默认使用HttpURLConnection 设置为OKhttp
                .networkExecutor(new OkHttpNetworkExecutor())
                .build();
        NoHttp.initialize(config);
    }

    /**
     * 观察
     */
    public static RefWatcher getRefWatcher(Context context) {
        AppContext application = (AppContext) context.getApplicationContext();
        return application.refWatcher;
    }

    /**
     * 开启Debug调试
     */
    private void setAppDebug() {
        // 开启NoHttp调试模式。
//        Logger.setDebug(isDebug);
        // 设置NoHttp打印Log的TAG。
        Logger.setTag("NoHttpSample");
    }

    /**
     * 初始化百度地图
     *
     * @param mapView
     */
    public void initMap(MapView mapView) {
        this.mapView = mapView;
        this.baiduMap = mapView.getMap();
        this.mapView.showZoomControls(false);
        this.mapView.showScaleControl(false);
    }

    public void baiduMapClear() {
        if (null != baiduMap) {
            baiduMap.clear();
        }
    }
}
