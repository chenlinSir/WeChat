package com.direct.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;





public class TokenThreadUtil implements Runnable{
	//定义公开静态的私有属性
	public static String appId="";
	public static String appsecret="";
	public static String accessToken=null;
	
	@Override
	public void run() {
		while(true){
			
			accessToken=this.getToken(appId, appsecret);
			System.out.println("获取access_token:"+accessToken);

			if(accessToken!=null){
				try {
					Thread.sleep(60*1000*60);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

//调用接口获取access_token的方法
public static String getToken(String appId,String appsecret){
	String path="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+appsecret+"";
	
	try {
		//创建请求对象
		URL url = new URL(path);
		HttpURLConnection http = (HttpURLConnection)url.openConnection();
		
		//设置请求属性
		http.setRequestMethod("GET");
		http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		http.connect();
		
		//获取访问接口后的内容（流）
		InputStream is = http.getInputStream();
		int size = is.available();
		byte[] databt = new byte[size];
		is.read(databt);//读取流数据装入到byte数组中
		is.close();
		String message = new String(databt,"utf-8");
		//转换为json对象并获取其中的access_token值
		JSONObject jobj = new JSONObject(message);
		return jobj.getString("access_token");
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return "";
}	

}
