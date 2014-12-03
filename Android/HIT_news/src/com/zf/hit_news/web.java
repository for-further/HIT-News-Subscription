package com.zf.hit_news;

//import java.nio.Buffer;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class web extends ActionBarActivity {
	private ImageView webtoHome;
	private CheckBox web1,web2,web3,web4,web5,web6,web7,web8,web9,web10,web11,all,allnot;
	private Button websub;
	int i=0;
	public String web,text="0000000000001";
	char[] textr;
	SharedPreferences mShared = null;
	public final static String SHARED_keyword = "web";
	public final static String KEY_keyword = "web";
	public final static String DATA_URL = "/data/dataweb/";
	public final static String SHARED_keyword_XML = "web.xml";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);
		webtoHome=(ImageView)findViewById(R.id.webtoHome);
		all=(CheckBox)findViewById(R.id.all);
		allnot=(CheckBox)findViewById(R.id.allnot);
		web1=(CheckBox)findViewById(R.id.web1);
		web2=(CheckBox)findViewById(R.id.web2);
		web3=(CheckBox)findViewById(R.id.web3);
		web4=(CheckBox)findViewById(R.id.web4);
		web5=(CheckBox)findViewById(R.id.web5);
		web6=(CheckBox)findViewById(R.id.web6);
		web7=(CheckBox)findViewById(R.id.web7);
		web8=(CheckBox)findViewById(R.id.web8);
		web9=(CheckBox)findViewById(R.id.web9);
		web10=(CheckBox)findViewById(R.id.web10);
		web11=(CheckBox)findViewById(R.id.web11);
		websub=(Button)findViewById(R.id.websub);
		
		Listener listener=new Listener();
		webtoHome.setOnClickListener(listener);
		websub.setOnClickListener(listener);
		webListener weblistener = new webListener();
		all.setOnCheckedChangeListener(weblistener);
		allnot.setOnCheckedChangeListener(weblistener);
		web1.setOnCheckedChangeListener(weblistener);
		web2.setOnCheckedChangeListener(weblistener);
		web3.setOnCheckedChangeListener(weblistener);
		web4.setOnCheckedChangeListener(weblistener);
		web5.setOnCheckedChangeListener(weblistener);
		web6.setOnCheckedChangeListener(weblistener);
		web7.setOnCheckedChangeListener(weblistener);
		web8.setOnCheckedChangeListener(weblistener);
		web9.setOnCheckedChangeListener(weblistener);
		web10.setOnCheckedChangeListener(weblistener);
		web11.setOnCheckedChangeListener(weblistener);
		
		mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
		text = mShared.getString(KEY_keyword,"0000000000001");
		if(text.charAt(0)=='1') all.setChecked(true);
		else if(text.charAt(0)=='0') all.setChecked(false);
		if(text.charAt(1)=='1') web1.setChecked(true);
		else if(text.charAt(1)=='0') web1.setChecked(false);
		if(text.charAt(2)=='1') web2.setChecked(true);
		else if(text.charAt(2)=='0') web2.setChecked(false);
		if(text.charAt(3)=='1') web3.setChecked(true);
		else if(text.charAt(3)=='0') web3.setChecked(false);
		if(text.charAt(4)=='1') web4.setChecked(true);
		else if(text.charAt(4)=='0') web4.setChecked(false);
		if(text.charAt(5)=='1') web5.setChecked(true);
		else if(text.charAt(5)=='0') web5.setChecked(false);
		if(text.charAt(6)=='1') web6.setChecked(true);
		else if(text.charAt(6)=='0') web6.setChecked(false);
		if(text.charAt(7)=='1') web7.setChecked(true);
		else if(text.charAt(7)=='0') web7.setChecked(false);
		if(text.charAt(8)=='1') web8.setChecked(true);
		else if(text.charAt(8)=='0') web8.setChecked(false);
		if(text.charAt(9)=='1') web9.setChecked(true);
		else if(text.charAt(9)=='0') web9.setChecked(false);
		if(text.charAt(10)=='1') web10.setChecked(true);
		else if(text.charAt(10)=='0') web10.setChecked(false);
		if(text.charAt(11)=='1') web11.setChecked(true);
		else if(text.charAt(11)=='0') web11.setChecked(false);
		if(text.charAt(12)=='1') allnot.setChecked(true);
		else if(text.charAt(12)=='0') allnot.setChecked(false);
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	String WEBSITE, ID; int END, OK;
	class Listener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.webtoHome)
			{
//				Intent intent = new Intent(web.this,Home.class);
//				startActivity(intent);
				finish();
			}
			else if(v.getId()==R.id.websub){
				String website="";
				if(text.charAt(0)=='1') 
					website="http://today.hit.edu.cn$"
							+ "http://news.hit.edu.cn$"
							+ "http://acm.hit.edu.cn$"
							+ "http://hqjt.hit.edu.cn$"
							+ "http://hituc.hit.edu.cn$"
							+ "http://jwc.hit.edu.cn$"
							+ "http://studyathit.hit.edu.cn$"
							+ "http://yzb.hit.edu.cn$"
							+ "http://ygb.hit.edu.cn$"
							+ "http://www.usp.com.cn$"
							+ "http://zsb.hit.edu.cn$";
				else if (text.charAt(12)=='1') website="";
				else if(text.charAt(0)=='0'){ 
					if(text.charAt(1)=='1') website +="http://today.hit.edu.cn$";
					if(text.charAt(2)=='1') website +="http://news.hit.edu.cn$";
					if(text.charAt(3)=='1') website +="http://acm.hit.edu.cn$";
					if(text.charAt(4)=='1') website +="http://hqjt.hit.edu.cn$";
					if(text.charAt(5)=='1') website +="http://hituc.hit.edu.cn$";
					if(text.charAt(6)=='1') website +="http://jwc.hit.edu.cn$";
					if(text.charAt(7)=='1') website +="http://studyathit.hit.edu.cn$";
					if(text.charAt(8)=='1') website +="http://yzb.hit.edu.cn$";
					if(text.charAt(9)=='1') website +="http://ygb.hit.edu.cn$";
					if(text.charAt(10)=='1') website +="http://ygb.hit.edu.cn$";
					if(text.charAt(11)=='1') website +="http://zsb.hit.edu.cn$";
				}
				/*zheng 输入website*/		
				String id = MainActivity.getRId();
				WEBSITE = website; ID = id; OK = 0; END = 0;
				new Thread(){
					public void run(){
						OK = httpRequest.sendUrl(WEBSITE, ID);
						END = 1;
					}
				}.start();
				while (END == 0);
				if(OK == 1){
//				mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
//				text = mShared.getString(KEY_keyword,"0000000000001");
				System.out.println("完成");
				System.out.println(text);
				/**开始保存入SharedPreferences**/
				Editor editor = mShared.edit();
				editor.putString(KEY_keyword, text);
				/**put完毕必需要commit()否则无法保存**/
				editor.commit();
				Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
	/*zheng*/		}else{
				Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
		
	}
	class webListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
			// TODO Auto-generated method stub
			if(buttonView.getId()==R.id.web1)      i=1;
			else if(buttonView.getId()==R.id.web2) i=2;
			else if(buttonView.getId()==R.id.web3) i=3;
			else if(buttonView.getId()==R.id.web4) i=4;
			else if(buttonView.getId()==R.id.web5) i=5;
			else if(buttonView.getId()==R.id.web6) i=6;
			else if(buttonView.getId()==R.id.web7) i=7;
			else if(buttonView.getId()==R.id.web8) i=8;		
			else if(buttonView.getId()==R.id.web9) i=9;
			else if(buttonView.getId()==R.id.web10) i=10;
			else if(buttonView.getId()==R.id.web11) i=11;
			if(buttonView.getId()==R.id.all && isChecked) {
				web1.setChecked(true);
				web2.setChecked(true);
				web3.setChecked(true);
				web4.setChecked(true);
				web5.setChecked(true);
				web6.setChecked(true);
				web7.setChecked(true);
				web8.setChecked(true);
				web9.setChecked(true);
				web10.setChecked(true);
				web11.setChecked(true);
				allnot.setChecked(false);
				all.setChecked(true);
				text="1111111111110";
			}
			if(buttonView.getId()==R.id.allnot && isChecked){
				web1.setChecked(false);
				web2.setChecked(false);
				web3.setChecked(false);
				web4.setChecked(false);
				web5.setChecked(false);
				web6.setChecked(false);
				web7.setChecked(false);
				web8.setChecked(false);
				web9.setChecked(false);
				web10.setChecked(false);
				web11.setChecked(false);
				all.setChecked(false);
				text="0000000000001";
				System.out.println(text);
			}
			if(buttonView.getId()!=R.id.all && buttonView.getId()!=R.id.allnot && isChecked) {
				textr=text.toCharArray();
				textr[i]='1';
				textr[12]='0';
				text=String.valueOf(textr);
				allnot.setChecked(false);
			}
			else if(buttonView.getId()!=R.id.all && buttonView.getId()!=R.id.allnot &&  isChecked==false){
				textr=text.toCharArray();
				textr[i]='0';
				textr[0]='0';
				text=String.valueOf(textr);
				all.setChecked(false);
			}
			/**开始保存入SharedPreferences**/
			/*Editor editor = mShared.edit();
			editor.putString(KEY_keyword, text);
			editor.commit();*/
			/**put完毕必需要commit()否则无法保存**/
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
