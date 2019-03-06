package com.demo.servlet;

import com.demo.http.HttpServletRequest;
import com.demo.http.HttpServletResponse;
/**
 * 注册
 * @author 贾小东东
 *
 */
public class RegisterServlet extends HttpServlet{
	
	public void service(HttpServletRequest request,HttpServletResponse response) {
		String id = request.getParameters("id");
		String username = request.getParameters("username");
		String password = request.getParameters("password");
		System.out.println("id:"+id+",username:"+username+",password:"+password);
		response.sendRedirect("reg.html");
	}
}
