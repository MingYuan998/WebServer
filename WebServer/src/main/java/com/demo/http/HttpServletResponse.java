package com.demo.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.demo.context.HttpServletContext;

/**
 * 响应给客户端数据的类
 * 
 * @author 贾小东东
 *
 */
public class HttpServletResponse {
	private Socket socket;
	private OutputStream out;
	// 状态码
	private int statusCode;
	// 响应头信息的存储
	private Map<String, String> headers = new HashMap<String, String>();
	// 响应文件的实体
	private File entity;

	public HttpServletResponse(Socket socket) {
		super();
		this.socket = socket;
		try {
			this.out = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 响应开始工作的方法，分为三步：1.发送状态行；2.发送响应头；3.发送响应正文
	 */
	public void flush() {
		sendStatusLine();
		sendHeaders();
		sendContext();
	}

	/**
	 * 发送状态行信息
	 */
	private void sendStatusLine() {
		String line = "HTTP/1.1 "+statusCode+" "+HttpServletContext.getStatusCode(statusCode);
		print(line);
	}

	/**
	 * 发送响应头信息
	 */
	private void sendHeaders() {
		// 遍历map集合
		for (Entry<String, String> header : headers.entrySet()) {
			// 取出对应的key和value
			String line = header.getKey() + ":" + header.getValue();
			// 发送
			print(line);
		}
		// 发送CRLF代表响应头信息发送完毕
		print("");
	}

	/**
	 * 发送响应正文
	 */
	private void sendContext() {
		try (FileInputStream fis = new FileInputStream(entity);) {
			// 创建10kb的字节数组
			byte[] data = new byte[1024 * 10];
			int len;
			// 循环读取10kb的内容
			while ((len = fis.read(data)) != -1) {
				// 读取到的文件内容响应给客户端
				out.write(data, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 重定向
	 * @param url
	 */
	public void sendRedirect(String url) {
		//设置状态码
		this.setStatusCode(302);
		//设置响应类型
		this.setHeaders("Location", url);
		//调用响应的方法，发送状态行、响应头，响应正文
		this.flush();
	}

	/**
	 * 响应数据的发送
	 * 
	 * @param line
	 */
	private void print(String line) {
		try {
			out.write(line.getBytes("ISO8859-1"));
			out.write(HttpServletContext.CR);
			out.write(HttpServletContext.LF);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setHeaders(String type, String content) {
		headers.put(type, content);
	}

	public void setEntity(File entity) {
		this.entity = entity;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
}
