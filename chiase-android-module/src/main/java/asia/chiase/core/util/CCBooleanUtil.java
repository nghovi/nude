package asia.chiase.core.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.exception.CCException;

/**
 * <strong>CCBooleanUtil</strong><br>
 * <br>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class CCBooleanUtil{

	/**
	 * <strong>checkBoolean</strong><br>
	 * <br>
	 * <ul>
	 * <li>parameter is "TRUE", "T", "1", "Y", "YES", return true
	 * <li>Other, return false
	 * </ul>
	 * 
	 * @param value
	 * @return
	 */
	public static boolean checkBoolean(Object value){
		if(value == null){
			return false;
		}

		if(value instanceof String){
			Set<String> set = new HashSet<String>();
			set.addAll(Arrays.asList("TRUE", "T", "1", "Y", "YES"));
			if(set.contains(CCStringUtil.toUpperCase(value))){
				return true;
			}else{
				return false;
			}
		}else if(value instanceof Integer){

			Integer val = (Integer)value;
			if(val == 1){
				return true;
			}else{
				return false;
			}
		}else if(value instanceof Boolean){
			return (Boolean)value;
		}else{
			return false;
		}
	}

	/**
	 * <strong>toString</strong><br>
	 * <br>
	 * <ul>
	 * <li> param is true, return "YES"
	 * <li> others, return "NO"
	 * </ul>
	 * 
	 * @param val
	 * @return
	 */
	public static String toString(Boolean val){
		if(val == null){
			return CCConst.NO;
		}else if(val == false){
			return CCConst.NO;
		}else{
			return CCConst.YES;
		}
	}

	/**
	 * <strong>toBoolean</strong><br>
	 * <br>
	 * <ul>
	 * <li>parameter is "TRUE", "T", "1", "Y", "YES", return true
	 * <li>others, return false
	 * </ul>
	 * 
	 * @param val
	 * @return
	 */
	public static Boolean toBoolean(Object val){

		try{
			if(val == null){
				return false;
			}else{
				return checkBoolean(val);
			}
		}catch(Exception ex){
			new CCException(ex);
			return false;
		}
	}

	// /**
	// * <strong>isLoginRequest</strong><br>
	// * <br>
	// *
	// * @param uri
	// * @return
	// */
	// public static Boolean isLoginRequest(String uri){
	// if(uri == null){
	// return false;
	// }
	// return uri.contains("/login");
	// }

}
