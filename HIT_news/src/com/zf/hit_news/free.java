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
    /**listviewʵ������*/  
    private ListView mListView;  
  
      
    private int headViewWidth, headViewHeight;  
    // ʵ�ʵ�padding�ľ����������ƫ�ƾ���ı���  
    private final static int RATIO = 3;  
    /**��ת����*/  
    private RotateAnimation animation,reverseAnimation;  
    /**��ת����*/  
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
    // ���ڱ�֤startY��ֵ��һ��������touch�¼���ֻ����¼һ��  
    private boolean isRecored;  
      
  
    private int firstItemIndex;  
  
    private int state;  
  
    private boolean isBack;  
  
    private TextView tipsTextview;  
    private TextView lastUpdatedTextView;  
    private ProgressBar progressBar;  
    //�鿴����  
    private TextView moreTextView;  
    //���ڼ��ؽ�����  
    private LinearLayout loadProgressBar;  
    //��ҳ���ص����ݵ�����  
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
	
//	URL = "http://today.hit.edu.cn$��������$http://img03.store.sogou.com/net/a/04/link?&url=http%3A%2F%2Ftoday.hit.edu.cn%2Fuploadfiles%2F2014%2F11-7%2F201411715181.jpg&appid=100520124&referer=http://today.hit.edu.cn/news/2014/11-07/9174815111RL0.htm$http://www.baidu.com$�ٶ�$http://www.baidu.com/img/bdlogo.png$";
//	public final String Url = "http://img03.store.sogou.com/net/a/04/link?&url=http%3A%2F%2Ftoday.hit.edu.cn%2Fuploadfiles%2F2014%2F11-7%2F201411715181.jpg&appid=100520124&referer=http://today.hit.edu.cn/news/2014/11-07/9174815111RL0.htm";
	
	
	List<HashMap<String,Object>> mListData = null, mList = null;
	 Handler handler=new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            if (msg.what==0x9527) {
	                //��ʾ���������ص�ͼƬ
	            	System.out.println("mess");
	            	Do();
	            	Toast.makeText(getApplicationContext(), "ˢ�³ɹ�", Toast.LENGTH_SHORT).show();
	            }else if(msg.what==0x9528){
	            	Toast.makeText(getApplicationContext(), "ˢ��ʧ��\n��������", Toast.LENGTH_SHORT).show();
	            }
	            else if(msg.what==0x9529)
	            	Toast.makeText(getApplicationContext(), "û�и���", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getApplicationContext(), "...Wait\n��������", Toast.LENGTH_SHORT).show();
		
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
				/**put��ϱ���Ҫcommit()�����޷�����**/
				editor.commit();
				System.out.println("*****����URL�ɹ�********"+URL);
				
				editor.putString(KEY_last, lastUrl);
				editor.commit();
				/**put��ϱ���Ҫcommit()�����޷�����**/
				System.out.println("����last------"+lastUrl);
				
				editor.putString(KEY_first, firstUrl);
				/**put��ϱ���Ҫcommit()�����޷�����**/
				editor.commit();
				System.out.println("����first------"+firstUrl); 
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
			        Toast.makeText(getApplicationContext(), "Wait\n��������", Toast.LENGTH_SHORT).show();
				}
		}
			
		
	}
	public void Do(){
//		System.out.println("DO");
//		mShared = getSharedPreferences(SHARED_keyword, Context.MODE_PRIVATE);
//		Editor editor = mShared.edit();
//		editor.putString(KEY_last, lastUrl);
//		/**put��ϱ���Ҫcommit()�����޷�����**/
//		editor.commit();
//		System.out.println("����last�ɹ�"+lastUrl);

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
                //�ж��Ƿ�Ϊ����Ҫ����Ķ���  
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
     * ��ListView�����"���ظ���"  
     */  
    private void addPageMore(){  
        View view=LayoutInflater.from(this).inflate(R.layout.bottom, null);  
        moreTextView=(TextView)view.findViewById(R.id.more_id);  
        loadProgressBar=(LinearLayout)view.findViewById(R.id.load_id);  
        moreTextView.setOnClickListener(new Button.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                //����"���ظ���"  
                moreTextView.setVisibility(View.GONE);  
                //��ʾ������  
                loadProgressBar.setVisibility(View.VISIBLE);  
                new refreshTask().execute();  
  
            }  
        });  
        mListView.addFooterView(view);  
    }  
      
    /**  
     * ������һҳ������  
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
  
  
    // �˷���ֱ���հ��������ϵ�һ������ˢ�µ�demo���˴��ǡ����ơ�headView��width�Լ�height  
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
     * ģ�����ݷ�ҳ���أ�  
     * @param pageStart  ��ʼ��  
     * @param pageSize   ÿҳ��ʾ��Ŀ  
     * @return  
     */  
    public static List<Map<String,Object>> initValue(int pageStart,int pageSize){  
        Map<String,Object> map;  
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();  
        for(int i=0;i<pageSize;i++){  
            map=new HashMap<String,Object>();  
            map.put("text", "����listview�б�");  
            map.put("title", "��"+pageStart+"��");  
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
                    Log.v(TAG, "��downʱ���¼��ǰλ�á�"+startY);  
                }  
                break;  
  
            case MotionEvent.ACTION_UP:  
  
                if (state != REFRESHING && state != LOADING) {  
                    if (state == DONE) {  
                        // ʲô������  
                    }  
                    if (state == PULL_To_REFRESH) {  
                        state = DONE;  
                        changeHeaderViewByState();  
                        Log.v(TAG, "������ˢ��״̬����done״̬");  
                    }  
                    if (state == RELEASE_To_REFRESH) {  
                        state = REFRESHING;  
                        changeHeaderViewByState();  
                        Log.v(TAG, "���ɿ�ˢ��״̬����done״̬");  
                    }  
                }  
  
                isRecored = false;  
                isBack = false;  
  
                break;  
  
            case MotionEvent.ACTION_MOVE:  
                int tempY = (int) event.getY();  
  
                if (!isRecored && firstItemIndex == 0) {  
                    Log.v(TAG, "��moveʱ���¼��λ��"+tempY);  
                    isRecored = true;  
                    startY = tempY;  
                }  
  
                if (state != REFRESHING && isRecored && state != LOADING) {  
  
                    // ��֤������padding�Ĺ����У���ǰ��λ��һֱ����head������������б�����Ļ�Ļ����������Ƶ�ʱ���б��ͬʱ���й���  
  
                    // ��������ȥˢ����  
                    if (state == RELEASE_To_REFRESH) {  
  
  
                        mListView.setSelection(0);  
  
                        // �������ˣ��Ƶ�����Ļ�㹻�ڸ�head�ĳ̶ȣ����ǻ�û���Ƶ�ȫ���ڸǵĵز�  
                        if (((tempY - startY) / RATIO < headViewHeight)&& (tempY - startY) > 0) {  
                            state = PULL_To_REFRESH;  
                            changeHeaderViewByState();  
  
                            Log.v(TAG, "���ɿ�ˢ��״̬ת�䵽����ˢ��״̬");  
                        }  
                        // һ�����Ƶ�����  
                        else if (tempY - startY <= 0) {  
                            state = DONE;  
                            changeHeaderViewByState();  
  
                            Log.v(TAG, "���ɿ�ˢ��״̬ת�䵽done״̬");  
                        }  
                        // �������ˣ����߻�û�����Ƶ���Ļ�����ڸ�head�ĵز�  
                        else {  
                            // ���ý����ر�Ĳ�����ֻ�ø���paddingTop��ֵ������  
                        }  
                    }  
                    // ��û�е�����ʾ�ɿ�ˢ�µ�ʱ��,DONE������PULL_To_REFRESH״̬  
                    if (state == PULL_To_REFRESH) {  
  
  
                        // ���������Խ���RELEASE_TO_REFRESH��״̬  
                        if ((tempY - startY) / RATIO >= headViewHeight) {  
                            state = RELEASE_To_REFRESH;  
                            isBack = true;  
                            changeHeaderViewByState();  
  
                            Log.v(TAG, "��done��������ˢ��״̬ת�䵽�ɿ�ˢ��");  
                        }  
                        // ���Ƶ�����  
                        else if (tempY - startY <= 0) {  
                            state = DONE;  
                            changeHeaderViewByState();  
  
                            Log.v(TAG, "��DOne��������ˢ��״̬ת�䵽done״̬");  
                        }  
                    }  
  
                    // done״̬��  
                    if (state == DONE) {  
                        if (tempY - startY > 0) {  
                            state = PULL_To_REFRESH;  
                            changeHeaderViewByState();  
                        }  
                    }  
  
                    // ����headView��size  
                    if (state == PULL_To_REFRESH) {  
                        headView.setPadding(0, (int)(-1 * headViewHeight  
                                + (tempY - startY) / RATIO), 0, 0);  
                        System.out.println("top_distance0:"+(-1 * headViewHeight + (tempY - startY) / RATIO));  
  
  
                    }  
  
                    // ����headView��paddingTop  
                    if (state == RELEASE_To_REFRESH) {  
                        headView.setPadding(0, (int)((tempY - startY) / RATIO  
                                - headViewHeight), 0, 0);  
                    }  
  
                }  
  
                break;  
            }  
  
        return false;  
    }  
    // ��״̬�ı�ʱ�򣬵��ø÷������Ը��½���  
    private void changeHeaderViewByState() {  
        switch (state) {  
        case RELEASE_To_REFRESH:  
            head_arrowImageView.setVisibility(View.VISIBLE);  
            progressBar.setVisibility(View.GONE);  
            tipsTextview.setVisibility(View.VISIBLE);  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
  
            head_arrowImageView.clearAnimation();  
            head_arrowImageView.startAnimation(animation);  
  
            tipsTextview.setText("�ɿ�ˢ��");  
  
            Log.v(TAG, "��ǰ״̬���ɿ�ˢ��");  
            break;  
        case PULL_To_REFRESH:  
            progressBar.setVisibility(View.GONE);  
            tipsTextview.setVisibility(View.VISIBLE);  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
            head_arrowImageView.clearAnimation();  
            head_arrowImageView.setVisibility(View.VISIBLE);  
            // ����RELEASE_To_REFRESH״̬ת������  
            if (isBack) {  
                isBack = false;  
                head_arrowImageView.clearAnimation();  
                head_arrowImageView.startAnimation(reverseAnimation);  
  
                tipsTextview.setText("����ˢ��");  
            } else {  
                tipsTextview.setText("����ˢ��");  
            }  
            Log.v(TAG, "��ǰ״̬������ˢ��");  
            break;  
  
        case REFRESHING:  
  
            headView.setPadding(0, 0, 0, 0);  
  
            progressBar.setVisibility(View.VISIBLE);  
            head_arrowImageView.clearAnimation();  
            head_arrowImageView.setVisibility(View.GONE);  
            tipsTextview.setText("����ˢ��...");  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
            lastUpdatedTextView.setVisibility(View.GONE);  
              
            Log.v(TAG, "��ǰ״̬,����ˢ��...");  
            new refreshTask().execute();  
            break;  
        case DONE:  
            headView.setPadding(0, -1 * headViewHeight, 0, 0);  
  
            progressBar.setVisibility(View.GONE);  
            head_arrowImageView.clearAnimation();  
            head_arrowImageView.setImageResource(R.drawable.ic_launcher);  
            tipsTextview.setText("����ˢ��");  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
  
            Log.v(TAG, "��ǰ״̬��done");  
            break;  
        }  
    }  
      
    class refreshTask extends AsyncTask<Void, Void, Void>{  
  
        @Override  
        protected Void doInBackground(Void... params) {  
            // TODO Auto-generated method stub  
              
            //��Ҫ�ڼ��ع�������������  
              
            try{  
                Thread.sleep(3000);  
            }catch(Exception e){  
                e.printStackTrace();  
            }  
  
            //����ģ�����ݣ���һҳ���ݣ� ����������£�����������ǲ���Ҫ��ֱ��ʹ����������������������  
            chageListView(data.size()+1,pageSize);  
            return null;  
        }  
  
        @Override  
        protected void onProgressUpdate(Void... values) {  
            // TODO Auto-generated method stub  
            super.onProgressUpdate(values);  
            //���ȸ���  
        }  
  
        @Override  
        protected void onPostExecute(Void result) {  
            // TODO Auto-generated method stub  
            super.onPostExecute(result);  
            //�������֮��Ӧ����ʲô���Ĳ���  
              
  
            //֪ͨ�����������ָı����  
            adapter.notifyDataSetChanged();  
            onRefreshComplete();  
            //�ٴ���ʾ"���ظ���"  
            moreTextView.setVisibility(View.VISIBLE);  
            //�ٴ����ء���������  
            loadProgressBar.setVisibility(View.GONE);  
        }  
  
    }  
    /**ˢ����ɣ���ԭ�������״̬���޸ĸ���ʱ��*/  
    public void onRefreshComplete() {  
        state = DONE;  
        //��������ˢ��ʱ��  
        lastUpdatedTextView.setText("�������:" + new Date().toLocaleString());  
        changeHeaderViewByState();  
    } 
    }




