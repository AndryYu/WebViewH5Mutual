package com.andryyu.webview.refreshview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.andryyu.webview.R;


/**
 * 自定义WebView支持下拉刷新的功能
 */
public class DavidWebViewActivity extends Activity {

    /**
     * 若是需要设置一下功能 可在DavidWebView中进行设置
     */
    private DavidWebView davidWebView;
    private ToggleButton tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_david_webview);

        davidWebView =  findViewById(R.id.davidWebView);
        tb =  findViewById(R.id.tb);
        initData();
    }


    protected void initData() {
        davidWebView.setOnRefreshWebViewListener(new OnRefreshWebViewListener() {
            @Override
            public void onRefresh() {
                // 模拟接口调用3秒
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        //调用接口结束
                        davidWebView.setRefreshSuccess();
//                        davidWebView.setRefreshFail();
                    }
                }.sendEmptyMessageDelayed(0, 3000);
            }
        });

        davidWebView.getWebView().loadUrl("http://www.jianshu.com/u/f4440f398c51");
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //支持刷新
                    davidWebView.setRefreshEnable(true);
                } else {
                    //不支持刷新
                    davidWebView.setRefreshEnable(false);
                }
            }
        });
    }
}
