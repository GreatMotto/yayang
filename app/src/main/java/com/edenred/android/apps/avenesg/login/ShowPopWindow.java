package com.edenred.android.apps.avenesg.login;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.bean.StringBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.view.PopAdapter;

import java.util.List;

/**
 * Created by Administrator on 2015/7/23.
 */
public class ShowPopWindow {
    private static PopAdapter sortAdapter;
    private static PopupWindow popSort;
//    private static List<StringBean> listpop = new ArrayList<StringBean>();
    /**
     * popwindow
     *
     * @param v      点击的view
     * @param x      x位置
     * @param y      y位置
     */
    public static void showPopWindow(Context mContext, final View v, int x, int y, final TextView tv,
                                       final List<StringBean> list, final PopListener listener) {
        View popv1 = LayoutInflater.from(mContext).inflate(com.edenred.android.apps.avenesg.R.layout.pop, null);
        FontManager.applyFont(mContext, popv1, Constant.TTFNAME);
        if (popSort == null) {
            popSort = new PopupWindow(popv1, tv.getWidth(),
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        popSort.setFocusable(true);
        popSort.setBackgroundDrawable(new BitmapDrawable());
        popSort.setOutsideTouchable(true);
        popSort.update();
        popSort.showAsDropDown(v, x, y);
        popSort.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                popSort = null;
            }
        });
        ListView listview = (ListView) popv1.findViewById(com.edenred.android.apps.avenesg.R.id.lv_choice);
        sortAdapter = new PopAdapter(mContext, list);
        listview.setAdapter(sortAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                tv.setText(list.get(position).text);
                listener.getPopWindowPosition(v,position,tv);
                popSort.dismiss();
            }
        });
    }

    public interface PopListener
    {
        void getPopWindowPosition(View v,int pos,TextView tv);
    }
}
