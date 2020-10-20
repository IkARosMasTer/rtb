package com.yanhua.rtb.util;

import com.yanhua.rtb.common.EngineException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 程序常用处理方法
 * 
 * @author 李良敏
 * 
 */
public class MamsUtil {
	
	public final static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);  
	
	public static Object getBean(ServletContext context, String bean)
			throws InstantiationException, IllegalAccessException {
		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);
		return (Object) ctx.getBean(bean);
	}

	public static String lpad(String str, int length, String c) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(c);
		}
		sb.append(str == null ? "" : str.trim());
		return sb.substring(sb.length() - length);
	}

	public static String lpad(Integer i, int length, String c) {
		return lpad(String.valueOf(i), length, c);
	}

	public static String lpad0(String str, int length) {
		StringBuffer sb = new StringBuffer("0000000000000000");
		sb.append(str == null ? "" : str.trim());
		return sb.substring(sb.length() - length);
	}

	public static String lpad0(Integer i, int length) {
		return lpad0(String.valueOf(i), length);
	}

	public static String getSign(String str) {
		if (isNull(str)) {
			return "00";
		}
		int i = Integer.valueOf(str);
		return lpad0(++i, 2);
	}

	public static String rpad(String str, int length, String c) {
		StringBuffer sb = new StringBuffer(str == null ? "" : str.trim());
		for (int i = 0; i < length; i++) {
			sb.append(c);
		}
		return sb.substring(0, length);
	}
	
	public static Object[] toStringArray( String paras ) {
		if ( MamsUtil.isNull( paras ) ) {
			return null;
		}
		return paras.split(",");		
	}
	
	public static Object[] toIntArray( String paras ) {
		if ( MamsUtil.isNull( paras ) ) {
			return null;
		}
		String[] strArray = paras.split(",");	
		int length = strArray.length;
		Integer[] intArray = new Integer[length];
		for ( int i = 0; i < length; i ++ ) {
			intArray[i] = Integer.valueOf(strArray[i].trim());
		}
		return intArray;		
	}
	
	public static String getClientToken(String flag) {
		return Md5.crypt(killNull(flag)+"__WASU__");
	}
	
	public static String killNull(String o) {
		return o == null ? "" : o.trim();
	}

	/**
	 * 去掉空格或空格
	 * 
	 * @param o
	 * @return
	 */
	public static String killNull(Object o) {
		return o == null ? "" : String.valueOf(o).trim();
	}
	
	public static boolean isNull(String o) {
		if (o == null) {
			return true;
		}
		return "".equals( o.trim() );
	}

	/**
	 * 检查是否为空、空字符、空格
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isNull(Object o) {
		if (o == null) {
			return true;
		}
		if (o instanceof String) {
			return "".equals(((String) o).trim());
		}
		return false;
	}
	
	public static boolean isNotNull(String o) {
		// this.page.setId("");
		return !isNull(o);
	}

	/**
	 * 与isNull的非值
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isNotNull(Object o) {
		// this.page.setId("");
		return !isNull(o);
	}	
	

	/**
	 * 获取当前的日期
	 * 
	 * @return
	 */
	public static Date getCurrentDate() {
		return new Date();
	}

	/**
	 * 转化当天的日期格式
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateTime(Date date, String format) {
		SimpleDateFormat theDate = new SimpleDateFormat(format);
		return theDate.format(date);
	}
	
	/**
	 * 转化当天的日期格式
	 * @param format yyyyMMddHHmmssSSS
	 * @return
	 */
	public static String getDateTime(String format) {
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat theDate = new SimpleDateFormat(format);
		return theDate.format(cal.getTime());
	}
	
	public static List<String> getCmd(String para, String ...strings) {
		List<String> commend = new ArrayList<String>();
		int i = 0;
		StringTokenizer st = new StringTokenizer(para);
		String s = null;
	    while (st.hasMoreTokens()) {
	    	s = st.nextToken();
	    	if ( s.trim().startsWith("?") ) {
				commend.add( strings[i] );
				i ++;
			} else {
				commend.add(s);
			}
	    }
		return commend;
	}
	
	/**
	 * 
	 * @param cmd
	 * @param strings
	 * @return 0 表示成功! 其他表示失败
	 * @throws EngineException
	 */
	public static int executeCmd(String cmd, String ...strings ) throws EngineException {
		Process process = null;
		try {
			// Process process = Runtime.getRuntime().exec(cmd);
			ProcessBuilder builder = new ProcessBuilder();
			builder.command( getCmd(cmd, strings) );
			// 如果此属性为 true，则任何由通过此对象的 start() 方法启动的后续子进程生成的错误输出都将与标准输出合并，因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
			builder.redirectErrorStream(true);
			process = builder.start();
			// 很容易出现主线程阻塞，Process也挂起的情况。在调用waitFor() 的时候，Process需要向主线程汇报运行状况，所以要注意清空缓存区，即InputStream和ErrorStream
			/*
			final InputStream is = process.getInputStream();   
			启动单独的线程来清空process.getInputStream()的缓冲区			
			new Thread(
					new Runnable() {               
							public void run() {                   
								flushProcessStream(is);            
							}         
						}).start();
						*/
			flushProcessStream( process.getInputStream() );
			process.waitFor();	
			return process.exitValue();
		} catch (InterruptedException e) {
			throw new EngineException("命令被中断:" + e.getMessage());
		} catch (IOException e) {
			throw new EngineException("命令无法调用:" + e.getMessage());
		} finally {
			if (process != null) {
				close(process.getOutputStream());
				close(process.getInputStream());
				close(process.getErrorStream());
				process.destroy();
			}
		}
	}
	
	/**
	 * ◦0 表示命令执行成功 
			◦1 表示命令语法错误 
			◦2 表示命令执行错误 
	 * @param cmd
	 * @return
	 * @throws EngineException
	 */
	public static int executeCmd(List<String> cmd) throws EngineException {
		Process process = null;
		try {
			// Process process = Runtime.getRuntime().exec(cmd);
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(cmd);
			// 如果此属性为 true，则任何由通过此对象的 start() 方法启动的后续子进程生成的错误输出都将与标准输出合并，因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
			builder.redirectErrorStream(true);
			process = builder.start();
			// 很容易出现主线程阻塞，Process也挂起的情况。在调用waitFor() 的时候，Process需要向主线程汇报运行状况，所以要注意清空缓存区，即InputStream和ErrorStream
			/*
			final InputStream is = process.getInputStream();   
			启动单独的线程来清空process.getInputStream()的缓冲区			
			new Thread(
					new Runnable() {               
							public void run() {                   
								flushProcessStream(is);            
							}         
						}).start();
						*/
			flushProcessStream( process.getInputStream() );
			process.waitFor();	
			return process.exitValue();
		} catch (InterruptedException e) {
			throw new EngineException("命令被中断:" + e.getMessage());
		} catch (IOException e) {
			throw new EngineException("命令无法调用:" + e.getMessage());
		} finally {
			if (process != null) {
				close(process.getOutputStream());
				close(process.getInputStream());
				close(process.getErrorStream());
				process.destroy();
			}
		}
	}
	
	public static void flushProcessStream(InputStream inputStream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		try {
			while (reader.readLine() != null);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void close(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException e) {
				// ignored
				e.printStackTrace();
			}
		}
	}

	public static void createDir(String fileName) {
		File file = new File(fileName);
		// 判断目录是否存在
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
	}

	public static void printExeInfo(Process process) throws IOException {
		InputStream inputStream = process.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		String line;
		StringBuffer buffer = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			System.out.println("INFO:" + line);
			buffer.append(line);
		}
		inputStream.close();
		reader.close();
		System.out.println("INFO:" + buffer.toString());
	}

	public static void printError(Process process) throws IOException {
		InputStream inputStream = process.getErrorStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		String line;
		StringBuffer buffer = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		inputStream.close();
		reader.close();
		System.out.println("ERROR:" + buffer.toString());
	}
	
	public static String getAfterDateTime(int hour) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.add(Calendar.HOUR, hour);
		SimpleDateFormat theDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return theDate.format(gregorianCalendar.getTime());
	}
	
	public static Date getAfterHour(int hour) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.add(Calendar.HOUR, hour);
		return gregorianCalendar.getTime();
	}
	
	public static Date getAfterHour(Date date, int hour) {	
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.add(Calendar.HOUR, hour);
		return gregorianCalendar.getTime();
	}
	
	public static Date convertString2Date(String datetime, String format) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.parse(datetime);
	}
	
	public static String createXmlDeclaration() {
		return createXmlDeclaration("1.0","UTF-8");		
	}
	
	public static String createXmlDeclaration(String version, String encoding) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"");
		sb.append(version);
		sb.append("\" encoding=\"");
		sb.append(encoding);
		sb.append("\"?>");
		return sb.toString();		
	}
	
	public static String createXmlElement(String tag, Object value) {
		StringBuffer sb = new StringBuffer();
		sb.append("<");
		sb.append(tag);
		if ( MamsUtil.isNull(value) ) {
			sb.append("/>");
			return sb.toString();	
		}
		sb.append(">");
		sb.append(MamsUtil.killNull(value));
		sb.append("</");
		sb.append(tag);
		sb.append(">");
		return sb.toString();		
	}
	
	public static String createXmlElementCDATA(String tag, Object value) {
		StringBuffer sb = new StringBuffer();
		sb.append("<");
		sb.append(tag);
		if ( MamsUtil.isNull(value) ) {
			sb.append("/>");
			return sb.toString();	
		}
		sb.append(">");
		sb.append("<![CDATA[");
		sb.append(MamsUtil.killNull(value));
		sb.append("]]>");
		sb.append("</");
		sb.append(tag);
		sb.append(">");
		return sb.toString();		
	}
	
	public static String createXmlElementAttribute(String tag, Object value, String[] attrs, Object[] attrsVal ) {
		StringBuffer sb = new StringBuffer();
		sb.append("<");
		sb.append(tag);
		if ( attrs != null ) {
			int length = attrs.length;
			for ( int i = 0; i < length; i ++ ) {
				sb.append(" ");
				sb.append(attrs[i]);
				sb.append("=\"");
				sb.append( MamsUtil.killNull( attrsVal[i] ) );
				sb.append("\"");
			}		
		}
		if ( MamsUtil.isNull(value) ) {
			sb.append("/>");
			return sb.toString();	
		}
		sb.append(">");
		//sb.append("<![CDATA[");
		sb.append(MamsUtil.killNull(value));
		//sb.append("]]>");
		sb.append("</");
		sb.append(tag);
		sb.append(">");
		return sb.toString();		
	}
	
	public static String createXmlElementAttributeCDATA(String tag, Object value, String[] attrs, Object[] attrsVal ) {
		StringBuffer sb = new StringBuffer();
		sb.append("<");
		sb.append(tag);
		if ( attrs != null ) {
			int length = attrs.length;
			for ( int i = 0; i < length; i ++ ) {
				sb.append(" ");
				sb.append(attrs[i]);
				sb.append("=\"");
				sb.append( MamsUtil.killNull( attrsVal[i] ) );
				sb.append("\"");
			}		
		}
		if ( MamsUtil.isNull(value) ) {
			sb.append("/>");
			return sb.toString();	
		}
		sb.append(">");
		sb.append("<![CDATA[");
		sb.append(MamsUtil.killNull(value));
		sb.append("]]>");
		sb.append("</");
		sb.append(tag);
		sb.append(">");
		return sb.toString();		
	}
	
	public static String createXmlElementAttribute(String tag, Object value, Map<String,Object> map  ) {
		StringBuffer sb = new StringBuffer();
		sb.append("<");
		sb.append(tag);
		if ( map != null ) {
			for ( Entry<String,Object> entry : map.entrySet() ) {
				sb.append(" ");
				sb.append(entry.getKey());
				sb.append("=\"");
				sb.append( MamsUtil.killNull( entry.getValue() ) );
				sb.append("\"");
			}		
		}
		if ( MamsUtil.isNull(value) ) {
			sb.append("/>");
			return sb.toString();	
		}
		sb.append(">");
		//sb.append("<![CDATA[");
		sb.append(MamsUtil.killNull(value));
		//sb.append("]]>");
		sb.append("</");
		sb.append(tag);
		sb.append(">");
		return sb.toString();		
	}
	
	public static String createXmlElementAttributeCDATA(String tag, Object value, Map<String,Object> map  ) {
		StringBuffer sb = new StringBuffer();
		sb.append("<");
		sb.append(tag);
		if ( map != null ) {
			for ( Entry<String,Object> entry : map.entrySet() ) {
				sb.append(" ");
				sb.append(entry.getKey());
				sb.append("=\"");
				sb.append( MamsUtil.killNull( entry.getValue() ) );
				sb.append("\"");
			}		
		}
		if ( MamsUtil.isNull(value) ) {
			sb.append("/>");
			return sb.toString();	
		}
		sb.append(">");
		sb.append("<![CDATA[");
		sb.append(MamsUtil.killNull(value));
		sb.append("]]>");
		sb.append("</");
		sb.append(tag);
		sb.append(">");
		return sb.toString();		
	}
	
	public static Timestamp getCurrentTimestamp(){
		return new Timestamp(System.currentTimeMillis());
	}
	
	public static Integer toInteger(String str){
		if(isNull(str)){
			return null;
		}
		return Integer.valueOf(str);
	}
	
	public static int time2Int(String datetime){
		int retTime = 0;
		int hh = 0;													//时
		int mm = 0;													//分
		int ss = 0;													//秒
		if(datetime==null  || "".equals(datetime)){
			return retTime;
		}
		hh = Integer.parseInt(datetime.substring(0,datetime.indexOf(":")));
		mm = Integer.parseInt(datetime.substring(datetime.indexOf(":")+1, datetime.lastIndexOf(":")));
		ss = Integer.parseInt(datetime.substring(datetime.lastIndexOf(":")+1));
		retTime = (3600*hh+60*mm+ss)*1000;

		return retTime;
	}

	@SuppressWarnings("null")
	public static String getLanguage(String language) {
		// 默认国语
		String result = "1";
		if (language != null || !language.equals("")) {
			if (language.equals("国语")) {
				result = "1";
			}
			if (language.equals("韩语")) {
				result = "2";
			}
			if (language.equals("英语")) {
				result = "3";
			}
			if (language.equals("日语")) {
				result = "4";
			}
			if (language.equals("原声")) {
				result = "5";
			}
		}
		return result;
	}
}