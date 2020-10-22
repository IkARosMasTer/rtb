package com.yanhua.rtb.util;

import com.yanhua.rtb.common.GlobalConstant;
import com.yanhua.rtb.common.EngineException;
import org.llmin.core.util.Util;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * 
 * @author yejunhui 2011-12-22
 */
public class FileUtil {

	public static String createNewFileName(String type) {
		String id = UniqueId.getYMdHmsSId();
		String yyyyMm = id.substring(0, 6);
		String dd = id.substring(6, 8);
		String hh = id.substring(8, 10);
		String mm = id.substring(10, 12);
		return GlobalConstant.FILE_SEP +
				type +
				GlobalConstant.FILE_SEP +
				yyyyMm +
				GlobalConstant.FILE_SEP +
				dd +
				GlobalConstant.FILE_SEP +
				hh +
				GlobalConstant.FILE_SEP +
				mm +
				GlobalConstant.FILE_SEP +
				id;
	}
	
	public static String createImgPath(String fileName) {		
		return createPath("","img",getFileSuffix(fileName));
	}	

	public static String createVodPath( String fileName) {
		return createPath("","vod",getFileSuffix(fileName));
	}
	
	public static String createTempPath(String fileSuffix) {
		String id = UniqueId.getYMdHmsSId();
		String yyyyMMdd = id.substring(0, 8);
		StringBuffer sb = new StringBuffer();
		sb.append(GlobalConstant.FILE_SEP);
		sb.append(GlobalConstant.MAMS_PATH_NAME);
		sb.append(GlobalConstant.FILE_SEP);
		sb.append(yyyyMMdd);
		sb.append(GlobalConstant.FILE_SEP);
		sb.append(id);
		sb.append(".");
		sb.append(fileSuffix);
		return sb.toString();
	}
	


	public static String createPath(String path, String type, String fileSuffix) {
		StringBuffer sb = new StringBuffer();
		sb.append(GlobalConstant.FILE_SEP);
		sb.append(Util.killNull(path));
		sb.append(createNewFileName(type));
		sb.append(".");
		sb.append(Util.killNull(fileSuffix).toLowerCase());
		return resetPath(sb.toString());
	}

	public static String getFileSuffix(String path) {
		int index = path.lastIndexOf(".");
		if (index > 0) {
			return path.substring(index + 1).trim().toLowerCase();
		}
		return "";
	}
	
	public static String getFilePath(String path) {
		int index = path.lastIndexOf(GlobalConstant.FILE_SEP);
		if (index > 0) {
			return path.substring(0, index);
		}
		return path;
	}
	
	public static String getFileName(String path) {
		int index = path.lastIndexOf(GlobalConstant.FILE_SEP);
		if (index > 0) {
			return path.substring(index + 1);
		}
		return "";
	}

	public static String resetPath(String path) {
		String returnPath = Util.killNull(path);
		returnPath = returnPath.replaceAll("\\\\+", GlobalConstant.FILE_SEP).replaceAll("\\\\+|/+", GlobalConstant.FILE_SEP);
		if (returnPath.length() > 1 && returnPath.endsWith(GlobalConstant.FILE_SEP) )
			return returnPath.substring(0,returnPath.length() - 1);
		return returnPath;
	}
	
	public static String resetPath(String path, boolean b) {
		String returnPath = Util.killNull(path);
		returnPath = returnPath.replaceAll("\\\\+", GlobalConstant.FILE_SEP).replaceAll("\\\\+|/+", GlobalConstant.FILE_SEP);
		if ( b && !returnPath.endsWith(GlobalConstant.FILE_SEP) ) {
			returnPath += GlobalConstant.FILE_SEP;
		}
		if ( !b && returnPath.endsWith(GlobalConstant.FILE_SEP) ) {
			if (returnPath.length() > 1 )
				returnPath = returnPath.substring(0,returnPath.length() - 1);
		}		
		return returnPath;
	}

	/**
	 * 生成文件包括内容
	 * 
	 * @param fileName
	 *            文件名称包括目录
	 * @param string
	 *            内容
	 * @return
	 * @throws EngineException
	 */
	public static void createFile(String fileName,String str) throws EngineException {
		FileOutputStream  fileOutputStream = null;
		try{
			createFile(fileName);
			fileOutputStream = new FileOutputStream(fileName);
			fileOutputStream.write(str.getBytes());
		} catch (EngineException e) {
			throw e;
		} catch (IOException e) {
			throw new EngineException("文件写入失败：" + fileName+ e.getMessage());
		} finally {
			Util.close(fileOutputStream);
		}
	}

	/**
	 * 根据流建立文件
	 * 
	 * @param fileName
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static void createFile(String fileName, InputStream inputStream)
			throws Exception {
		FileOutputStream fileOutputStream = null;
		try {
			createFile(fileName);
			fileOutputStream = new FileOutputStream(fileName);
			byte[] buffer = new byte[10240];
			int n_bytes;
			while ((n_bytes = inputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, n_bytes);
				fileOutputStream.flush();
			}
		} catch (EngineException e) {
			throw e;
		} catch (IOException e) {
			throw new EngineException("文件写入失败：" + fileName+e.getMessage());
		} finally {
			Util.close(fileOutputStream);
		}
	}	
	
	/**
	 * 生成文件包括内容
	 * @throws EngineException
	 */
	public static void createFile(String fileName, String str, String character) throws EngineException {
		FileOutputStream fileOutputStream = null;
		BufferedWriter writer = null;
		OutputStreamWriter write = null;
		try {
			createFile(fileName);
			fileOutputStream = new FileOutputStream(fileName);
			write = new OutputStreamWriter(fileOutputStream, character);
			writer = new BufferedWriter(write);
			writer.write(str);
		} catch (EngineException e) {
			throw e;
		} catch (IOException e) {
			throw new EngineException("文件写入失败：" + fileName+ e.getMessage());
		} finally {
			Util.close(writer);
			Util.close(write);
			Util.close(fileOutputStream);
		}
	}
	
	public static void createDir(String fileName) {
		File file = new File(fileName);
		// 判断目录是否存在
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
	}	

	/**
	 * 读取文件名列表
	 * 
	 * @param path
	 * @return
	 * @throws EngineException
	 */
	public static List<String> fileNameList(String path) throws EngineException {
		List<String> list = new ArrayList<String>();
		File file = new File(path);
		if (file == null || !file.exists())
			throw new EngineException("文件路径(" + path + ")不存在!");
		for (File f : file.listFiles())
			list.add(f.getName());
		return list;
	}

	/**
	 * 复制文件
	 * 
	 * @param sourceFileName
	 * @param destinationFileName
	 * @return
	 * @throws EngineException
	 */
	public static void copyFile(String sourceFileName,
			String destinationFileName) throws EngineException {
		if (Util.isNull(sourceFileName)) {
			throw new EngineException("请输入源文件地址");
		}
		if (Util.isNull(destinationFileName)) {
			throw new EngineException("请输入目标文件地址");
		}
		File sourceFile = new File(sourceFileName);
		File destinationFile = new File(destinationFileName);

		// 判断源文件是否存在
		if (!sourceFile.exists()) {
			throw new EngineException("源文件不存在：" + sourceFileName);
		}

		// 创建目标文件
		createFile(destinationFileName);

		copyFile(sourceFile, destinationFile);
	}

	/**
	 * 复制文件
	 * 
	 * @param sourceFile
	 * @param destinationFile
	 * @return
	 * @throws EngineException
	 */
	private static void copyFile(File sourceFile, File destinationFile) throws EngineException {
		FileInputStream fileInputStream = null;
		DataInputStream dataInputStream = null;
		FileOutputStream fileOutputStream = null;
		DataOutputStream dataOutputStream = null;
		try {
			fileInputStream = new FileInputStream(sourceFile);
			dataInputStream = new DataInputStream(new BufferedInputStream(
					fileInputStream));
			fileOutputStream = new FileOutputStream(destinationFile);
			dataOutputStream = new DataOutputStream(new BufferedOutputStream(
					fileOutputStream));
			byte[] b = new byte[10240];
			int len;
			while ((len = dataInputStream.read(b)) != -1) {
				dataOutputStream.write(b, 0, len);
				dataOutputStream.flush();
			}			
		} catch (IOException e) {
			//throw new WasuException("复制文件失败！", e);
		} finally {
			Util.close(fileInputStream);
			Util.close(dataInputStream);
			Util.close(fileOutputStream);
			Util.close(dataOutputStream);
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param fileName
	 * @return
	 * @throws EngineException
	 */
	public static void createFile(String fileName) throws EngineException {
		if (Util.isNull(fileName)) {
			throw new EngineException("请输入源文件地址");
		}
		File file = new File(fileName);
		// 判断文件是否存在
		if (file.exists()) {
			return;
		}
		// 判断目录是否存在
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}	

		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new EngineException("创建新文件失败"+e.getMessage());
		}
	}

	public static void createPath(String fileName) throws EngineException {
		if (Util.isNull(fileName)) {
			throw new EngineException("请输入源文件地址");
		}
		File file = new File(fileName);
		// 判断目录是否存在
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 * @return
	 * @throws EngineException
	 */
	public static void deleteFile(String fileName) throws EngineException {
		if (Util.isNull(fileName)) {
			return;
		}
		File file = new File(fileName);
		if (file.exists())
			file.delete();
	}
	
	public static String fileMD5(String inputFile) throws IOException {
	      int bufferSize = 1024 * 1024;
	      FileInputStream fileInputStream = null;
	      DigestInputStream digestInputStream = null;
	      try {
	         MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	         fileInputStream = new FileInputStream(inputFile);
	         digestInputStream = new DigestInputStream(fileInputStream,messageDigest);
	         byte[] buffer = new byte[bufferSize];
	         while (digestInputStream.read(buffer) > 0);
	         messageDigest= digestInputStream.getMessageDigest();
	         byte[] resultByteArray = messageDigest.digest();
	         return byteArrayToHex(resultByteArray);
	      } catch (NoSuchAlgorithmException e) {
	         return null;
	      } finally {
	         try {
	            digestInputStream.close();
	         } catch (Exception e) {
	         }
	         try {
	            fileInputStream.close();
	         } catch (Exception e) {
	         }
	      }
	}
	
	public static String byteArrayToHex(byte[] byteArray) {
	      char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
	      char[] resultCharArray =new char[byteArray.length * 2];
	      int index = 0;
	      for (byte b : byteArray) {
	         resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
	         resultCharArray[index++] = hexDigits[b& 0xf];
	      }
	      return new String(resultCharArray);
	}

	public static void main(String[] arg) {
	}
}