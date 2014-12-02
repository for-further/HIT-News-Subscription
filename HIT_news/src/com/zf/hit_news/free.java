package com.zf.hit_news;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
//import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;

public class free extends ActionBarActivity implements OnTouchListener{
	/*********************************/
    /**listview实例对象*/  
    private ListView mListView;  
  
      
    private int headViewWidth, headViewHeight;  
    // 实际的padding的距离与界面上偏移距离的比例  
    private final static int RATIO = 3;  
    /**旋转动画*/  
    private RotateAnimation animation,reverseAnimation;  
    /**旋转对象*/  
    private ImageView head_arrowImageView;  
      
    private float startY;  
      
    private View headView;  
      
    private SimpleAdapter adapter;  
  
    private List<Map<String,Object>> data;  
      
  
    private static final String TAG = "listview";  
  
    private final static int RELEASE_To_REFRESH = 0;  
    private final static int PULL_To_REFRESH = 1;  
    private final static int REFRESHING = 2;  
    private final static int DONE = 3;  
    private final static int LOADING = 4;  
    // 用于保证startY的值在一个完整的touch事件中只被记录一次  
    private boolean isRecored;  
      
  
    private int firstItemIndex;  
  
    private int state;  
  
    private boolean isBack;  
  
    private TextView tipsTextview;  
    private TextView lastUpdatedTextView;  
    private ProgressBar progressBar;  
    //查看更多  
    private TextView moreTextView;  
    //正在加载进度条  
    private LinearLayout loadProgressBar;  
    //分页加载的数据的数量  
    private int pageSize=10;  
  
    private final int pageType=1;  
	/************************************/
	private ImageView newstoHome,refresh;
//	ListView mListView = null;
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
	            	Do();
	            	Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
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
		/*****************/
        mListView.setOnTouchListener(this);  
        
        
        
        LayoutInflater layoutInflater = getLayoutInflater();  
  
        animation = new RotateAnimation(0, 180,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);  
        animation.setFillAfter(true);  
        animation.setDuration(200);  
        animation.setInterpolator(new LinearInterpolator());  
  
  
        reverseAnimation = new RotateAnimation(180, 0,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);  
        reverseAnimation.setFillAfter(true);  
        reverseAnimation.setDuration(200);  
        reverseAnimation.setInterpolator(new LinearInterpolator());  
          
        headView = layoutInflater.from(this).inflate(R.layout.head, null);  
          
          
  
        progressBar = (ProgressBar) headView  
                .findViewById(R.id.head_progressBar);  
        tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);  
        lastUpdatedTextView = (TextView) headView  
                .findViewById(R.id.head_lastUpdatedTextView);  
          
        data = initValue(1,15);  
          
        adapter = new SimpleAdapter(this, data, android.R.layout.simple_expandable_list_item_2, new String[] {"title","text"}, new int[] {android.R.id.text1,android.R.id.text2});  
  
        head_arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);  
  
        head_arrowImageView.setMinimumHeight(50);  
        head_arrowImageView.setMinimumWidth(70);  
          
        measureView(headView);  
        addPageMore();  
          
        headViewWidth = headView.getMeasuredWidth();  
        headViewHeight = headView.getMeasuredHeight();  
  
        headView.setPadding(0, -1*headViewHeight, 0, 0);  
  
        headView.invalidate();  
        mListView.addHeaderView(headView, null, false);  
        mListView.setAdapter(adapter);  
		/****************/
		mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
		lastUrl = mShared.getString(KEY_last,"new");
		URLO=mShared.getString(KEY_news, "");
		firstUrl=mShared.getString(KEY_first, "new");
		new Thread(){
        	public void run() {
        		String id = MainActivity.getRId();
        			URL = httpRequest.getNewsNewer(firstUrl, id);
//        			System.out.println(URL);
//        			System.out.println(firstUrl);
    				URL=URL+URLO;
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
        Toast.makeText(getApplicationContext(), "...Wait\n。。。。", Toast.LENGTH_SHORT).show();
		
		Newslistener listener=new Newslistener();	
		newstoHome.setOnClickListener(listener);
		refresh.setOnClickListener(listener);
		
	
	}
	int min(int a, int b){
		return a < b ? a : b;
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
			        			URL = httpRequest.getNewsNewer(firstUrl, id);
			        			URLO=URL+URLO;
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
			        Toast.makeText(getApplicationContext(), "Wait\n。。。。", Toast.LENGTH_SHORT).show();
				}
		}
			
		
	}
	public void Do(){
//		System.out.println("DO");
//		mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
//		Editor editor = mShared.edit();
//		editor.putString(KEY_last, lastUrl);
//		/**put完毕必需要commit()否则无法保存**/
//		editor.commit();
//		System.out.println("保存last成功"+lastUrl);

		if(mListData != null)
		for(HashMap<String,Object> mp: mListData){
			mList.add(mp);
		}
		mListData = mList;
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
        mListView.setAdapter(adapter);
	} 
	public List<HashMap<String,Object>> getListData(){  
        List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();  
        HashMap<String,Object> map = null;
        int cnt = 0;
        String title = "", murl = "", Url = "";
        for(int i=0;i < URL.length();i++){ 
        	//System.out.println(URL.charAt(i));
        	if(cnt == 0){
        		map = new HashMap<String, Object>(); 
        		if(URL.charAt(i) == '$'){
        			map.put("url", Url);
        			lastUrl = Url;
//        			System.out.println(Url);
        			Url = "";
        			cnt++;
        		}else{
        			Url += URL.charAt(i);
        		}
        	}else if(cnt == 1){
        		if(URL.charAt(i) == '$'){
        			map.put("text", title);
//        			System.out.println(title);
        			title = "";
        			cnt++;
        		}else{
        			title += URL.charAt(i);
        		}
        	}else{
        		if(URL.charAt(i) == '$'){
//        			System.out.println(murl);
        			map.put("image", getBitmap(murl));
        			
        			murl = "";
        			cnt = 0;
        			list.add(map); 
        		}else{
        			murl += URL.charAt(i);
        		}
        	}
        }  
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
 
	/**  
     * 在ListView中添加"加载更多"  
     */  
    private void addPageMore(){  
        View view=LayoutInflater.from(this).inflate(R.layout.bottom, null);  
        moreTextView=(TextView)view.findViewById(R.id.more_id);  
        loadProgressBar=(LinearLayout)view.findViewById(R.id.load_id);  
        moreTextView.setOnClickListener(new Button.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                //隐藏"加载更多"  
                moreTextView.setVisibility(View.GONE);  
                //显示进度条  
                loadProgressBar.setVisibility(View.VISIBLE);  
                new refreshTask().execute();  
  
            }  
        });  
        mListView.addFooterView(view);  
    }  
      
    /**  
     * 加载下一页的数据  
     * @param pageStart  
     * @param pageSize  
     */  
    private void chageListView(int pageStart,int pageSize){  
        List<Map<String,Object>> data=initValue(pageStart,pageSize);  
        for (Map<String, Object> map : data) {  
            this.data.add(map);  
        }  
        data=null;  
    }  
  
  
    // 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height  
    private void measureView(View child) {  
        ViewGroup.LayoutParams p = child.getLayoutParams();  
        if (p == null) {  
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,  
                    ViewGroup.LayoutParams.WRAP_CONTENT);  
        }  
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);  
        int lpHeight = p.height;  
        int childHeightSpec;  
        if (lpHeight > 0) {  
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,  
                    MeasureSpec.EXACTLY);  
        } else {  
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,  
                    MeasureSpec.UNSPECIFIED);  
        }  
        child.measure(childWidthSpec, childHeightSpec);  
    }  
    /**  
     * 模拟数据分页加载，  
     * @param pageStart  起始数  
     * @param pageSize   每页显示数目  
     * @return  
     */  
    public static List<Map<String,Object>> initValue(int pageStart,int pageSize){  
        Map<String,Object> map;  
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();  
        for(int i=0;i<pageSize;i++){  
            map=new HashMap<String,Object>();  
            map.put("text", "定义listview列表");  
            map.put("title", "第"+pageStart+"项");  
            ++pageStart;  
            list.add(map);  
        }  
        return list;  
    }  
  
  
    @Override  
    public boolean onTouch(View v, MotionEvent event) {  
            switch (event.getAction()) {  
            case MotionEvent.ACTION_DOWN:  
                if (firstItemIndex == 0 && !isRecored) {  
                    isRecored = true;  
                    startY = (int) event.getY();  
                    Log.v(TAG, "在down时候记录当前位置‘"+startY);  
                }  
                break;  
  
            case MotionEvent.ACTION_UP:  
  
                if (state != REFRESHING && state != LOADING) {  
                    if (state == DONE) {  
                        // 什么都不做  
                    }  
                    if (state == PULL_To_REFRESH) {  
                        state = DONE;  
                        changeHeaderViewByState();  
                        Log.v(TAG, "由下拉刷新状态，到done状态");  
                    }  
                    if (state == RELEASE_To_REFRESH) {  
                        state = REFRESHING;  
                        changeHeaderViewByState();  
                        Log.v(TAG, "由松开刷新状态，到done状态");  
                    }  
                }  
  
                isRecored = false;  
                isBack = false;  
  
                break;  
  
            case MotionEvent.ACTION_MOVE:  
                int tempY = (int) event.getY();  
  
                if (!isRecored && firstItemIndex == 0) {  
                    Log.v(TAG, "在move时候记录下位置"+tempY);  
                    isRecored = true;  
                    startY = tempY;  
                }  
  
                if (state != REFRESHING && isRecored && state != LOADING) {  
  
                    // 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动  
  
                    // 可以松手去刷新了  
                    if (state == RELEASE_To_REFRESH) {  
  
  
                        mListView.setSelection(0);  
  
                        // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步  
                        if (((tempY - startY) / RATIO < headViewHeight)&& (tempY - startY) > 0) {  
                            state = PULL_To_REFRESH;  
                            changeHeaderViewByState();  
  
                            Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");  
                        }  
                        // 一下子推到顶了  
                        else if (tempY - startY <= 0) {  
                            state = DONE;  
                            changeHeaderViewByState();  
  
                            Log.v(TAG, "由松开刷新状态转变到done状态");  
                        }  
                        // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步  
                        else {  
                            // 不用进行特别的操作，只用更新paddingTop的值就行了  
                        }  
                    }  
                    // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态  
                    if (state == PULL_To_REFRESH) {  
  
  
                        // 下拉到可以进入RELEASE_TO_REFRESH的状态  
                        if ((tempY - startY) / RATIO >= headViewHeight) {  
                            state = RELEASE_To_REFRESH;  
                            isBack = true;  
                            changeHeaderViewByState();  
  
                            Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");  
                        }  
                        // 上推到顶了  
                        else if (tempY - startY <= 0) {  
                            state = DONE;  
                            changeHeaderViewByState();  
  
                            Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");  
                        }  
                    }  
  
                    // done状态下  
                    if (state == DONE) {  
                        if (tempY - startY > 0) {  
                            state = PULL_To_REFRESH;  
                            changeHeaderViewByState();  
                        }  
                    }  
  
                    // 更新headView的size  
                    if (state == PULL_To_REFRESH) {  
                        headView.setPadding(0, (int)(-1 * headViewHeight  
                                + (tempY - startY) / RATIO), 0, 0);  
                        System.out.println("top_distance0:"+(-1 * headViewHeight + (tempY - startY) / RATIO));  
  
  
                    }  
  
                    // 更新headView的paddingTop  
                    if (state == RELEASE_To_REFRESH) {  
                        headView.setPadding(0, (int)((tempY - startY) / RATIO  
                                - headViewHeight), 0, 0);  
                    }  
  
                }  
  
                break;  
            }  
  
        return false;  
    }  
    // 当状态改变时候，调用该方法，以更新界面  
    private void changeHeaderViewByState() {  
        switch (state) {  
        case RELEASE_To_REFRESH:  
            head_arrowImageView.setVisibility(View.VISIBLE);  
            progressBar.setVisibility(View.GONE);  
            tipsTextview.setVisibility(View.VISIBLE);  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
  
            head_arrowImageView.clearAnimation();  
            head_arrowImageView.startAnimation(animation);  
  
            tipsTextview.setText("松开刷新");  
  
            Log.v(TAG, "当前状态，松开刷新");  
            break;  
        case PULL_To_REFRESH:  
            progressBar.setVisibility(View.GONE);  
            tipsTextview.setVisibility(View.VISIBLE);  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
            head_arrowImageView.clearAnimation();  
            head_arrowImageView.setVisibility(View.VISIBLE);  
            // 是由RELEASE_To_REFRESH状态转变来的  
            if (isBack) {  
                isBack = false;  
                head_arrowImageView.clearAnimation();  
                head_arrowImageView.startAnimation(reverseAnimation);  
  
                tipsTextview.setText("下拉刷新");  
            } else {  
                tipsTextview.setText("下拉刷新");  
            }  
            Log.v(TAG, "当前状态，下拉刷新");  
            break;  
  
        case REFRESHING:  
  
            headView.setPadding(0, 0, 0, 0);  
  
            progressBar.setVisibility(View.VISIBLE);  
            head_arrowImageView.clearAnimation();  
            head_arrowImageView.setVisibility(View.GONE);  
            tipsTextview.setText("正在刷新...");  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
            lastUpdatedTextView.setVisibility(View.GONE);  
              
            Log.v(TAG, "当前状态,正在刷新...");  
            new refreshTask().execute();  
            break;  
        case DONE:  
            headView.setPadding(0, -1 * headViewHeight, 0, 0);  
  
            progressBar.setVisibility(View.GONE);  
            head_arrowImageView.clearAnimation();  
            head_arrowImageView.setImageResource(R.drawable.ic_launcher);  
            tipsTextview.setText("下拉刷新");  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
  
            Log.v(TAG, "当前状态，done");  
            break;  
        }  
    }  
      
    class refreshTask extends AsyncTask<Void, Void, Void>{  
  
        @Override  
        protected Void doInBackground(Void... params) {  
            // TODO Auto-generated method stub  
              
            //需要在加载过程中做的事情  
              
            try{  
                Thread.sleep(3000);  
            }catch(Exception e){  
                e.printStackTrace();  
            }  
  
            //加载模拟数据：下一页数据， 在正常情况下，上面的休眠是不需要，直接使用下面这句代码加载相关数据  
            chageListView(data.size()+1,pageSize);  
            return null;  
        }  
  
        @Override  
        protected void onProgressUpdate(Void... values) {  
            // TODO Auto-generated method stub  
            super.onProgressUpdate(values);  
            //进度更新  
        }  
  
        @Override  
        protected void onPostExecute(Void result) {  
            // TODO Auto-generated method stub  
            super.onPostExecute(result);  
            //加载完成之后，应该做什么样的操作  
              
  
            //通知适配器，发现改变操作  
            adapter.notifyDataSetChanged();  
            onRefreshComplete();  
            //再次显示"加载更多"  
            moreTextView.setVisibility(View.VISIBLE);  
            //再次隐藏“进度条”  
            loadProgressBar.setVisibility(View.GONE);  
        }  
  
    }  
    /**刷新完成，还原界面各种状态，修改更新时间*/  
    public void onRefreshComplete() {  
        state = DONE;  
        //更新最新刷新时间  
        lastUpdatedTextView.setText("最近更新:" + new Date().toLocaleString());  
        changeHeaderViewByState();  
    } 
    }




