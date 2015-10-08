package com.edenred.android.apps.avenesg.home;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by zhaoxin on 2015/7/15.
 * 首页
 */
public class HomeActivity extends BaseViewActivity{

    private Fragment mContent;
    private SlidingMenu sm;
    public static HomeActivity instanceHomeAc = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_main);
        instanceHomeAc = this;
        // check if the content frame contains the menu frame
        if (findViewById(com.edenred.android.apps.avenesg.R.id.menu_frame) == null) {
            setBehindContentView(com.edenred.android.apps.avenesg.R.layout.menu_frame);
            getSlidingMenu().setSlidingEnabled(true);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }
        else {
            // add a dummy view
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

        // set the Above View Fragment
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }

        if (mContent == null) {
            // mContent = new ContentFragment();
            mContent = new HomeFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(com.edenred.android.apps.avenesg.R.id.content_frame, mContent)
                .commit();

        // set the Behind View Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(com.edenred.android.apps.avenesg.R.id.menu_frame, new HomeMenuFragment()).commit();

        // customize the SlidingMenu
        sm = getSlidingMenu();
        sm.setBehindOffsetRes(com.edenred.android.apps.avenesg.R.dimen.dim289);//设置侧滑后主页的大小
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);//设置边界滑动
        sm.setFadeEnabled(false);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);

        sm.setBackgroundImage(com.edenred.android.apps.avenesg.R.color.grey_dark);//设置背景图片或颜色
        sm.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer()
        {

            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
                canvas.scale(scale, scale, -canvas.getWidth() / 2, canvas.getHeight() / 2);
            }
        });

        sm.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer()
        {

            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (1 - percentOpen * 0.00);
                canvas.scale(scale, scale, 0, canvas.getHeight());
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    @Override
    protected int getAcIndex() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void toggleMenu() {
        sm.toggle();
    }

}
