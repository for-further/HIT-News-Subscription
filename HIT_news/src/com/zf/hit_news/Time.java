package com.zf.hit_news;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class Time extends ActionBarActivity {
	private Button Timesub;
	private TimePicker Timepicker;
	private ImageView timetoHome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time);
		Timesub=(Button)findViewById(R.id.Timesub);
		Timepicker=(TimePicker)findViewById(R.id.Timepicker);
		timetoHome=(ImageView)findViewById(R.id.timetoHome);
		ButtonListener listener=new ButtonListener();
		timetoHome.setOnClickListener(listener);
		Timesub.setOnClickListener(listener);
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
			}
			else if(v.getId()==R.id.timetoHome){
				Intent intent = new Intent(Time.this,Home.class);
				startActivity(intent);
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
