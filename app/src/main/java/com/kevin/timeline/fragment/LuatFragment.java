package com.kevin.timeline.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kevin.timeline.R;
import com.kevin.timeline.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LuatFragment extends BaseFragment {

    private View view;
    private LayoutInflater mInflater;
    private Unbinder unbinder;

    public static LuatFragment newInstance() {
        LuatFragment fragment = new LuatFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_luat, container, false);
        mInflater = inflater;
        unbinder = ButterKnife.bind(this, view);
        setViewAndListener();
        return view;
    }

    private void setViewAndListener() {
    }

}
