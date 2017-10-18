package com.muedsa.wfapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.muedsa.wfapp.R;
import com.muedsa.wfapp.activity.MainActivity;
import com.muedsa.wfapp.adapter.VoidFissureAdapter;

public class VoidFissureFragment extends Fragment {

    public VoidFissureFragment(){}

    public static VoidFissureFragment newInstance() {
        VoidFissureFragment fragment = new VoidFissureFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(new VoidFissureAdapter(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                ((MainActivity)getActivity()).threadLock = 0;
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    ((MainActivity)getActivity()).threadLock = 1;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return rootView;
    }
}
