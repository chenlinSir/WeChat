package com.direct.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.direct.util.TokenThreadUtil;

public class InitTokenThreanServlet extends HttpServlet{
	@Override
	public void init() throws ServletException {
		//初始化 AppId和AppSecret的值
		System.out.println("执行了init");
		TokenThreadUtil.appId=getInitParameter("AppId");
		TokenThreadUtil.appsecret=getInitParameter("AppSecret");
		//开启线程
		new Thread(new TokenThreadUtil()).start();
	}
}
