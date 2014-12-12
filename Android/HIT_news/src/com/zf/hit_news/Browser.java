package com.zf.hit_news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

public class Browser extends Activity{
	private WebView webview;
	private String url = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browser);
		
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
	    System.out.println(url);
		
		//ʵ����WebView����  
        webview = (WebView)findViewById(R.id.browser);  
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setDefaultFontSize(18);
        webview.loadUrl(url);
	}
	
	@Override 
    //���û���  
    //����Activity���onKeyDown(int keyCoder,KeyEvent event)����  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        finish();
        return true;
	}
}
