package com.demo.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务器端 
 * @author 贾小东东
 *
 */
public class WebServer {
	// 服务器服务
	private ServerSocket server;
	// 端口号
	private static Integer port;
	// 线程数量
	private static Integer threadPoolCount;
	// 线程池
	private ExecutorService threadPool;

	static {
		Properties properties = new Properties();
		// 读取端口号配置文件
		InputStream inputStream = WebServer.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			// 加载文件
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		// 获取端口号
		port = Integer.parseInt(properties.getProperty("port"));
		System.out.println(port);
		// 获取线程池数量
		threadPoolCount = Integer.parseInt(properties.getProperty("threadPool"));
	}

	public WebServer() {
		try {
			// 设定端口号
			server = new ServerSocket(port);
			// 设定线程池数量
			threadPool = Executors.newFixedThreadPool(threadPoolCount);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 服务器开始工作的方法
	 */
	public void start() {
		// 循环接收客户端请求
		try {
			while (true) {
				// 等待连接
				Socket socket = server.accept();
				// 初始化客户端
				ClientHandler handler = new ClientHandler(socket);
				// 线程开始工作
				threadPool.execute(handler);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start();
	}

}
