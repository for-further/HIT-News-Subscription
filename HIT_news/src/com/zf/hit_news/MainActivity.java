package com.zf.hit_news;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class MainActivity extends ActionBarActivity {
	private LinearLayout welcome;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		welcome=(LinearLayout)findViewById(R.id.welcome);
		Listener listener=new Listener();
		welcome.setOnClickListener(listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	class Listener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.welcome)
			{
				Intent intent = new Intent(MainActivity.this,Home.class);
				startActivity(intent);
			}
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
