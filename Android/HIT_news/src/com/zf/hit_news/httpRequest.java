package com.zf.hit_news;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class httpRequest{
	public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        int flag = 0;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //System.out.print("123");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            //发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            result = java.net.URLDecoder.decode(result, "utf-8");
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
            flag = 1;
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
                flag = 1;
            }
        }
        if (flag == 1) return null;
        return result;
    }    
	
	public static String toUtf8(String s){
		try{
			s = java.net.URLEncoder.encode(s, "utf-8");
		} catch (Exception e){
			
		}
		return s;
	}
	
	
	public static int addKeyword(String word, String id){
		System.out.println("123:" + word);
		System.out.println("456:" + id);
		word = toUtf8(word);
		id = toUtf8(id);
		String ret = sendPost("http://hitnewsapp.sinaapp.com/test", "op=1&word=" + word + "&id=" + id);
		if (ret == null || ret.equals("failed")) return 0; 
		System.out.println(ret);
		return 1;
	}
	
	public static int delKeyword(String word, String id){
		word = toUtf8(word);
		id = toUtf8(id);
		String ret = sendPost("http://hitnewsapp.sinaapp.com/test", "op=2&word=" + word + "&id=" + id);
		if (ret == null || ret.equals("failed")) return 0; 
		//System.out.println(ret);
		return 1;
	}
	
	public static String getNewsNewer(String word, String id){
		word = toUtf8(word);
		id = toUtf8(id);
		String ret = sendPost("http://hitnewsapp.sinaapp.com/test", "op=0&word=" + word + "&id=" + id);
//		System.out.println(1);
		if (ret == null) return null;
//		System.out.println(2);
		return ret;
	}
	public static String getNewsOlder(String word, String id){
		word = toUtf8(word);
		id = toUtf8(id);
		String ret = sendPost("http://hitnewsapp.sinaapp.com/test", "op=4&word=" + word + "&id=" + id);
		if (ret == null) return null;
		return ret;
	}
	
    public static int sendUrl(String word, String id){
		word = toUtf8(word);
		id = toUtf8(id);
		String ret = sendPost("http://hitnewsapp.sinaapp.com/test", "op=3&word=" + word + "&id=" + id);
		System.out.println(ret);
		if (ret == null) return 0;
		return 1;
	}
    public static void main(String[] args) {
    	System.out.println(addKeyword("2014", "070dec8af07"));
//    	System.out.println(123);
    	
    	//(delKeyword("123", "456"));
    	//String sr = sendPost("http://hitnewsapp.sinaapp.com/test", "op=1&v=");
        //System.out.println(sr);
    }


}
