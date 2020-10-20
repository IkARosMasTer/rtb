package com.yanhua.rtb.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
	
	public static String getProperty(String key){
		InputStream is = null;
		try {
			is = PropertyUtil.class.getResourceAsStream("/config/system.properties");
			Properties prop = new Properties();
			prop.load(is);
			return prop.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(PropertyUtil.getProperty("UT_URL"));
	}

}
