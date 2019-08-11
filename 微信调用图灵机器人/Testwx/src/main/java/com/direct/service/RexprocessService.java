package com.direct.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;

import com.direct.entity.ReceiveXmlEntity;
import com.direct.util.MenuUtil;

public class RexprocessService {
/*
 * 解析xml
 * */
	private ReceiveXmlEntity rxe;
	public String processXml(String xml){
		 rxe = this.getReceiveEntity(xml);
		//回复内容
		String result = "";
		if("event".endsWith(rxe.getMsgType())){//事件类型
			if("subscribe".endsWith(rxe.getEvent())){
				result = "欢迎关注我的微信公众号";
				new MenuUtil().createMenu(); //创建菜单
				//return "<xml><ToUserName><![CDATA["+rxe.getFromUserName()+"]]></ToUserName><FromUserName><![CDATA["+rxe.getToUserName()+"]]></FromUserName><CreateTime>"+new Date().getTime()+"</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[subscribe]]></Event><EventKey><![CDATA[]]></EventKey></xml>";
				return "<xml><ToUserName><![CDATA["+rxe.getFromUserName()+"]]></ToUserName><FromUserName><![CDATA["+rxe.getToUserName()+"]]></FromUserName><CreateTime>"+new Date().getTime()+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA["+result+"]]></Content></xml>";
			}else if("unsubscribe".endsWith(rxe.getEvent())){//用户取消关注事件
				System.out.println("用户："+rxe.getToUserName()+"取消了关注");
			}
			}else if("text".endsWith(rxe.getMsgType())){ //endsWith 以...结尾
				if("陈林".equals(rxe.getContent())){
					result = "大哥";
					//return "<xml><ToUserName><![CDATA["+rxe.getFromUserName()+"]]></ToUserName><FromUserName><![CDATA["+rxe.getToUserName()+"]]></FromUserName><CreateTime>"+new Date().getTime()+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA["+result+"]]></Content></xml>";
					return this.message(rxe, result, "text");
				}else{
					//调用图灵机器人
					return this.tuLing();
				}
			
		}
		return null;
	}
	//

	public ReceiveXmlEntity getReceiveEntity(String xml){
		
		try {
			//通过dom4j解析xml
			Document doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			Iterator it = root.elementIterator();
			//反射创建实例对象
			Class clas = Class.forName("com.direct.entity.ReceiveXmlEntity");
			ReceiveXmlEntity rxe = (ReceiveXmlEntity) clas.newInstance();
			//遍历节点标签（对应属性赋值）
			while(it.hasNext()){
				Element ele = (Element) it.next();
				//根据当前标签名（属性名）得到实体对象属性
				Field field = clas.getDeclaredField(ele.getName());
				field.setAccessible(true);//强制访问私有属性
				//根据当前的标签名（属性名）得到实体属性对应的set方法
				Method method = clas.getMethod("set"+ele.getName(), field.getType());
				method.invoke(rxe, ele.getText());
			}
			return rxe;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public String tuLing(){//图灵机器人调用接口
		//创建图灵机器人请求对象地址
		String path="http://openapi.tuling123.com/openapi/api/v2";
		try {
			URL url = new URL(path);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			
			//设置请求属性
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/json");
			http.setDoInput(true);//输入输出流 图灵需要
			http.setDoOutput(true);
			http.connect();
			
			//传递参数
			OutputStream os = http.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
			//String str="{\"perception\":{\"inputText\":{\"text\": \""+rxe.getContent()+"\"}},\"userInfo\":{\"apiKey\":\"11fac7778c3e48f1bc79decc823e5dfb\",\"userId\":\"441513\"}}";
			String srt="{\"perception\": {\"inputText\": {\"text\": \""+rxe.getContent()+"\"}},\"userInfo\": {\"apiKey\": \"11fac7778c3e48f1bc79decc823e5dfb\",\"userId\": \"441513\"}}";
			osw.write(srt);
			osw.close();
			//获取访问接口后的内容
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] databt = new byte[size];
			is.read(databt);
			is.close();
			
			String message = new String(databt,"utf-8");
			//转换为josn字符串
			 JSONObject jobj = new JSONObject(message);
			 String result = jobj.getJSONArray("results").getJSONObject(0).getJSONObject("values").getString("text");
			 return this.message(rxe, result, "text");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	//回复消息的xml方法
	public String message(ReceiveXmlEntity entity,String result,String type){
		String msg="<xml><ToUserName><![CDATA["+entity.getFromUserName()+"]]></ToUserName><FromUserName><![CDATA["+entity.getToUserName()+"]]></FromUserName><CreateTime>"+new Date().getTime()+"</CreateTime>";
		if("subscribe".equals(type) || "text".equals(type)){
			msg+="<MsgType><![CDATA[text]]></MsgType><Content><![CDATA["+result+"]]></Content>";
		}
		msg+="</xml>";
		return msg;
	}
}
