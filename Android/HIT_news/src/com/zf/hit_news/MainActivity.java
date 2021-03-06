package com.zf.hit_news;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.jpush.android.api.JPushInterface;



public class MainActivity extends Activity {
	private LinearLayout welcome;
	ListView mListView = null;
	View mView = null;
	private static String id = "";
	
	private EditText msgText;
	public static boolean isForeground = false;
	
	Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 ExitApplication.getInstance().addActivity(this);
				
//		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
		setContentView(R.layout.welcome);
        welcome=(LinearLayout)findViewById(R.id.welcome);
//		Listener listener=new Listener();
//		welcome.setOnClickListener(listener);
		id=JPushInterface.getRegistrationID(MainActivity.this);
		
		System.out.println("id   " + id);
		
		
		
		mHandler.sendEmptyMessageDelayed(0, 1500);
	}
	
	
	
	public static String getRId(){
		return id;
//		TextView mRid = (TextView) findViewById(R.id.textView1);
//		mRid.setText("Rid: " + id);
//		System.out.println("!!!" + id);
		
		
//		msgText = (EditText)findViewById(R.id.msg);
	}
	// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
	private void init(){
		 JPushInterface.init(getApplicationContext());
	}
	@Override
	/*
	 * (non-Javadoc)
	 * @see cn.jpush.android.api.InstrumentedActivity#onResume()
	 * receiver used
	 */
	protected void onResume() {
		isForeground = true;
		super.onResume();
	}
	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}
	//for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.zf.hit_news.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}
	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
              String messge = intent.getStringExtra(KEY_MESSAGE);
              String extras = intent.getStringExtra(KEY_EXTRAS);
              StringBuilder showMsg = new StringBuilder();
              showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
              if (!Unit.isEmpty(extras)) {
            	  showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
              }
              setCostomMsg(showMsg.toString());
			}
		}
	}
	private void setCostomMsg(String msg){
		 if (null != msgText) {
			 msgText.setText(msg);
			 msgText.setVisibility(android.view.View.VISIBLE);
         }
	}
	
//	class Listener implements OnClickListener{
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			if(v.getId()==R.id.welcome)
//			{
//				Intent intent = new Intent(MainActivity.this,Home.class);
//				startActivity(intent);
//			}
//		}
//		
//	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		if(keyCode == KeyEvent.KEYCODE_BACK){                             
			finish();
            System.exit(0); 
	    }  
	    return false;  
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}