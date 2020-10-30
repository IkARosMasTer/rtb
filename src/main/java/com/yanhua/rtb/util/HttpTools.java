package com.yanhua.rtb.util;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpTools {


	public static String doPost(String url, String body) {
		HttpClient client = new HttpClient();
		String rtnString = null;
		PostMethod post = new PostMethod(url);
		try {
//			System.out.println(body);
			RequestEntity re = new StringRequestEntity(body, "application/json", "utf-8");
			post.setRequestEntity(re);
			client.getHttpConnectionManager().getParams().setSoTimeout(50000);
			client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);
			client.executeMethod(post);
			rtnString = post.getResponseBodyAsString();
//			System.out.println("Access System authenticate, Status: " + post.getStatusCode());
//			System.out.println("rtnString : " + rtnString);
			if ("Error processing request".equals(rtnString)) {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 调用异常, 返回异常报文
		} finally {
			post.releaseConnection();
		}
		return rtnString;
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	public static void main(String[] args) {
//		for(int i=0;i<10;i++){
//			long begin = System.currentTimeMillis();
//			String jsonData = doHttpGet("http://s.video.qq.com/get_playsource?plat=2&type=4&data_type=3&video_type=6&plname=qq&otype=json&callback=_jsonp_2_023d&_t=1544614277713&id=81019&year=2018&month=1");
//			System.out.println(jsonData.substring(14,jsonData.length()-1));
//			System.out.println(JSONObject.parseObject(jsonData.substring(14,jsonData.length()-1)).getJSONObject("PlaylistItem"));
//			List<String> indexList = new ArrayList<>();//JSONArray.fromObject(JSONObject.parseObject(jsonData.substring(14,jsonData.length()-1)).getJSONObject("PlaylistItem").get("indexList"));
////			figures_list
//			List<Integer> indexList1 = new ArrayList<>();
//			indexList.add("2017");
//			indexList.add("2018");
//			indexList.add("2016");
//			Collections.sort(indexList);
//			System.out.println(indexList);
//			System.out.println("单用时【" + (System.currentTimeMillis() - begin));
//		}

		String s = doPost("http://192.168.1.113:8081/upd/cms.do", "");
		System.out.println(s);
	}

	public static String doHttpGet(String url) {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			GetMethod getMethod = new GetMethod(url);
//			getMethod.getParams().setParameter("data","{\"youCallbackName\":\"infoDataCallBack\",\"channelCode\":\"00210OX\",\"token\":\"8bf9adf62276420d9fa4c229d8f3adbd\",\"vrbtId\":\"699052T2491M\"}");
//			getMethod.addRequestHeader("Connection", "close");
			HttpClient client = new HttpClient();
			client.executeMethod(getMethod);
			client.getParams().setBooleanParameter( "http.protocol.expect-continue" , false );
			if (getMethod.getStatusCode() == 200) {
				is = getMethod.getResponseBodyAsStream();
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				StringBuilder sb = new StringBuilder();
				String result = null;
				while( (result = br.readLine()) != null){
					sb.append(result);
				}
				return sb.toString();
//				return getMethod.getResponseBodyAsString();
			}
			return null;

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null){
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(isr != null){
				try {
					isr.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**   
     * 发送xml请求到server端   
     * @param url xml请求数据地址   
     * @param xmlString 发送的xml数据流   
     * @return null发送失败，否则返回响应内容   
     */      
    public static String sendPostXml(String url,String xmlString){      
        //创建httpclient工具对象     
        HttpClient client = new HttpClient();      
        //创建post请求方法     
        PostMethod post = new PostMethod(url);      
        //设置请求超时时间     
        client.setConnectionTimeout(3000*1000);    
        String responseString = null;      
        try{      
            //设置请求头部类型     
            post.setRequestHeader("Content-Type","text/xml");    
            post.setRequestHeader("charset","utf-8");    
            //设置请求体，即xml文本内容，一种是直接获取xml内容字符串，一种是读取xml文件以流的形式     
            RequestEntity requestEntity = new StringRequestEntity(xmlString, "text/xml", "UTF-8");
            post.setRequestEntity(requestEntity);   
            int statusCode = client.executeMethod(post);     
            //只有请求成功200了，才做处理  
            if(statusCode == HttpStatus.SC_OK){      
                InputStream inputStream = post.getResponseBodyAsStream();  
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));  
                StringBuffer stringBuffer = new StringBuffer();  
                String str = "";  
                while ((str = br.readLine()) != null) {  
                    stringBuffer.append(str);  
                }  
                responseString = stringBuffer.toString();  
            }      
        }catch (Exception e) {   
            e.printStackTrace();      
        }finally{  
             post.releaseConnection();   
        }  
        return responseString;      
    }   
    
	/**
	 * 计算2个日期之间的差,返回天数
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static int daysDifference(String dateStr1, String dateStr2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dateStr1 = sdf.format(sdf2.parse(dateStr1));
			dateStr2 = sdf.format(sdf2.parse(dateStr2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int year1 = Integer.parseInt(dateStr1.substring(0, 4));
		int month1 = Integer.parseInt(dateStr1.substring(4, 6));
		int day1 = Integer.parseInt(dateStr1.substring(6, 8));
		int year2 = Integer.parseInt(dateStr2.substring(0, 4));
		int month2 = Integer.parseInt(dateStr2.substring(4, 6));
		int day2 = Integer.parseInt(dateStr2.substring(6, 8));
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.YEAR, year1);
		c1.set(Calendar.MONTH, month1 - 1);
		c1.set(Calendar.DAY_OF_MONTH, day1);
		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.YEAR, year2);
		c2.set(Calendar.MONTH, month2 - 1);
		c2.set(Calendar.DAY_OF_MONTH, day2);
		long mills = c1.getTimeInMillis() > c2.getTimeInMillis() ? c1.getTimeInMillis() - c2.getTimeInMillis()
				: c2.getTimeInMillis() - c1.getTimeInMillis();
		return (int) (mills / 1000 / 3600 / 24);
	}

	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	public static String getWeek(String sdate) {
		// 再转换为时间
		Date date = HttpTools.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// int hour=c.get(Calendar.DAY_OF_WEEK);
		// hour中存的就是星期几了，其范围 1~7
		// 1=星期日 7=星期六，其他类推
		return new SimpleDateFormat("EEEE").format(c.getTime());
	}

	/**
	 * 获取当前时间
	 */
	public static String getNewDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.format(c.getTime());
	}

	/**
	 * 获得当前日期后几天的日期
	 *
	 * @param
	 * @return
	 */
	public static String getAfterDate(int number) {
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		// 得到当前时间，+3天 HH:mm:ss
		rightNow.add(Calendar.DAY_OF_MONTH, number);
		// System.out.println(java.util.Calendar.DAY_OF_MONTH);
		// 如果是后退几天，就写 -天数 例如：
		// rightNow.add(java.util.Calendar.DAY_OF_MONTH, -3);
		// 进行时间转换
		String date = sim.format(rightNow.getTime());
		return date;
	}

	/**
	 * 获得星期的数字
	 */
	public static String getWeekNumber(String morrowDate) {
		String weekDate = HttpTools.getWeek(morrowDate);
		String s = new String(weekDate);
		String a[] = s.split("期");
		return a[1];
	}
	
	public static Map<String ,Object> obj2Map(Object obj){
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for(int i=0; i<fields.length; i++){
			Field field = fields[i];
			field.setAccessible(true);
			try {
				map.put(field.getName(), field.get(obj));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}


	public static boolean isHostConnectable(String host, int port) {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(host, port));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

//	public static void main(String[] args) throws IOException {
//		MamsCodecResTaskDetail d = new MamsCodecResTaskDetail();
//		d.setAssetSize("10000");
//		d.setStatus("24");
//		d.setItemId("11397");
//		d.setCodecId("1");
//		d.setAssetBitrate("");
//		
//		Map<String, Object> map = obj2Map(d);
//		System.out.println(new Gson().toJson(map));
//
//	}

}
