package top.dcoin.decoin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView wvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        wvMain = (WebView) findViewById(R.id.wvMain);
        wvMain.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.toLowerCase().endsWith(".apk")) {
                    //跳转到下载的Activity
//                    Intent intent=new Intent(MainActivity.this,UpdateActivity.class);
//                    intent.putExtra("url",url.toLowerCase());
//                    startActivity(intent);
//                    finish();
//                    return  false;
                    //默认浏览器中打开===================================================
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    finish();
                    return false;
                }
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = wvMain.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setMediaPlaybackRequiresUserGesture(false);


        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setDomStorageEnabled(true);
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);


        wvMain.loadUrl("file:///android_asset/flash.html");
        //wvMain.loadUrl("file:///android_asset/index.html");

        //wvMain.loadUrl("http://dcoin.sjxfc.top/");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (true) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                startActivity(intent);
            }
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wvMain.canGoBack()) {
                wvMain.goBack();//返回上一浏览页面
                return true;
            } else {
                finish();//关闭Activity
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

