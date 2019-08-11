package com.direct.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.direct.service.RexprocessService;
import com.direct.util.MenuUtil;
import com.direct.util.PersonOpenIdUtil;
import com.direct.util.TokenThreadUtil;
import com.opensymphony.xwork2.ActionSupport;

public class HandleAction extends ActionSupport {

	public void handle(){
		HttpServletRequest	request = ServletActionContext.getRequest();
		HttpServletResponse reponse = ServletActionContext.getResponse();
		try {			
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is,"utf-8");
			BufferedReader br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();//存放读取的所有数据
			String str="";//存放读取的当前一行数据
			while((str=br.readLine())!=null){
				sb.append(str);
			}
			System.out.println("xml");
			System.out.println(sb);                                      
			//定义响应回微信服务器的字符串内容
			String result="";
			//微信发过来的请求参数
			String echostr = request.getParameter("echostr");
			if(echostr!=null&&!echostr.equals("")){
				System.out.println("微信第一次接入：");
				System.out.println("signature:"+request.getParameter("signature"));
				System.out.println("timestamp:"+request.getParameter("timestamp"));
				System.out.println("nonce:"+request.getParameter("nonce"));
				System.out.println("echostr:"+request.getParameter("echostr"));
				//返回
				result=echostr;
			}else{
				System.out.println("方法被访问");
				result = new RexprocessService().processXml(sb.toString());
				System.out.println("result:"+result);
			}
				//响应
				reponse.setCharacterEncoding("utf-8");
				PrintWriter out = reponse.getWriter();
				out.print(result);
				out.close();
			} catch (Exception e) {
			
			}
	}
	public void showXml(){ //公众号的操作 然后微信给你发来的XML
		HttpServletRequest	request = ServletActionContext.getRequest();
		HttpServletResponse reponse = ServletActionContext.getResponse();
		try {
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is,"utf-8");
			BufferedReader br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();//存放读取的所有数据
			String str="";//存放读取的当前一行数据
			while((str=br.readLine())!=null){
				sb.append(str);
			}
			System.out.println("xml");
			System.out.println(sb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public String showCode(){
		HttpServletRequest	request = ServletActionContext.getRequest();
		HttpServletResponse reponse = ServletActionContext.getResponse();
		//String path="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+TokenThreadUtil.appId+"&redirect_uri="+"http://k7iahm.natappfree.cc/Testwx/handAc!showCode"+"&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
		String code = request.getParameter("code");
			System.out.println("进入获取openid方法");
			String openId = new PersonOpenIdUtil().getOpenId(code);
			System.out.println("openId:"+openId);
			String s=new PersonOpenIdUtil().getWeChatUser(openId);
	//	System.out.println("code:"+code);
	//	System.out.println("showCode被访问");
		return null;
		
	}
}
