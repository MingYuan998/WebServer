package com.demo.context;
/**
 * url和类之间的映射关系配置文件
 * @author 贾小东东
 *
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ServletContext {

	private static Map<String, String> servletName= new HashMap<String, String>();
	
	static {
		initServletMapping();
	}
	/**
	 * 读取配置文件
	 */
	private static void initServletMapping() {
		SAXReader reader = new SAXReader();
		try {
			Document root = reader.read(HttpServletContext.class.getClassLoader().getResourceAsStream("servlets.xml"));
			List<Element> list = root.getRootElement().elements("servlet");
			for (Element element : list) {
				servletName.put(element.attributeValue("url"), element.attributeValue("class"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 通过url获取servletName
	 * @param url
	 * @return
	 */
	public static String getServletNameByUrl(String url) {
		return servletName.get(url);
	}
}
