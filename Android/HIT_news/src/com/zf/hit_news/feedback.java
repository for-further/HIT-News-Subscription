package com.zf.hit_news;

import com.zf.hit_news.Search.Listener;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class feedback extends ActionBarActivity {
	private ImageView feedbacktoHome;
	private Button feedbacksub;
	public EditText et;
//	private Boolean isEmpty = true;

	Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if (msg.what == 0x1000){
        		Toast.makeText(getApplicationContext(), "提交成功，感谢您的支持！", Toast.LENGTH_SHORT).show();
				finish();
			}else if (msg.what == 0x1001){
				Toast.makeText(getApplicationContext(), "提交失败，请检查网络连接。", Toast.LENGTH_SHORT).show();
			}
        }
    };
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		
		setContentView(R.layout.feedback);
		feedbacktoHome=(ImageView)findViewById(R.id.feedbacktoHome);
		feedbacksub=(Button)findViewById(R.id.feedbacksub);
		et = (EditText)this.findViewById(R.id.feedbacktext);
		Listener listener=new Listener();
		feedbacktoHome.setOnClickListener(listener);
		feedbacksub.setOnClickListener(listener);
	}
	String TEXT, ID; int OK, END;
	class Listener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.feedbacktoHome)
			{
				Intent intent = new Intent(feedback.this,Home.class);
				startActivity(intent);
			}
			else if(v.getId()==R.id.feedbacksub){
				new Thread(){
					public void run(){
						String text=et.getText().toString().trim();
						String id = MainActivity.getRId();
						if (httpRequest.sendFeedback(text, id) == 1){
							handler.sendEmptyMessage(0x1000);
						}else 
							handler.sendEmptyMessage(0x1001);
					}
				}.start();
			}
		}
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		if(keyCode == KeyEvent.KEYCODE_BACK){                             
			finish();
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
