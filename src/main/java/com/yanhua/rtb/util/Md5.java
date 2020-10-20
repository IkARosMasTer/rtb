package com.yanhua.rtb.util;

import lombok.extern.slf4j.Slf4j;


import java.security.MessageDigest;

/**
 * @author 49468
 */
@Slf4j
public class Md5 {
	public static String crypt(String str) {
		String aaa = "";
		try {
			if(str == null || str.replaceAll(" ", "").length() == 0){
				return null;
			}
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] digestArr = md.digest();
			aaa = byte2hex(digestArr);
		} catch (Exception e) {
			log.info("密码加密失败!",e);
		}
		return aaa;
	}

	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (byte value : b) {
			stmp = (Integer.toHexString(value & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	public static void main(String[] args) {
		String ff = Md5.crypt("appkey3000007497timestamp20201012182700B26795DB8A716BB7");
		ff = ff.toUpperCase();
		System.out.println(ff);
	}
}