package asia.chiase.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import asia.chiase.core.exception.CCException;

public class CCFileUtil{

	public static boolean deleteFile(Integer folderId, String path, String storeName) throws Exception{
		try{
			File filePath = new File(path + File.separator + CCStringUtil.toString(folderId));
			if(filePath.isDirectory()){
				File fileDb = new File(filePath.getPath() + File.separator + storeName);
				if(fileDb.isFile()){
					fileDb.delete();
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		return true;

	}

	/**
	 * <strong>writeFileByText</strong><br>
	 * <br>
	 * 
	 * @param fileName
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static boolean writeFileByText(String fileName, String text) throws Exception{

		FileWriter filewriter = null;
		try{
			if(CCStringUtil.isEmpty(fileName)) return false;

			File file = new File(fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			filewriter = new FileWriter(file);

			filewriter.write(text);
			filewriter.flush();

		}catch(IOException e){
			e.printStackTrace();
			throw e;
		}finally{
			if(filewriter != null) filewriter.close();
		}

		return true;
	}

	/**
	 * <strong>writeFile</strong><br>
	 * <br>
	 * 
	 * @param path
	 * @param storeName
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static boolean writeFile(String storePath, String storeName, File file) throws Exception{

		if(file == null || CCStringUtil.isEmpty(storePath) || CCStringUtil.isEmpty(storeName)) return false;

		// read
		byte[] readBinary = new byte[(int)file.length()];
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try{

			// open byte stream
			fis = new FileInputStream(file);
			// read to buffer
			bis = new BufferedInputStream(fis);
			// set binary array
			bis.read(readBinary);

		}catch(FileNotFoundException e){
			e.printStackTrace();
			throw e;
		}catch(IOException e){
			e.printStackTrace();
			throw e;
		}finally{
			if(fis != null) fis.close();
			if(bis != null) bis.close();
		}

		// write
		File writeFile = new File(storePath + File.separator + storeName);
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try{
			fos = new FileOutputStream(writeFile);
			bos = new BufferedOutputStream(fos);

			// write
			bos.write(readBinary);
			bos.flush();

		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}finally{
			if(bos != null) bos.close();
			if(fos != null) fos.close();
		}

		return true;
	}

	/**
	 * <strong>convertFile2Bytes</strong><br>
	 * <br>
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] convertFile2Bytes(File file){
		if(file == null){
			return null;
		}
		try{
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			for(int readNum; (readNum = fis.read(buf)) != -1;){
				bos.write(buf, 0, readNum); // no doubt here is 0
			}
			byte[] bytes = bos.toByteArray();
			fis.close();
			return bytes;
		}catch(IOException ex){
			new CCException(ex);
			return null;
		}finally{
			file.delete();
		}
	}

	public static String caculateFileSize(Integer fileSize){
		String size = "";
		if((fileSize / 1024) < 1024){
			size = CCStringUtil.toString((fileSize / 1024)) + " KB";
		}else{
			size = CCStringUtil.toString((fileSize / (1024 * 1024))) + " MB";
		}
		return size;
	}
}