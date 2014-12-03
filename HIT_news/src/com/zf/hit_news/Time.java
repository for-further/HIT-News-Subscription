package com.zf.hit_news;

import cn.jpush.android.api.JPushInterface;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class Time extends ActionBarActivity {
	private Button Timesub,timeswitch;
	private TimePicker Timepicker;
	private ImageView timetoHome;
	SharedPreferences mShared = null;
	public final static String SHARED_time = "time";
	public final static String KEY_time = "time";
	public final static String DATA_URL = "/data/datatime/";
	public final static String SHARED_time_XML = "time.xml";
	String timeon="1";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time);
		Timesub=(Button)findViewById(R.id.Timesub);
		timeswitch=(Button)findViewById(R.id.Timeswitch);
		Timepicker=(TimePicker)findViewById(R.id.Timepicker);
		timetoHome=(ImageView)findViewById(R.id.timetoHome);
		System.out.println("1"+timeon);
		mShared = getSharedPreferences(SHARED_time, Context.MODE_PRIVATE);
		timeon = mShared.getString(KEY_time,"1");
		if(timeon.equals("1")){
			timeswitch.setText("定时推送          开");
			}
			else if(timeon.equals("0")){
				timeswitch.setText("定时推送          关");
			}
		System.out.println("2"+timeon);
		ButtonListener listener=new ButtonListener();
		timetoHome.setOnClickListener(listener);
		Timesub.setOnClickListener(listener);
		timeswitch.setOnClickListener(listener);
		TimeListener timelistener=new TimeListener();
		Timepicker.setOnTimeChangedListener(timelistener);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.Timesub){
				int hour = Timepicker.getCurrentHour();
				int minute = Timepicker.getCurrentMinute();
				System.out.println("h:" + hour + ",minute:" + minute);
				Toast.makeText(getApplicationContext(), "完成", Toast.LENGTH_SHORT).show();
			}
			else if(v.getId()==R.id.timetoHome){
				finish();
			}
			else if(v.getId()==R.id.Timeswitch){
				System.out.println("3"+timeon);
				if(timeon.equals("1")){
				timeswitch.setText("定时推送          关");
				System.out.println("定时推送关");
				JPushInterface.stopPush(getApplicationContext());
				timeon = "0";
				}
				else if(timeon.equals("0")){
					timeswitch.setText("定时推送          开");
					System.out.println("定时推送开");
					JPushInterface.resumePush(getApplicationContext());
					timeon="1";
				}
				mShared = getSharedPreferences(SHARED_time, Context.MODE_PRIVATE);
				/**开始保存入SharedPreferences**/
				Editor editor = mShared.edit();
				editor.putString(KEY_time,timeon);
				/**put完毕必需要commit()否则无法保存**/
				editor.commit();
			}
				
		}
		
	}
	class TimeListener implements OnTimeChangedListener{
		@Override
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//			System.out.println("Hour:" + hourOfDay + ",minute:" + minute);
		}
		
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
