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
	private String type = "";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		
		setContentView(R.layout.browser);
		
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
	    System.out.println(url);
	    type = intent.getStringExtra("source");
	    System.out.println(type);
	    
		
		//实例化WebView对象  
        webview = (WebView)findViewById(R.id.browser);  
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);//设置可缩放
        webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setDefaultFontSize(18);
        webview.loadUrl(url);
	}
	
	@Override 
    //设置回退  
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		if(keyCode == KeyEvent.KEYCODE_BACK){                             
			finish();
	    }  
	    return false;  
	}
}
