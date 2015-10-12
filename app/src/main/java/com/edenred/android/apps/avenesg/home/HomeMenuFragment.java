package com.edenred.android.apps.avenesg.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.edenred.android.apps.avenesg.R;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.utils.FontManager;

/**
 * Created by zhaoxin on 2015/7/15.
 * 侧滑菜单
 */
public class HomeMenuFragment extends Fragment{

    private ListView lv_home_menu;
    private MenuAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fg_home_menu,container,false);
        FontManager.applyFont(getActivity(), getActivity().getWindow().
                getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        lv_home_menu= (ListView) view.findViewById(R.id.lv_home_menu);
        adapter=new MenuAdapter(getActivity());
        lv_home_menu.setAdapter(adapter);

        return view;
    }
}
