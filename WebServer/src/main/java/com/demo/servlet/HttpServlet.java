package com.demo.servlet;

import com.demo.http.HttpServletRequest;
import com.demo.http.HttpServletResponse;
/**
 * Servlet父类
 * @author 贾小东东
 *
 */
public abstract class HttpServlet {
	/**
	 * servlet工作的方法
	 * @param request
	 * @param response
	 */
	public abstract void service(HttpServletRequest request,HttpServletResponse response);
	/**
	 * 转发
	 * @return
	 */
	public String forward() {
		
		return "";
	}
}
