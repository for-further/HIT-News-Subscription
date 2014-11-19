package com.zf.hit_news;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class Home extends ActionBarActivity {
	private RelativeLayout HometoNews,HometoSearch,HometosetKeyword,HometosetTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	 	setContentView(R.layout.activity_main);
		HometoNews=(RelativeLayout)findViewById(R.id.HometoNews);
		HometoSearch=(RelativeLayout)findViewById(R.id.HometoSearch);
		HometosetKeyword=(RelativeLayout)findViewById(R.id.HometosetKeyWord);
		HometosetTime=(RelativeLayout)findViewById(R.id.HometosetTime);
		HomeClicklistener Hlistener=new HomeClicklistener();
		HometoNews.setOnClickListener(Hlistener);
		HometoSearch.setOnClickListener(Hlistener);
		HometosetKeyword.setOnClickListener(Hlistener);
		HometosetTime.setOnClickListener(Hlistener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	class HomeClicklistener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.HometoNews) {
				Intent intent = new Intent(Home.this,News.class);
				startActivity(intent);
				System.out.println("News");
			}
			else if(v.getId()==R.id.HometoSearch) {
				Intent intent = new Intent(Home.this,Search.class);
				startActivity(intent);
				System.out.println("Search");
			}
			else if(v.getId()==R.id.HometosetKeyWord) {
				Intent intent = new Intent(Home.this,keyword.class);
				startActivity(intent);
				System.out.println("¹Ø¼ü×Ö");
			}
			else if(v.getId()==R.id.HometosetTime) {
				Intent intent = new Intent(Home.this,Time.class);
				startActivity(intent);
				System.out.println("Time");
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
