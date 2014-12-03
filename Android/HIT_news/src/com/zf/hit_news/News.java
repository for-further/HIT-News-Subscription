package com.zf.hit_news;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;
//import android.widget.Button;

public class News  extends Activity implements SwipeRefreshLayout.OnRefreshListener {
	private ImageView newstoHome,refresh;
	private SwipeRefreshLayout mSwipeLayout;
	ListView mListView = null;
	View mView = null;
	public String URL=null,URLO="", lastUrl = "new",firstUrl="new";
	
	SharedPreferences mShared = null;
	public final static String SHARED_keyword = "url";
	public final static String DATA_URL = "/data/dataurl/";
	public final static String SHARED_keyword_XML = "news.xml";
	public final static String KEY_news = "";
	public final static String KEY_last = "new";
	public final static String KEY_first ="new";
	
//	URL = "http://today.hit.edu.cn$工大新闻$http://img03.store.sogou.com/net/a/04/link?&url=http%3A%2F%2Ftoday.hit.edu.cn%2Fuploadfiles%2F2014%2F11-7%2F201411715181.jpg&appid=100520124&referer=http://today.hit.edu.cn/news/2014/11-07/9174815111RL0.htm$http://www.baidu.com$百度$http://www.baidu.com/img/bdlogo.png$";
//	public final String Url = "http://img03.store.sogou.com/net/a/04/link?&url=http%3A%2F%2Ftoday.hit.edu.cn%2Fuploadfiles%2F2014%2F11-7%2F201411715181.jpg&appid=100520124&referer=http://today.hit.edu.cn/news/2014/11-07/9174815111RL0.htm";
	
	
	List<HashMap<String,Object>> mListData = null, mList = null;
	 Handler handler=new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            if (msg.what==0x9527) {
	                //显示从网上下载的图片
	            	System.out.println("mess");
	            	Do(0);
	            	Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
	            }else if(msg.what==0x9526){
	            	System.out.println("mess");
	            	Do(1);
	            	
	            }else if(msg.what==0x9528){
	            	Toast.makeText(getApplicationContext(), "刷新失败\n请检查网络", Toast.LENGTH_SHORT).show();
	            }
	            else if(msg.what==0x9529)
	            	Toast.makeText(getApplicationContext(), "没有更新", Toast.LENGTH_SHORT).show();
	        }          
	    };
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);
		mView = LayoutInflater.from(this).inflate(R.layout.list_item, null); 
		mListView = (ListView) findViewById(R.id.mList);  
		newstoHome=(ImageView)findViewById(R.id.newstoHome);
		refresh = (ImageView) findViewById(R.id.refresh);
		
		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		mSwipeLayout.setOnRefreshListener((OnRefreshListener) this);
		mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
		android.R.color.holo_green_light, android.R.color.holo_orange_light,
		android.R.color.holo_red_light);
		
		mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
		lastUrl = mShared.getString(KEY_last,"new");
		URLO=mShared.getString(KEY_news, "");
		firstUrl=mShared.getString(KEY_first, "new");
		new Thread(){
        	public void run() {
    				URL=URLO;
    				if(URL == null)
        				handler.sendEmptyMessage(0x9528);
        			else if(URL.equals(""))
        				handler.sendEmptyMessage(0x9529);
        			else{
        				mList = getListData();
        				handler.sendEmptyMessage(0x9527);		
        			}
        	}
        }.start();
        Toast.makeText(getApplicationContext(), "...Wait\n。。。。", Toast.LENGTH_SHORT).show();
		
		Newslistener listener=new Newslistener();	
		newstoHome.setOnClickListener(listener);
		refresh.setOnClickListener(listener);
		
	
	}
	
	public void onRefresh() {
		new Thread(){
        	public void run() {
        		String id = MainActivity.getRId();
        			URL = httpRequest.getNewsNewer("new", id);
        			URLO = URL;
        			System.out.println(URL);
        			if(URL == null)
        				handler.sendEmptyMessage(0x9528);
        			else if(URL == "")
        				handler.sendEmptyMessage(0x9529);
        			else{
        				mList = getListData();
        				handler.sendEmptyMessage(0x9527);		
        			}
        	}
        }.start();
        mSwipeLayout.setRefreshing(false);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	class Newslistener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.newstoHome){
				
				
				int cnt = 0, flag = 1;
				URL = firstUrl = lastUrl = "";
				for(int i=0; i<URLO.length(); i++){
					char ch = URLO.charAt(i);
					URL += ch;
					if(ch == '$'){
						cnt++;
						continue;
					}
					if(cnt == 30) break;
					else if(cnt == 0) firstUrl += ch;
					if(cnt % 3 == 0) {
						if(flag == 0)
							lastUrl += ch;
						else{
							flag = 0;
							lastUrl = "" + ch;
						}
					}else if(cnt % 3 == 1) flag = 1;
				}
				
				System.out.println(firstUrl + " " + lastUrl);
				
				mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
				Editor editor = mShared.edit();
				editor.putString(KEY_news, URL);
				/**put完毕必需要commit()否则无法保存**/
				editor.commit();
				System.out.println("*****保存URL成功********"+URL);
				
				editor.putString(KEY_last, lastUrl);
				editor.commit();
				/**put完毕必需要commit()否则无法保存**/
				System.out.println("保存last------"+lastUrl);
				
				editor.putString(KEY_first, firstUrl);
				/**put完毕必需要commit()否则无法保存**/
				editor.commit();
				System.out.println("保存first------"+firstUrl); 
//				Intent intent = new Intent(News.this,Home.class);
//				startActivity(intent);
				finish();
			}
			else if(v.getId()==R.id.refresh){
					new Thread(){
			        	public void run() {
			        		String id = MainActivity.getRId();
			        			URL = httpRequest.getNewsOlder(lastUrl, id);
			        			URLO=URL+URLO;
			        			if(URL == null)
			        				handler.sendEmptyMessage(0x9528);
			        			else if(URL == "")
			        				handler.sendEmptyMessage(0x9529);
			        			else{
			        				mList = getListData();
			        				handler.sendEmptyMessage(0x9526);		
			        			}
			        	}
			        }.start();
			        Toast.makeText(getApplicationContext(), "Wait\n。。。。", Toast.LENGTH_SHORT).show();
				}
		}
			
		
	}
	public void Do(int sta){
//		System.out.println("DO");
//		mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
//		Editor editor = mShared.edit();
//		editor.putString(KEY_last, lastUrl);
//		/**put完毕必需要commit()否则无法保存**/
//		editor.commit();
//		System.out.println("保存last成功"+lastUrl);

		if(sta != 0)
		for(HashMap<String,Object> mp: mList){
			mListData.add(mp);
		}else mListData = mList;
		SimpleAdapter adapter = new SimpleAdapter(this, mListData, R.layout.list_item,  
                new String[]{"image", "text", "url"}, new int[]{R.id.image, R.id.text, R.id.url});
        
        adapter.setViewBinder(new ViewBinder() {  
            
            public boolean setViewValue(View view, Object data,  
                    String textRepresentation) {  
                //判断是否为我们要处理的对象  
                if(view instanceof ImageView  && data instanceof Bitmap){  
                    ImageView iv = (ImageView) view;  
                  
                    iv.setImageBitmap((Bitmap) data);  
                    return true;  
                }else  
                return false;  
            }  
        });  
        //if(sta == 0) 
        mListView.setAdapter(adapter);
        //else adapter.notifyDataSetChanged();
	} 
	public List<HashMap<String,Object>> getListData(){  
        List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();  
        HashMap<String,Object> map = null;
        int cnt = 0;
        String title = "", murl = "", Url = "";
        firstUrl = "";
        for(int i=0;i < URL.length();i++){ 
        	//System.out.println(URL.charAt(i));
        	if(cnt == 0){
        		map = new HashMap<String, Object>(); 
        		if(URL.charAt(i) == '$'){
        			map.put("url", Url);
        			lastUrl = Url;
        			if (firstUrl.equals("")) firstUrl = Url;
//        			System.out.println(Url);
        			Url = "";
        			cnt++;
        		}else{
        			Url += URL.charAt(i);
        		}
        	}else if(cnt == 1){
        		if(URL.charAt(i) == '$'){
        			map.put("text", title);
        			System.out.println(title);
        			title = "";
        			cnt++;
        		}else{
        			title += URL.charAt(i);
        		}
        	}else{
        		if(URL.charAt(i) == '$'){
        			System.out.println(murl);
        			map.put("image", getBitmap(murl));
        			
        			murl = "";
        			cnt = 0;
        			list.add(map); 
        		}else{
        			murl += URL.charAt(i);
        		}
        	}
        }  
        System.out.println("zbfisrt : " + firstUrl);
        System.out.println("zblast : " + lastUrl);
        return list;  
    }  
	
	public Bitmap getBitmap(String mUrl){  
		if(mUrl == "NONE") return null;
        Bitmap mBitmap = null, resizeBmp = null;  
        try {  
            URL url = new URL(mUrl);  
            InputStream is = url.openStream();
            mBitmap = BitmapFactory.decodeStream(is);
            Matrix matrix = new Matrix();
            matrix.postScale(2, 2);
            resizeBmp = Bitmap.createBitmap(mBitmap,0,0,mBitmap.getWidth(), 
                    mBitmap.getHeight(),matrix,true);
              
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
            return null;
        } catch (IOException e) {  
            e.printStackTrace(); 
            return null;
        }  
          
        return resizeBmp;  
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


