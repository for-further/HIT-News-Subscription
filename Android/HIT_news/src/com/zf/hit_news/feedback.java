package com.zf.hit_news;

import com.zf.hit_news.Search.Listener;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class feedback extends ActionBarActivity {
	private ImageView feedbacktoHome;
	private Button feedbacksub;
	public EditText et;
//	private Boolean isEmpty = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		feedbacktoHome=(ImageView)findViewById(R.id.feedbacktoHome);
		feedbacksub=(Button)findViewById(R.id.feedbacksub);
		et = (EditText)this.findViewById(R.id.feedbacktext);
		Listener listener=new Listener();
		feedbacktoHome.setOnClickListener(listener);
		feedbacksub.setOnClickListener(listener);
	}
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
				String text=et.getText().toString().trim();
				System.out.println(text);
			}
		}
		
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
