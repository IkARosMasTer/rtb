package com.yanhua.rtb.util;

import org.llmin.core.util.Util;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;

public class UniqueId {
	
	/**
	 * UUID生成函数
	 */
	public static String getId() {
		// 128位UUID的高64位
		long hsb = 0;
		// 128位UUID的低64位
		long lsb = 0;
		// UUID转换动态字符串
		StringBuffer sb = new StringBuffer();

		byte[] randomBytes = new byte[16];

		// 加密的强随机数生成类(param1:算法名称,param2:算法存在的包名)
		SecureRandom secureRandom;
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
			// 用随机字节填充byte数组
			secureRandom.nextBytes(randomBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException();
		} catch (NoSuchProviderException e) {
			throw new RuntimeException();
		}
		// 将byte数组元素十六进制化并逐步移动位数，直到充满高64位
		for (int i = 0; i < 8; i++)
			hsb = (hsb << 8) | (randomBytes[i] & 0xff);
		// 将byte数组元素十六进制化并逐步移动位数，直到充满低64位
		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (randomBytes[i] & 0xff);

		sb.append(format(hsb >> 32, 8));
		sb.append(format(hsb >> 16, 4));
		sb.append(format(hsb, 4));
		return sb.toString();
	}
	
	public static String getId8() {
		// 128位UUID的高64位
		long hsb = 0;
		// 128位UUID的低64位
		long lsb = 0;
		// UUID转换动态字符串
		StringBuffer sb = new StringBuffer();

		byte[] randomBytes = new byte[16];

		// 加密的强随机数生成类(param1:算法名称,param2:算法存在的包名)
		SecureRandom secureRandom;
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
			// 用随机字节填充byte数组
			secureRandom.nextBytes(randomBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException();
		} catch (NoSuchProviderException e) {
			throw new RuntimeException();
		}
		// 将byte数组元素十六进制化并逐步移动位数，直到充满高64位
		for (int i = 0; i < 8; i++)
			hsb = (hsb << 8) | (randomBytes[i] & 0xff);
		// 将byte数组元素十六进制化并逐步移动位数，直到充满低64位
		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (randomBytes[i] & 0xff);

		sb.append(format(hsb >> 32, 8));
		return sb.toString();
	}
	
	public static String getId16() {
		// 128位UUID的高64位
		long hsb = 0;
		// 128位UUID的低64位
		long lsb = 0;
		// UUID转换动态字符串
		StringBuffer sb = new StringBuffer();

		byte[] randomBytes = new byte[16];

		// 加密的强随机数生成类(param1:算法名称,param2:算法存在的包名)
		SecureRandom secureRandom;
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
			// 用随机字节填充byte数组
			secureRandom.nextBytes(randomBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException();
		} catch (NoSuchProviderException e) {
			throw new RuntimeException();
		}
		// 将byte数组元素十六进制化并逐步移动位数，直到充满高64位
		for (int i = 0; i < 8; i++)
			hsb = (hsb << 8) | (randomBytes[i] & 0xff);
		// 将byte数组元素十六进制化并逐步移动位数，直到充满低64位
		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (randomBytes[i] & 0xff);
		
		sb.append(format(hsb >> 32, 8));
		sb.append(format(hsb >> 16, 4));
		sb.append(format(hsb, 4));
		return sb.toString();
	}
	
	public static String getId32() {
		// 128位UUID的高64位
		long hsb = 0;
		// 128位UUID的低64位
		long lsb = 0;
		// UUID转换动态字符串
		StringBuffer sb = new StringBuffer();

		byte[] randomBytes = new byte[16];

		// 加密的强随机数生成类(param1:算法名称,param2:算法存在的包名)
		SecureRandom secureRandom;
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
			// 用随机字节填充byte数组
			secureRandom.nextBytes(randomBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException();
		} catch (NoSuchProviderException e) {
			throw new RuntimeException();
		}
		// 将byte数组元素十六进制化并逐步移动位数，直到充满高64位
		for (int i = 0; i < 8; i++)
			hsb = (hsb << 8) | (randomBytes[i] & 0xff);
		// 将byte数组元素十六进制化并逐步移动位数，直到充满低64位
		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (randomBytes[i] & 0xff);

		sb.append(format(hsb >> 32, 8));
		sb.append(format(hsb >> 16, 4));
		sb.append(format(hsb, 4));
		sb.append(format(lsb >> 48, 4));
		sb.append(format(lsb, 12));
		return sb.toString();
	}

	/**
	 * UUID生成函数
	 * @param bit 生成UUID位数(32位,64位,128位)
	 * @param uuidIF 加入UUID的附加条件
	 * @param separator 分隔UUID标准位数之间的分隔符
	 * @return
	 */
	public static String getId(int bit, String uuidIF, String separator) {
		// 128位UUID的高64位
		long hsb = 0;
		// 128位UUID的低64位
		long lsb = 0;
		// UUID转换动态字符串
		StringBuffer sb = new StringBuffer();

		byte[] randomBytes = new byte[16];

		// 加密的强随机数生成类(param1:算法名称,param2:算法存在的包名)
		SecureRandom secureRandom;
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
			// 用随机字节填充byte数组
			secureRandom.nextBytes(randomBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException();
		} catch (NoSuchProviderException e) {
			throw new RuntimeException();
		}
		// 将byte数组元素十六进制化并逐步移动位数，直到充满高64位
		for (int i = 0; i < 8; i++)
			hsb = (hsb << 8) | (randomBytes[i] & 0xff);
		// 将byte数组元素十六进制化并逐步移动位数，直到充满低64位
		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (randomBytes[i] & 0xff);

		if (bit == 32) {
			if (!isNull(uuidIF)) {
				sb.append(uuidIF);
				sb.append(separator);
			}
			sb.append(format(hsb >> 32, 8));
		} else if (bit == 64) {
			if (!isNull(uuidIF)) {
				sb.append(uuidIF);
				sb.append(separator);
			}
			sb.append(format(hsb >> 32, 8));
			if (!isNull(separator)) {
				sb.append(separator);
			}
			sb.append(format(hsb >> 16, 4));
			if (!isNull(separator)) {
				sb.append(separator);
			}
			sb.append(format(hsb, 4));
		} else if (bit == 128) {
			if (!isNull(uuidIF)) {
				sb.append(uuidIF);
				sb.append(separator);
			}
			sb.append(format(hsb >> 32, 8));
			if (!isNull(separator)) {
				sb.append(separator);
			}
			sb.append(format(hsb >> 16, 4));
			if (!isNull(separator)) {
				sb.append(separator);
			}
			sb.append(format(hsb, 4));
			if (!isNull(separator)) {
				sb.append(separator);
			}
			sb.append(format(lsb >> 48, 4));
			if (!isNull(separator)) {
				sb.append(separator);
			}
			sb.append(format(lsb, 12));
		}
		return sb.toString();
	}
	
	public static String getIdByDay() {
		return Util.getDateTime(new Date(), "yyyyMMdd").concat(UniqueId.getId8());
	}
	
	public static String getYMdHmsSId() {
		return Util.getDateTime(new Date(), "yyyyMMddHHmmssSSS").concat(UniqueId.getId8());
	}
	public static String getHmsSId() {
		return Util.getDateTime(new Date(), "HHmmssSSS").concat(UniqueId.getId8());
	}

	/**
	 * UUID生成函数
	 * @return
	 */
	public static long getIdLong() {
		long hsb = 0;
		long lsb = 0;
		byte[] randomBytes = new byte[16];
		long result = 0;
		// 加密的强随机数生成类(param1:算法名称,param2:算法存在的包名)
		SecureRandom secureRandom;
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
			// 用随机字节填充byte数组
			secureRandom.nextBytes(randomBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException();
		} catch (NoSuchProviderException e) {
			throw new RuntimeException();
		}

		// 将byte数组元素十六进制化并逐步移动位数，直到充满高64位
		for (int i = 0; i < 8; i++)
			hsb = (hsb << 8) | (randomBytes[i] & 0xff);
		// 将byte数组元素十六进制化并逐步移动位数，直到充满低64位
		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (randomBytes[i] & 0xff);

		result = formatLong(hsb >> 32, 8);
		result |= formatLong(hsb >> 16, 4);
		result |= formatLong(hsb, 4);
		result |= formatLong(lsb >> 48, 4);
		result |= formatLong(lsb, 12);		
		return result;
	}

	// 生成经过位数格式化的无符号long值的字符串
	private static String format(long val, int digits) {
		long hi = 1L << (digits * 4);
		return Long.toHexString(hi | (val & (hi - 1))).substring(1);
	}

	// 生成经过位数格式化的无符号long值
	private static long formatLong(long val, int digits) {
		long hi = 1L << (digits * 4);
		return (hi | (val & (hi - 1)));
	}

	private static boolean isNull(String val) {
		return val == null || val.equals("");
	}
	
	public static Long getRandomId19(){
		return Long.parseLong(System.currentTimeMillis()+""+(int)((Math.random()*9+1)*100000));
	}
	//
	// // 生成指定长度的随机串
	// private String getRandStr(Integer length, String s) {
	// String str = "";
	// while (str.length() != length) {
	// str = s.substring(2, 2 + length);
	// }
	// return str;
	// }	
	
	public static void main(String[] arg) {
		HashMap<String,String> hp = new HashMap<String,String>();
		long t = System.currentTimeMillis();
		for (int i = 0; i < 10000; i ++) {
			UniqueId.getId8();
			//String key = UniqueId.getUuid();
			//System.out.println(UniqueId.getUuid());
			hp.put(UniqueId.getId8(), "");
		}
		// System.out.println(System.currentTimeMillis()-t);
		System.out.println(hp.size());
		System.out.println(System.currentTimeMillis()-t);

		///*
		System.out.println(UniqueId.getId8());
		System.out.println(UniqueId.getId16());
		System.out.println(UniqueId.getId32());
		System.out.println(UniqueId.getId(32,"",""));
		System.out.println(UniqueId.getId(32,"201007201111",""));
		System.out.println(UniqueId.getId(64,"2008","_"));
		System.out.println(UniqueId.getId(128,"2008","_"));
		System.out.println(UniqueId.getId(128,"","_"));
		System.out.println(UniqueId.getIdLong());
		System.out.println(UniqueId.getIdByDay());
		System.out.println(UniqueId.getId());
		//*/
	}
 
}
