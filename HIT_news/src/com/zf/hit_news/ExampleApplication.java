package com.zf.hit_news;

import android.app.Application;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * For developer startup JPush SDK
 * 
 * һ�㽨�����Զ��� Application �����ʼ����Ҳ�������� Activity �
 */
public class ExampleApplication extends Application {
    private static final String TAG = "JPush";

    @Override
    public void onCreate() {    	     
    	 Log.d(TAG, "[ExampleApplication] onCreate");
         super.onCreate();
//        System.out.println("Example");
         JPushInterface.setDebugMode(true); 	// ���ÿ�����־,����ʱ��ر���־
         JPushInterface.init(this);     		// ��ʼ�� JPush
    }
}