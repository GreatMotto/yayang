package com.edenred.android.apps.avenesg.settings;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.utils.FontManager;

/**
 * @description 加载网页
 * @author 赵鑫
 * 
 * @time
 */
public class WebViewActivity extends BaseActivity {

    private WebView agreement_content;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_webview);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        initLogo();
        init();
    }

    private void init() {
        title = "";
        title = getIntent().getStringExtra("flag");
        String url = getIntent().getStringExtra("url");
        initTitle(title);
        agreement_content = (WebView) findViewById(com.edenred.android.apps.avenesg.R.id.agreement_content);

        agreement_content.loadUrl(url);

        agreement_content.getSettings().setJavaScriptEnabled(true);
        agreement_content.setWebViewClient(new HelloWebViewClient());
    }

    private class HelloWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

}
