package com.demo.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {

	public static void main(String[] args) throws ParseException {
//		Date date=new Date();
//		System.out.println(date);
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String time = dateFormat.format(date);
//		System.out.println(time);
//		Date date2 = dateFormat.parse(time);
//		
//		System.out.println(date2);
		String string = "aassssssssss123456";
		System.out.println(string.matches("^[a-zA-Z]\\w{6,16}$"));
		System.out.println(string.matches("/^[a-zA-Z]\\w{6,16}$/"));
	}
}
