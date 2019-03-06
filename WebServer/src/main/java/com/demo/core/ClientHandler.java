package com.demo.core;

import java.io.File;
import java.net.Socket;

import com.demo.context.HttpServletContext;
import com.demo.context.ServletContext;
import com.demo.http.HttpServletRequest;
import com.demo.http.HttpServletResponse;
import com.demo.servlet.RegisterServlet;

/**
 * 客户端
 * 
 * @author 贾小东东
 *
 */
public class ClientHandler implements Runnable {
	private Socket socket;

	public ClientHandler(Socket socket) {
		super();
		this.socket = socket;
	}

	/**
	 * 线程开始工作的方法
	 */
	public void run() {
		HttpServletRequest request = new HttpServletRequest(socket);
		HttpServletResponse response = new HttpServletResponse(socket);
		// 获取客户端访问路径
		String url = request.getRequestURI();
		String servletName = ServletContext.getServletNameByUrl(url);
		if (servletName != null) {
			try {
				Class<?> cls = Class.forName(servletName);
				RegisterServlet servlet = (RegisterServlet) cls.newInstance();
				servlet.service(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			// 找到对应文件
			File file = new File("webapps" + url);
			// 截取后缀
			String contentType = url.substring(url.lastIndexOf(".") + 1);
			// 如果文件存在则执行正常响应操作
			if (file.exists()) {
				response.setEntity(file);
				response.setStatusCode(200);
				response.setHeaders("Content-Type", HttpServletContext.getStatusMapping(contentType));
				response.setHeaders("Content-Length", file.length() + "");
				response.flush();
			} else {
				
			}
			
		}
	}

}
