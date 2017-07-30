package com.kevin.timeline.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.kevin.timeline.BaiduMapUtils;
import com.kevin.timeline.R;
import com.kevin.timeline.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author：KevinLiu
 * @E-mail:KevinLiu9527@163.com
 * @time 2017/7/28 13:50
 * 备注：    9339设备
 */
public class RoadWellFragment extends BaseFragment implements OnGetGeoCoderResultListener {


    @BindView(R.id.roadwell_map)
    MapView mapRoadwell;
    @BindView(R.id.Roadwell_tv)
    TextView RoadwellTv;

    //绑定布局
    private View view;
    private LayoutInflater mInflater;
    private Unbinder unbinder;

    //位图描述符
    private BitmapDescriptor bDescriptor;
    // 地理位置搜索
    private GeoCoder mSearch = null;
    private LatLng latLng = null;
    // 当前marker
    private Marker nowMarker;
    //Text
    private Text nameMarker;
    private BaiduMap bmap;

    /**
     * Fragment
     */
    public static RoadWellFragment newInstance() {
        RoadWellFragment fragment = new RoadWellFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_road_well, container, false);
        mInflater = inflater;
        unbinder = ButterKnife.bind(this, view);
        setViewAndListener();
        return view;
    }

    /**
     * 初始化
     */
    private void setViewAndListener() {
        latLng = new LatLng(31.317309, 121.401356);
        bmap = mapRoadwell.getMap();
        //地图显示
        mapRoadwell.showZoomControls(false);
        mapRoadwell.showScaleControl(true);
        bmap.setMapStatus(MapStatusUpdateFactory.zoomTo(17));
        BaiduMapUtils.removeBaiduMapIcon(mapRoadwell);
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        initData();
    }


    /**
     * 地图显示Marker
     */
    private void initData() {
        try {
            if (null == bDescriptor) {
                bDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_my_location_address);
            }
            OverlayOptions option = new MarkerOptions().position(latLng).icon(bDescriptor);
            bmap.addOverlay(option);
            nowMarker = (Marker) (bmap.addOverlay(option));
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(nowMarker.getPosition()));
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(nowMarker.getPosition());
            bmap.animateMapStatus(u, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        mapRoadwell.onPause();
    }

    @Override
    public void onDestroy() {
        mapRoadwell.onDestroy();
        try {
            if (null != bmap) {
                bmap.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapRoadwell.onResume();
    }


    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        String address = reverseGeoCodeResult.getAddress();
        if (null != nowMarker) {
            // 构建文字Option对象，用于在地图上添加文字
            TextOptions textOption = new TextOptions()
                    .bgColor(0xFF157fcc)
                    .fontSize(18)
                    .fontColor(0xFFFFFFFF)
                    .text(address)
                    .align(TextOptions.ALIGN_CENTER_HORIZONTAL,
                            TextOptions.ALIGN_TOP).position(latLng);
            // 在地图上添加该文字对象并显示
            nameMarker = (Text) bmap.addOverlay(textOption);
        }
    }
}
