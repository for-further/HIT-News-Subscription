package com.zf.hit_news;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

import com.whos.swiperefreshandload.view.SwipeRefreshLayout;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout.OnLoadListener;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout.OnRefreshListener;


@SuppressLint("ResourceAsColor")
public class News  extends Activity implements OnRefreshListener, OnLoadListener{
	private ImageView newstoHome;
	private SwipeRefreshLayout mSwipeLayout;
	ListView mListView = null;
	View mView = null;
	public String URL = null, URLO = "", lastUrl = "new", firstUrl = "new";
	
	SharedPreferences mShared = null;
	public final static String SHARED_keyword = "url";
	public final static String DATA_URL = "/data/dataurl/";
	public final static String SHARED_keyword_XML = "news.xml";
	public final static String KEY_news = "";
	public final static String KEY_last = "new";
	public final static String KEY_first ="new";
	
	List<HashMap<String,Object>> mListData = new ArrayList<HashMap<String,Object>>(), mList = new ArrayList<HashMap<String,Object>>();
	SimpleAdapter adapter = null;
	
	 Handler handler=new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            if (msg.what==0x9527) {
	                //显示从网上下载的图片
	            	System.out.println("mess");
	            	Do(0);
//	            	Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
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
		//初始化控件
		setContentView(R.layout.news);
		mView = LayoutInflater.from(this).inflate(R.layout.list_item, null); 
		mListView = (ListView) findViewById(R.id.mList);  
		newstoHome=(ImageView)findViewById(R.id.newstoHome);
		
		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
	    mSwipeLayout.setOnRefreshListener(this);
	    mSwipeLayout.setOnLoadListener(this);
	    mSwipeLayout.setColor(android.R.color.holo_blue_bright,
		                      android.R.color.holo_green_light,
		                      android.R.color.holo_orange_light,
		                      android.R.color.holo_red_light);
	    mSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
	    mSwipeLayout.setLoadNoFull(false);
		
	    //显示本地保存的新闻
		mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
		lastUrl = mShared.getString(KEY_last,"new");
		URLO=mShared.getString(KEY_news, "");
		firstUrl=mShared.getString(KEY_first, "new");
		URL = URLO;
//		System.out.println(URLO);
		new Thread(){
        	public void run() {
    			if(URLO == null)
        			handler.sendEmptyMessage(0x9528);
        		else if(URLO.equals(""))
        			handler.sendEmptyMessage(0x9529);
        		else{
        			mList = getListData();
        			handler.sendEmptyMessage(0x9527);		
        		}
        	}
        }.start();
        Toast.makeText(getApplicationContext(), "正在刷新", Toast.LENGTH_SHORT).show();
		
        //设置监听
		Newslistener listener=new Newslistener();	
		newstoHome.setOnClickListener(listener);
		//点击listview事件
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				HashMap<String, Object> map = (HashMap<String, Object>) News.this.adapter.getItem(position);
				String url = (String) map.get("url");
				Intent intent = new Intent(News.this, Browser.class);
				intent.putExtra("url", url);
		        startActivity(intent);
			}
		});
	}
	
	//下拉刷新，显示最新的10条新闻
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
        Toast.makeText(getApplicationContext(), "正在刷新", Toast.LENGTH_SHORT).show();
	}
	
	//上拉刷新，显示lasturl之前的10条新闻
	@Override
    public void onLoad() {
		new Thread(){
        	public void run() {
        		String id = MainActivity.getRId();
        			URL = httpRequest.getNewsOlder(lastUrl, id);
        			System.out.println("Older       \n" + URL);
        			URLO=URLO+URL;
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
        mSwipeLayout.setLoading(false);
        Toast.makeText(getApplicationContext(), "正在刷新", Toast.LENGTH_SHORT).show();
	}
	
	//button监听
	class Newslistener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.newstoHome){
				Save();
			}
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		if(keyCode == KeyEvent.KEYCODE_BACK){                             
			Save();
	    }  
	    return false;  
	}
	
	//返回时保存新闻
	public void Save(){
		int cnt = 0, flag = 1;
		URL = firstUrl = lastUrl = "";
		for(int i=0; i<URLO.length(); i++){
			char ch = URLO.charAt(i);
			URL += ch;
			if(ch == '$'){
				if(++cnt == 30) break;
				continue;
			}
			if(cnt == 0) firstUrl += ch;
			if(cnt % 3 == 0) {
				if(flag == 0) lastUrl += ch;
				else{
					flag = 0;
					lastUrl = "" + ch;
				}
			}else if(cnt % 3 == 1) flag = 1;
		}
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
		finish();
	}
	
	//显示到listview
	public void Do(int sta){
//		System.out.println("DO");
		if(sta != 0)
		for(HashMap<String,Object> mp: mList){
			mListData.add(mp);
		}else mListData = mList;
		
		adapter = new SimpleAdapter(this, mListData, R.layout.list_item,  
                new String[]{"image", "text"}, new int[]{R.id.image, R.id.text});
        
        adapter.setViewBinder(new ViewBinder() {      
            public boolean setViewValue(View view, Object data,  
                    String textRepresentation) {  
                //判断是否为我们要处理的对象  
                if(view instanceof ImageView  && data instanceof Bitmap){  
                    ImageView iv = (ImageView) view;  
                    iv.setImageBitmap((Bitmap) data);  
                    return true;  
                }else return false;  
            }  
        });  
        mListView.setAdapter(adapter);

	} 
	
	//得到包含标题、url、图片的list
	public List<HashMap<String,Object>> getListData(){  
        List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();  
        HashMap<String,Object> map = null;
        int cnt = 0;
        String title = "", murl = "", Url = "", tmp = "";
        firstUrl = "";
        for (int i = 0;i < URL.length(); i++){ 
        	if(cnt == 0){
        		map = new HashMap<String, Object>(); 
        		if(URL.charAt(i) == '$'){
        			map.put("url", Url);
        			lastUrl = Url;
        			if (firstUrl.equals("")) firstUrl = Url;
        			Url = "";
        			cnt++;
        		}else Url += URL.charAt(i);
        	}else if(cnt == 1){
        		if(URL.charAt(i) == '$'){
        			map.put("text", title);
        			tmp = title;
//        			System.out.println(title);
        			title = "";
        			cnt++;
        		}else title += URL.charAt(i);
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
        System.out.println("fisrt : " + firstUrl);
        System.out.println("last : " + lastUrl);
        System.out.println("title : " + tmp);
        return list;  
    }  
	
	//从图片的url中获取图片
	public Bitmap getBitmap(String mUrl){  
		if(mUrl.equals("NONE")) {
			/*ImageView mImageView = (ImageView)this.findViewById(R.id.image);
			System.out.println("123" + R.drawable.emptyimage);
			Drawable tmp = mImageView.getResources().getDrawable(R.drawable.emptyimage);
			System.out.println("456");
			BitmapDrawable bd = (BitmapDrawable) tmp;
			System.out.println("789");
			return bd.getBitmap();
			*/
			InputStream is = getResources().openRawResource(R.drawable.emptyimage);  
	        Bitmap mBitmap = BitmapFactory.decodeStream(is);
	        return mBitmap;
			/*Bitmap mBitmap = null, resizeBmp = null;
	        //获得屏幕分辨率
	        Display mDisplay = getWindowManager().getDefaultDisplay();
	        int W = mDisplay.getWidth();
	        try {  
	            URL url = new URL(mUrl);  
	            InputStream is = url.openStream();
	            mBitmap = BitmapFactory.decodeStream(is);
	            //获得图片分辨率
	            int w = mBitmap.getWidth();
	            double scale = (double)W / 10.0 / (double)w;//屏幕图片比例10:1
	            Matrix matrix = new Matrix();
	            matrix.postScale((float)scale, (float)scale);
	            resizeBmp = Bitmap.createBitmap(mBitmap,0,0,mBitmap.getWidth(), 
	                    mBitmap.getHeight(),matrix,true);
	        } catch (MalformedURLException e) {  
	            e.printStackTrace();  
	            return null;
	        } catch (IOException e) {  
	            e.printStackTrace(); 
	            return null;
	        }  
	        return resizeBmp;  */
		}
        Bitmap mBitmap = null, resizeBmp = null;
        //获得屏幕分辨率
        Display mDisplay = getWindowManager().getDefaultDisplay();
        int W = mDisplay.getWidth();
        try {  
            URL url = new URL(mUrl);  
            InputStream is = url.openStream();
            mBitmap = BitmapFactory.decodeStream(is);
            //获得图片分辨率
            int w = mBitmap.getWidth();
            double scale = (double)W / 2.0 / (double)w;//屏幕图片比例2:1
            Matrix matrix = new Matrix();
            matrix.postScale((float)scale, (float)scale);
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


