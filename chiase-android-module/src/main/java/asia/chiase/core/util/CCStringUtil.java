package asia.chiase.core.util;

import java.util.Collection;
import java.util.Date;

import asia.chiase.core.exception.CCException;

/**
 * <strong>CSStringUtil</strong><br>
 * <br>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class CCStringUtil{

	public static void test(){

	}

	/**
	 * <strong>isEmpty</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isEmpty(Object data){

		if(data == null){
			return true;
		}

		if(data instanceof String){
			String target = (String)data;
			if(target.length() > 0){
				return false;
			}else{
				return true;
			}
		}

		if(data instanceof Collection){
			Collection<?> target = (Collection<?>)data;
			if(target.size() > 0){
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}

	}

	/**
	 * <strong>isNumber</strong><br>
	 * <br>
	 * checking a string whether is Integer or Double?
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isNumber(String number){
		if(CCStringUtil.isEmpty(number)){
			return false;
		}
		try{
			Integer.valueOf(number);
			return true;
		}catch(Exception e){
			try{
				Double.valueOf(number);
				return true;
			}catch(Exception e2){
				return false;
			}
		}
	}

	/**
	 * <strong>isInteger</strong><br>
	 * <br>
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isInteger(String number){
		if(CCStringUtil.isEmpty(number)){
			return false;
		}

		try{
			Integer.valueOf(number);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}

	/**
	 * <strong>toString</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static String toString(Object data){

		try{
			if(data == null){
				return "";
			}

			if(data instanceof Date){
				return CCFormatUtil.formatDate((Date)data);
			}

			return data.toString();
		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>toUpperCase</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static String toUpperCase(Object data){

		try{
			if(data == null){
				return "";
			}
			return data.toString().toUpperCase();
		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>checkNull</strong><br>
	 * <br>
	 * 
	 * @param str
	 * @return
	 */
	public static String checkNull(String str){
		if(str == null){
			return "";
		}else{
			return str;
		}
	}

	/**
	 * <strong>toTrim</strong><br>
	 * <br>
	 * 
	 * @param str
	 * @return
	 */
	public static String toTrim(String str){

		if(CCStringUtil.isEmpty(str)){
			return "";
		}else{
			return str.trim();
		}

	}

}
