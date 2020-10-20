package com.yanhua.rtb.util;

//import com.ally.core.exception.AllyException;
//import com.ally.mams.Context;
//import com.ally.mams.ftp.FtpToolFactory;
import com.yanhua.rtb.common.EngineException;
import org.apache.commons.lang3.StringUtils;
//import org.zkoss.io.Files;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;

public class URLStreamTool {
	/**
	 * 得到输入流
	 * 
	 * @param urlPath
	 * @return
	 * @throws IOException
	 */
	public static InputStream getInputStream(String urlPath) throws IOException {
		return getURLConnection(urlPath, false).getInputStream();
	}

	/**
	 * 得到输出流
	 * 
	 * @param urlPath
	 * @return
	 * @throws IOException
	 */
	public static OutputStream getOutputStream(String urlPath) throws IOException {
		return getURLConnection(urlPath, true).getOutputStream();
	}

	public static URLConnection getURLConnection(String urlPath, boolean write) throws IOException {
		URL url = new URL(urlPath);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setDoOutput(write);
		// urlConnection.setDoInput( write );
		urlConnection.setConnectTimeout(60 * 1000); // 链接超时为1分钟
		urlConnection.setReadTimeout(120 * 1000); // 读数据超时为2分钟
		return urlConnection;
	}

	/**
	 * 将输入流转村为文件,可获取http,ftp等数据流
	 * 
	 * @param urlPath
	 * @param file
	 * @throws IOException
	 */
	public static void downStreamToFile(String urlPath, String file) throws IOException {
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			MamsUtil.createDir(file);
			URLConnection urlConnection = getURLConnection(urlPath, false);
			inputStream = urlConnection.getInputStream();
			fileOutputStream = new FileOutputStream(file);
			byte[] buffer = new byte[10240];
			int nBytes;
			while ((nBytes = inputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, nBytes);
				fileOutputStream.flush();
			}
		} finally {
			MamsUtil.close(inputStream);
			MamsUtil.close(fileOutputStream);
		}
	}

	public static void downStreamToFileNew(String remoteFile, String localFile) throws Exception {
		InputStream inputStream = null;
		try {
			MamsUtil.createDir(localFile);
			inputStream = new FileInputStream(remoteFile);

			File file = new File(localFile);
			Field[] field = File.class.getDeclaredFields();
			for (Field f : field) {
				if ("path".equals(f.getName())) {
					f.setAccessible(true);
					f.set(file, localFile);
					break;
				}
			}
//			Files.copy(file, inputStream);
		} finally {
			MamsUtil.close(inputStream);
		}
	}

	public static void downloadFile(String urlPath, String localFile) throws Exception {
		if (MamsUtil.isNull(urlPath) || MamsUtil.isNull(localFile)) {
			throw new Exception("源地址or本地文件地址为空!");
		}
		String temp = urlPath.trim().toLowerCase();
//		downStreamToFileNew(urlPath, localFile);
		downStreamToFile(urlPath,localFile);
	}

	/**
	 * 转码
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream is) throws IOException {
		BufferedInputStream buf = null;
		try {
			buf = new BufferedInputStream(is);
			StringBuffer info = new StringBuffer();
			byte[] buffer = new byte[10240];
			int iRead;
			while ((iRead = buf.read(buffer)) != -1) {
				info.append(new String(buffer, 0, iRead));
			}
			return info.toString();
		} finally {
			MamsUtil.close(buf);
		}
	}

	/**
	 * 上传内容,并获取返回的信息
	 * 
	 * @param urlPath
	 * @param content
	 *            需要上传的内容
	 * @return 返回的内容
	 * @throws IOException
	 */
	public static String executeUrl(String urlPath, String content) throws IOException {
		URLConnection urlConnection = getURLConnection(urlPath, true);
		OutputStream outputStream = null;
		ByteArrayInputStream bais = null;
		InputStream inputStream = null;
		try {
			outputStream = urlConnection.getOutputStream();
			bais = new ByteArrayInputStream(content.getBytes());
			byte[] buffer = new byte[10240];
			int n_bytes;
			while ((n_bytes = bais.read(buffer)) != -1) {
				outputStream.write(buffer, 0, n_bytes);
				outputStream.flush();
			}
			inputStream = urlConnection.getInputStream();
			// 转码为字符串
			return inputStream2String(inputStream);
		} finally {
			MamsUtil.close(outputStream);
			MamsUtil.close(inputStream);
			MamsUtil.close(bais);
		}
	}

	public static String executeUrl(String urlPath, InputStream inputStream) throws IOException {
		URLConnection urlConnection = getURLConnection(urlPath, true);
		OutputStream outputStream = null;
		ByteArrayInputStream bais = null;
		InputStream retInputStream = null;
		try {
			outputStream = urlConnection.getOutputStream();
			// bais = new ByteArrayInputStream(content.getBytes());
			byte[] buffer = new byte[10240];
			int n_bytes;
			while ((n_bytes = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, n_bytes);
				outputStream.flush();
			}
			retInputStream = urlConnection.getInputStream();
			// 转码为字符串
			return inputStream2String(retInputStream);
		} finally {
			MamsUtil.close(outputStream);
			MamsUtil.close(retInputStream);
			MamsUtil.close(bais);
		}
	}

	public static void main(String[] args) throws Exception {
		// FileInputStream is = new FileInputStream(new
		// File("E:\\temp\\mv1.xml"));
		// System.out.println(
		// URLStreamTool.executeUrl("http://125.210.243.40:8080/mrms/service/xmlResourceEnter",
		// is) );
		// is.close();
		URLStreamTool.downStreamToFileNew("E:\\tomcat\\tomcat1\\webapps\\mams\\WEB-INF\\lib\\jxl.jar",
				"E:\\test\\jxl.jar");
	}
}