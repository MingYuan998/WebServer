package com.demo.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理客户端请求的类
 * 
 * @author 贾小东东
 *
 */
public class HttpServletRequest {
	private Socket socket;
	// 输入流
	private InputStream inputStream;
	// 请求方式
	private String method;
	// 请求路径
	private String url;
	// 请求协议版本
	private String protocol;
	// 请求头信息
	private Map<String, String> headers = new HashMap<String, String>();
	// 请求列表参数信息
	private Map<String, String> parameters = new HashMap<String, String>();
	// 请求完整路径
	private String requestURI;
	//
	private String queryString;

	public HttpServletRequest(Socket socket) {
		super();
		this.socket = socket;
		try {
			// 从socket中获取内容
			this.inputStream = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		getRequestLine();
		getRequestHeaders();
		getRequestContent();
	}

	/**
	 * 读取请求行内容，如：GET /index.html HTTP/1.1
	 */
	private void getRequestLine() {
		String headLine = readLine();
		// 按空格拆分，分别保存
		String[] data = headLine.split("\\s");
		this.method = data[0];
		this.url = data[1];
		this.protocol = data[2];
		parseUri();
	}

	/**
	 * 解析请求地址
	 */
	private void parseUri() {
		// 找到？的位置
		int index = url.indexOf("?");
		// 如果不是-1，说明存在？
		if (index != -1) {
			this.requestURI = url.substring(0, index);// 把左半部分储存到新URI中
			this.queryString = url.substring(index + 1);// 右半部分储存到queryString
			String[] newData = queryString.split("&");// 按&拆分
			for (String string : newData) {
				String[] arr = string.split("=");
				if (arr.length == 2) {
					this.parameters.put(arr[0], arr[1]);
				} else {
					this.parameters.put(arr[0], "");
				}
			}
		} else {
			this.requestURI = url;
		}
	}

	/**
	 * 读取请求头内容，并放入map集合中
	 */
	private void getRequestHeaders() {
		// 循环读取
		while (true) {
			String header = readLine();
			// 读取到空的时候退出
			if ("".equals(header)) {
				break;
			}
			// 按照":空格"拆分
			String[] data = header.split(":\\s");
			// 放入集合中
			headers.put(data[0], data[1]);
		}
	}
	
	/**
	 * 解析请求正文内容
	 */
	private void getRequestContent() {
		//正文中如果包含Content-Length，说明正文有内容
		if (headers.containsKey("Content-Length")) {
			//获取内容
			int length = Integer.parseInt(headers.get("Content-Length"));
			//读取相应的字节
			byte[] data = new byte[length];
			try {
				inputStream.read(data);
				//如果Content-Type为true，说明内容为post请求
				if ("application/x-www-form-urlencoded".equals(headers.get("Content-Type"))) {
					this.queryString = new String(data,"ISO8859-1");
					parseUri();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取客户端请求的内容：请求行、请求头、请求正文
	 * 
	 * @return 读取到内容，要清除两边的空白
	 */
	private String readLine() {
		StringBuilder builder = new StringBuilder();
		char cr = 'a';// 回车符ASCLL码：13
		char lf = 'a';// 换行符ASCLL码：10
		// 读取字节
		int len;
		try {
			// 循环读取内容,直到读取到-1(代表读取完毕)
			while ((len = inputStream.read()) != -1) {
				lf = (char) len;// 读取到的内容赋值给lf
				// 读取到CRLF时退出
				if (cr == 13 && lf == 10) {
					break;
				}
				builder.append(lf);// 追加
				cr = lf;// 再赋值给cr
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString().trim();
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public String getParameters(String parameter) {
		return parameters.get(parameter);
	}
	
	
}
