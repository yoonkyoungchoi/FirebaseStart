package kr.hs.emirim.choi.firebaseStart.Hosting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kr.hs.emirim.choi.firebaseStart.R;

public class HostingActivity extends AppCompatActivity {

    private WebView mWebView = null;

    public class WebCustonClient extends WebViewClient{
        public boolean shouldOverrideUrlLodeing(WebView webView, WebResourceRequest resourceRequest){
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosting);

        mWebView = (WebView)findViewById(R.id.mywebview);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://fir-start-90438.web.app/");
    }
}