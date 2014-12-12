package com.zf.hit_news;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.LiveFolders;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
				mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
				String textbefore = mShared.getString(KEY_keyword,"��ʱû������");
				String text=et.getText().toString().trim();
				if(text.length()!=0){
					String id = MainActivity.getRId();
					if(httpRequest.addKeyword(text,id )==1){
					System.out.println("�¼���ؼ��֣�"+text);
					/**��ʼ������SharedPreferences**/
					Editor editor = mShared.edit();
					if(textbefore=="��ʱû������")editor.putString(KEY_keyword, text+"\n");
					else editor.putString(KEY_keyword, textbefore+text+"\n");
					/**put��ϱ���Ҫcommit()�����޷�����**/
					editor.commit();
					Toast.makeText(getApplicationContext(), "��ӳɹ�", Toast.LENGTH_SHORT).show();
				}
					else{
					Toast.makeText(getApplicationContext(), "���ʧ��", Toast.LENGTH_SHORT).show();
				}
				}
				else
				Toast.makeText(getApplicationContext(), "������ؼ���", Toast.LENGTH_SHORT).show();
                text = mShared.getString(KEY_keyword, "��ʱû������");
                add.setText(text);
				
			}
			else if(v.getId()==R.id.keyworddel){
				mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
				String textbefore = mShared.getString(KEY_keyword, "��ʱû������");
				System.out.println("A"+textbefore);
				String text=et.getText().toString().trim();
				if(text.length()!=0){
				String id = MainActivity.getRId();
				if(httpRequest.delKeyword(text,  id)==1){
					System.out.println("ɾ���Ĺؼ���"+text);
					/**��ʼ������SharedPreferences**/
					textbefore=textbefore.replace(text+"\n", "");
//				textbefore=textbefore.replace(text, "");
					System.out.println("B"+textbefore+"C");
					Editor editor = mShared.edit();
					editor.putString(KEY_keyword, textbefore);
					/**put��ϱ���Ҫcommit()�����޷�����**/
					editor.commit();
					Toast.makeText(getApplicationContext(), "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getApplicationContext(), "ɾ��ʧ��", Toast.LENGTH_SHORT).show();
					}
				}
				else
				Toast.makeText(getApplicationContext(), "������ؼ���", Toast.LENGTH_SHORT).show();
                text = mShared.getString(KEY_keyword, "��ʱû������");
                if(textbefore.length()==0) text="��ʱû������"; 
                add.setText(text);
				
			}
			else if(v.getId()==R.id.keywordclean){
				mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
                /**��ʼ���SharedPreferences�б��������**/
				String text = mShared.getString(KEY_keyword, "��ʱû������");
				System.out.println(text);
				if(text!="��ʱû������"){
                Editor editor = mShared.edit();
                editor.remove(KEY_keyword);
                //editor.clear();
                editor.commit();
//                ShowDialog("���SharedPreferences���ݳɹ�");
				}
                System.out.println(text);
                Toast.makeText(getApplicationContext(), "�����", Toast.LENGTH_SHORT).show();
                text = mShared.getString(KEY_keyword, "��ʱû������");
                add.setText(text);
				
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
