package com.zf.hit_news;

import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
        Bundle bundle = getIntent().getExtras();
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        intent.setAction("android.intent.action.VIEW");
        
        String URL = "";
        Boolean flag = false;
        for(int i=0; i<content.length(); i++){
        	if(flag){
        		URL += content.charAt(i);
        	}else{
        		if(content.charAt(i) == '$') flag = true;
        	}
        }
        Uri content_url = Uri.parse(URL);
        intent.setData(content_url);
        startActivity(intent);
    }
}

