package com.direct.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class MenuUtil {
	//创建菜单的方法
	public void createMenu(){
		String path="https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+TokenThreadUtil.accessToken;
		try {
			//创建请求对象
			URL url=new URL(path);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			
			//设置请求属性
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/json");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.connect();
			
			//传递参数
			String url1 = "http://k7iahm.natappfree.cc/Testwx/handAc!showCode";//重定向地址
			String redirect="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+TokenThreadUtil.appId+"&redirect_uri="+url1+"&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
			//String path="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+TokenThreadUtil.appId+"&redirect_uri="+"http://k7iahm.natappfree.cc/Testwx/handAc!showCode"+"&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
			String menu="{\"button\":[{\"type\":\"click\",\"name\":\"疾风剑豪\",\"key\":\"menu1\"},{\"name\":\"战争之影\",\"sub_button\":[{\"type\":\"view\",\"name\":\"百度一下\",\"url\":\"http://www.baidu.com/\"},{\"type\":\"view\",\"name\":\"个人资料\",\"url\":\""+redirect+"\"}]}]}";
			System.out.println(menu);
			OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
			osw.write(menu);
			osw.close();
			
			//获取访问接口后的内容(流)
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] databt=new byte[size];
			is.read(databt);//读取流数据装入到byte数组中
			is.close();
			
			String message = new String(databt, "UTF-8");
			System.out.println(message);
			//转换为json对象并获取其中的access_token值
			JSONObject jobj=new JSONObject(message);
			String errmsg = jobj.getString("errmsg");
			System.out.println("errmsg:"+errmsg);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
