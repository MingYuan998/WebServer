package com.demo.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * HTTP相关配置信息
 * 
 * @author 贾小东东
 *
 */
public class HttpServletContext {
	// 回车符
	public static final int CR = 13;
	// 换行符
	public static final int LF = 10;
	// 介质关系映射
	public static final Map<String, String> STATUS_MAPPING = new HashMap<String, String>();
	//保存状态码
	public static final Map<Integer, String> STATUS_CODE = new HashMap<Integer, String>();

	static {
		initStatusMapping();
		initStatusCode();
	}

	/**
	 * 初始化介质，读取并存储到map中
	 */
	private static void initStatusMapping() {
		SAXReader reader = new SAXReader();
		try {
			Document root = reader.read(HttpServletContext.class.getClassLoader().getResourceAsStream("web.xml"));
			List<Element> list = root.getRootElement().elements("mime-mapping");
			for (Element element : list) {
				STATUS_MAPPING.put(element.elementText("extension"), element.elementText("mime-type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存状态码和对应状态的信息
	 */
	private static void initStatusCode() {
		SAXReader reader = new SAXReader();
		try {
			Document root = reader
					.read(HttpServletContext.class.getClassLoader().getResourceAsStream("status_code.xml"));
			List<Element> list = root.getRootElement().elements("status");
			for (Element element : list) {
				STATUS_CODE.put(Integer.parseInt(element.elementText("status-code")),
						element.elementText("status-mapping"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过文件类型获取对应http
	 * @param statusMapping
	 * @return
	 */
	public static String getStatusMapping(String statusMapping) {
		return STATUS_MAPPING.get(statusMapping);
	}

	/**
	 * 通过状态码获取对应的代码
	 * @param statusCode
	 * @return
	 */
	public static String getStatusCode(int statusCode) {
		return STATUS_CODE.get(statusCode);
	}
}
