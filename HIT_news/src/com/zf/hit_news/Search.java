package com.zf.hit_news;

import com.zf.hit_news.keyword.Listener;

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

public class Search extends ActionBarActivity {

	private ImageView searchtoHome;
	public EditText et;
	private Button searchsub;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		searchtoHome=(ImageView)findViewById(R.id.searchtoHome);
		et=(EditText)findViewById(R.id.searchtext);
		searchsub=(Button)findViewById(R.id.searchsub);
		Listener listener=new Listener();
		searchtoHome.setOnClickListener(listener);
		searchsub.setOnClickListener(listener);
	}
	class Listener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.searchtoHome)
			{
//				Intent intent = new Intent(Search.this,Home.class);
//				startActivity(intent);
				finish();
			}
			else if(v.getId()==R.id.searchsub){
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
