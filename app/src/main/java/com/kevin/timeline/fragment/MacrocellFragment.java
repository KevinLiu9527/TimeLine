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

public class MacrocellFragment extends BaseFragment {

    private View view;
    private LayoutInflater mInflater;
    private Unbinder unbinder;

    public static MacrocellFragment newInstance() {
        MacrocellFragment fragment = new MacrocellFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_macrocell, container, false);
        mInflater = inflater;
        unbinder = ButterKnife.bind(this, view);
        setViewAndListener();
        return view;
    }

    private void setViewAndListener() {
    }

}
