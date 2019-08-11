package com.direct.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class PersonOpenIdUtil {
	//通过code获取openid
	public String getOpenId(String code){
		
		String path="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+TokenThreadUtil.appId+"&secret="+TokenThreadUtil.appsecret+"&code="+code+"&grant_type=authorization_code";
		
		try {
			//创建请求对象
			URL url = new URL(path);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			
			//设置请求属性
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/json");
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
			return jobj.getString("openid");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String getWeChatUser(String openId){
		String path = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+TokenThreadUtil.accessToken+"&openid="+openId+"&lang=zh_CN";
		
		try {
			//创建请求对象
			URL url = new URL(path);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			
			//设置请求属性
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/json");
			http.setDoInput(true);
			http.setDoOutput(true);
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
			String openid = jobj.getString("openid");
			String nickname = jobj.getString("nickname");
			System.out.println("getWeChatUser方法中获取openid："+openid);
			System.out.println("getWeChatUser方法中获取nickname:"+nickname);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
