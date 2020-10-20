package com.yanhua.rtb.common;


import com.yanhua.rtb.util.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class Context {

	public static final String MAMS_PATH_NAME = "mams";
	public static String MAMS_VOD_PATH_NAME = "vod";
	public static String YANHUA = "YANHUA";
	public static String YANHUA_CHINESE = "YANHUA";
//	public static String MAMS_PATH = PropertyUtil.getProperty("MAMS_PATH");
//	public static final String LOCAL_MNT_PATH = Context.MAMS_PATH;
	public static final String FILE_SEP = "/";// System.getProperty("file.separator");
	public static String MAMS_XML_HEAD = "xml";
	public static String MAMS_MOVIE_PATH = "vod";
	public static String MAMS_IMG_PATH = "img";
	


}
