package com.andryyu.webview.javatojs;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.andryyu.webview.R;
import com.andryyu.webview.utils.WebViewUtil;

import org.json.JSONObject;

public class JavaToJsActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings webSettings;
    private Toolbar mToolbar;
    private TextView mTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_tojs);

        mWebView = findViewById(R.id.webview);
        mToolbar = this.findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("");
        mTitleTextView = this.findViewById(R.id.toolbar_title);
        mTitleTextView.setText("Android调用Js方法");
        webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/js_interaction/hello.html");

        findViewById(R.id.one).setOnClickListener(mOnClickListener);
        findViewById(R.id.two).setOnClickListener(mOnClickListener);
        findViewById(R.id.three).setOnClickListener(mOnClickListener);
        findViewById(R.id.four).setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener=new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.one:
                    quickCallJs("callByAndroid", null);
                    break;
                case R.id.two:
                    quickCallJs("callByAndroidParam",null, "Hello ! Agentweb");
                    break;
                case R.id.three:
                    quickCallJs("callByAndroidMoreParams", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.i("Info","value:"+value);
                        }
                    },getJson(),"say:", " Hello! Agentweb");
                    break;
                case R.id.four:
                    quickCallJs("callByAndroidInteraction",null,"你好Js");
                    break;
            }
        }
    };


    public void callJs(String js, final ValueCallback<String> callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript(js, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    if (callback != null)
                        callback.onReceiveValue(value);
                }
            });
        } else {
            mWebView.loadUrl(js);
        }
    }

    public void quickCallJs(String method, ValueCallback<String> callback,String... params){
        StringBuilder sb=new StringBuilder();
        sb.append("javascript:"+method);
        if(params==null||params.length==0){
            sb.append("()");
        }else{
            sb.append("(").append(concat(params)).append(")");
        }
        callJs(sb.toString(),callback);
    }

    private String concat(String...params){
        StringBuilder mStringBuilder=new StringBuilder();
        for(int i=0;i<params.length;i++){
            String param=params[i];
            if(!WebViewUtil.isJson(param)){
                mStringBuilder.append("\"").append(param).append("\"");
            }else{
                mStringBuilder.append(param);
            }
            if(i!=params.length-1){
                mStringBuilder.append(" , ");
            }
        }
        return mStringBuilder.toString();
    }

    private String getJson(){
        String result="";
        try {
            JSONObject mJSONObject=new JSONObject();
            mJSONObject.put("id",1);
            mJSONObject.put("name","Agentweb");
            mJSONObject.put("age",18);
            result= mJSONObject.toString();
        }catch (Exception e){

        }
        return result;
    }
}
