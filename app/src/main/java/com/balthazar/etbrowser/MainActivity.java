package com.balthazar.etbrowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText urlInput;
    ImageView cancelUrl;
    WebView webview;
    ProgressBar progressBar;
    ImageView webBack,webForward,webRefresh,webShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlInput = findViewById(R.id.url_input);
        cancelUrl = findViewById(R.id.cancel_icon);
        webview = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.progress_bar);

        webBack = findViewById(R.id.back);
        webForward = findViewById(R.id.forward);
        webRefresh = findViewById(R.id.refresh);
        webShare = findViewById( R.id.share);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webview.setWebViewClient(new  MyWebViewClient());
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });

        loadMyUrl("google.com");

        urlInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int i, KeyEvent event) {
                if (i == EditorInfo.IME_ACTION_GO || i == EditorInfo.IME_ACTION_DONE){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(urlInput.getWindowToken(), 0);
                    loadMyUrl(urlInput.getText().toString());
                    return true;
                }
                return false;
            }
        });

        cancelUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlInput.setText("");
            }
        });


        webBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview.canGoBack()){
                    webview.goBack();
                }
            }
        });

        webForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview.canGoForward()){
                    webview.goForward();
                }
            }
        });

        webRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.reload();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        webShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,webview.getUrl());
                intent.setType("text/plain");
                startActivity(intent);
            }
        });
    }

    void loadMyUrl(String url){
        boolean matchUrl = Patterns.WEB_URL.matcher(url).matches();
        if(matchUrl){
            webview.loadUrl(url);
        }else {
            webview.loadUrl("google.com/seatch?q="+url);
        }
    }

    @Override
    public void onBackPressed() {

        if (webview.canGoBack()){
            webview.goBack();
        }else{
            super.onBackPressed();
        }

    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            urlInput.setText(webview.getUrl());
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


}