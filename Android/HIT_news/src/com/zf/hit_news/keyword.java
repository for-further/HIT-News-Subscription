package com.zf.hit_news;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.LiveFolders;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class keyword extends ActionBarActivity {
	
	private ImageView keywordtoHome;
	private Button keywordsub,keyworddel,keywordclean;
	public EditText et;
	private TextView add;
	SharedPreferences mShared = null;
	public final static String SHARED_keyword = "keyword";
	public final static String KEY_keyword = "keyword";
	public final static String DATA_URL = "/data/data/";
	public final static String SHARED_keyword_XML = "keyword.xml";
	
	Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if (msg.what == 0x1000){
        		Toast.makeText(getApplicationContext(), "��ӳɹ�", Toast.LENGTH_SHORT).show();
        	}else if (msg.what == 0x1001){
        		Toast.makeText(getApplicationContext(), "���ʧ��", Toast.LENGTH_SHORT).show();
        	}else if (msg.what == 0x1002){
        		Toast.makeText(getApplicationContext(), "������ؼ���", Toast.LENGTH_SHORT).show();
        	}else if (msg.what == 0x1003){
        		Toast.makeText(getApplicationContext(), "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
        	}else if (msg.what == 0x1004){
        		Toast.makeText(getApplicationContext(), "ɾ��ʧ��", Toast.LENGTH_SHORT).show();	
        	}else if (msg.what == 0x1005){
        		Toast.makeText(getApplicationContext(), "��ճɹ�", Toast.LENGTH_SHORT).show();
        	}else if (msg.what == 0x1006){
        		Toast.makeText(getApplicationContext(), "���ʧ��", Toast.LENGTH_SHORT).show();
        	}else if (msg.what == 0x1007){
        		Toast.makeText(getApplicationContext(), "�����", Toast.LENGTH_SHORT).show();
        	}else if (msg.what == 0x1008){
        		String text = mShared.getString(KEY_keyword, "��ʱû������");
        		add.setText(text);
        	}
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		
		setContentView(R.layout.keyword);
		keywordtoHome=(ImageView)findViewById(R.id.keywordtoHome);
		keywordsub=(Button)findViewById(R.id.keywordsub);
		keyworddel=(Button)findViewById(R.id.keyworddel);
		keywordclean=(Button)findViewById(R.id.keywordclean);
		et=(EditText)findViewById(R.id.keywordtext);
		add=(TextView)findViewById(R.id.add);
		Listener listener=new Listener();
		keywordtoHome.setOnClickListener(listener);
		keywordsub.setOnClickListener(listener);
		keyworddel.setOnClickListener(listener);
		keywordclean.setOnClickListener(listener);
		
		mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
		String text = mShared.getString(KEY_keyword,"��ʱû������");
        add.setText(text);
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
			if(v.getId()==R.id.keywordtoHome)
			{
//				Intent intent = new Intent(keyword.this,Home.class);
//				startActivity(intent);
				finish();
			}
			else if(v.getId()==R.id.keywordsub){
				//Add Keyword
        		new Thread(){
        			public void run(){
        				mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
        				String textbefore = mShared.getString(KEY_keyword, "��ʱû������");
        				String text=et.getText().toString().trim();
        				if(text.length()!=0){
        					String id = MainActivity.getRId();
        					if (httpRequest.addKeyword(text, id) == 1){
        						System.out.println("�¼���ؼ��֣�"+text);
        						/**��ʼ������SharedPreferences**/
        						Editor editor = mShared.edit();
        						if(textbefore=="��ʱû������")editor.putString(KEY_keyword, text+"\n");
        						else editor.putString(KEY_keyword, textbefore+text+"\n");
        						/**put��ϱ���Ҫcommit()�����޷�����**/
        						editor.commit();
        						handler.sendEmptyMessage(0x1000);
        					}else
        						handler.sendEmptyMessage(0x1001);
        				}else handler.sendEmptyMessage(0x1002);
						handler.sendEmptyMessage(0x1008);
        			}
        		}.start();
			}else if(v.getId()==R.id.keyworddel){
				//Delete Keyword
        		new Thread(){
        			public void run(){
		        		mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
						String textbefore = mShared.getString(KEY_keyword, "��ʱû������");
						//System.out.println("A"+textbefore);
						String text=et.getText().toString().trim();
						if(text.length()!=0){
							String id = MainActivity.getRId();
							if(httpRequest.delKeyword(text, id) == 1){
								System.out.println("ɾ���Ĺؼ���"+text);
								/**��ʼ������SharedPreferences**/
								textbefore=textbefore.replace(text+"\n", "");
								//textbefore=textbefore.replace(text, "");
								System.out.println("B"+textbefore+"C");
								Editor editor = mShared.edit();
								editor.putString(KEY_keyword, textbefore);
								/**put��ϱ���Ҫcommit()�����޷�����**/
								editor.commit();
								handler.sendEmptyMessage(0x1003);
							}else handler.sendEmptyMessage(0x1004);	
						}else handler.sendEmptyMessage(0x1002);
						handler.sendEmptyMessage(0x1008);
						//text = mShared.getString(KEY_keyword, "��ʱû������");
		                //if(textbefore.length()==0) text="��ʱû������"; 
		                //add.setText(text);
	        		}
        		}.start();
			}else if(v.getId()==R.id.keywordclean){
				//Clear All
        		System.out.println("123");
        		new Thread(){
        			public void run(){

                		System.out.println("456");
        				mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
                        /**��ʼ���SharedPreferences�б��������**/
        				String text = mShared.getString(KEY_keyword, "��ʱû������");
        				System.out.println(text);
        				String id = MainActivity.getRId();

                    	System.out.println("789");
        				if (httpRequest.sendClearRequest(id) == 1){
        					handler.sendEmptyMessage(0x1005);
        					Editor editor = mShared.edit();
            				editor.remove(KEY_keyword);
            				//editor.clear();
            				editor.commit();
        				} else 	handler.sendEmptyMessage(0x1006);
//                       ShowDialog("���SharedPreferences���ݳɹ�");
        				System.out.println("10JQ");
        				handler.sendEmptyMessage(0x1008);	
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
